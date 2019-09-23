package com.mraof.minestuck.item.crafting.alchemy;

import com.google.gson.JsonObject;
import com.mraof.minestuck.item.crafting.MSRecipeTypes;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.*;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.registries.ForgeRegistryEntry;

import javax.annotation.Nullable;
import java.util.Comparator;
import java.util.Optional;

public abstract class GristCostRecipe implements IRecipe<IInventory>
{
	
	public static GristSet findCostForItem(ItemStack input, GristType type, boolean shouldRoundDown, World world)
	{
		return findRecipeForItem(input, world).map(recipe -> recipe.getGristCost(input, type, shouldRoundDown)).orElse(null);
	}
	
	public static Optional<GristCostRecipe> findRecipeForItem(ItemStack input, World world)
	{
		return world.getRecipeManager().getRecipes(MSRecipeTypes.GRIST_COST_TYPE, new Inventory(input), world).stream().max(Comparator.comparingInt(GristCostRecipe::getPriority));
	}
	
	public final ResourceLocation id;
	public final Ingredient ingredient;
	@Nullable
	public final Integer priority;
	
	public GristCostRecipe(ResourceLocation id, Ingredient ingredient, Integer priority)
	{
		this.id = id;
		this.ingredient = ingredient;
		this.priority = priority;
	}
	
	@Override
	public boolean matches(IInventory inv, World worldIn)
	{
		return ingredient.test(inv.getStackInSlot(0));
	}
	
	@Override
	public ItemStack getCraftingResult(IInventory inv)
	{
		return inv.getStackInSlot(0);
	}
	
	@Override
	public boolean canFit(int width, int height)
	{
		return true;
	}
	
	@Override
	public ItemStack getRecipeOutput()
	{
		return ItemStack.EMPTY;
	}
	
	@Override
	public NonNullList<Ingredient> getIngredients()
	{
		return NonNullList.from(this.ingredient);
	}
	
	@Override
	public ResourceLocation getId()
	{
		return id;
	}
	
	@Override
	public IRecipeType<?> getType()
	{
		return MSRecipeTypes.GRIST_COST_TYPE;
	}
	
	public int getPriority()
	{
		if(priority == null)
			return priorityFromIngredient(ingredient);
		else return priority;
	}
	
	public abstract GristSet getGristCost(ItemStack input, GristType wildcardType, boolean shouldRoundDown);
	
	public boolean canPickWildcard()
	{
		return false;
	}
	
	private static int priorityFromIngredient(Ingredient ingredient)
	{
		return 100 - (ingredient.getMatchingStacks().length - 1)*10;
	}
	
	public static GristSet scaleToCountAndDurability(GristSet cost, ItemStack stack, boolean shouldRoundDown)
	{
		if (stack.getCount() != 1)
			cost.scale(stack.getCount());
		
		if (stack.isDamaged())
		{
			float multiplier = 1 - stack.getItem().getDamage(stack) / ((float) stack.getMaxDamage());
			cost.scale(multiplier, true);
		}
		
		return cost;
	}
	
	//Implementation classes below
	
	public static class Serializer extends ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<GristCostRecipe>
	{
		@Override
		public GristCostRecipe read(ResourceLocation recipeId, JsonObject json)
		{
			Ingredient ingredient = Ingredient.deserialize(json.get("ingredient"));
			Integer priority = json.has("priority") ? JSONUtils.getInt(json, "priority") : null;
			if(json.has("grist_cost"))
			{
				if(json.get("grist_cost").isJsonPrimitive())
				{
					int wildcardCost = JSONUtils.getInt(json, "grist_cost");
					return new WildcardGristCostRecipe(recipeId, ingredient, wildcardCost, priority);
				} else
				{
					GristSet set = GristSet.deserialize(json.getAsJsonObject("grist_cost"));
					return new SimpleGristCostRecipe(recipeId, ingredient, set, priority);
				}
			} else return new UnavailableGristCostRecipe(recipeId, ingredient, priority);
		}
		
		@Nullable
		@Override
		public GristCostRecipe read(ResourceLocation recipeId, PacketBuffer buffer)
		{
			Ingredient ingredient = Ingredient.read(buffer);
			int priority = buffer.readInt();
			byte type = buffer.readByte();
			switch(type)
			{
				case 0:
					return new SimpleGristCostRecipe(recipeId, ingredient, GristSet.read(buffer), priority);
				case 1:
					return new WildcardGristCostRecipe(recipeId, ingredient, buffer.readInt(), priority);
				default:
					return new UnavailableGristCostRecipe(recipeId, ingredient, priority);
			}
		}
		
		@Override
		public void write(PacketBuffer buffer, GristCostRecipe recipe)
		{
			recipe.ingredient.write(buffer);
			buffer.writeInt(recipe.getPriority());
			if(recipe instanceof SimpleGristCostRecipe)
			{
				buffer.writeByte(0);
				((SimpleGristCostRecipe) recipe).cost.write(buffer);
			} else if(recipe instanceof  WildcardGristCostRecipe)
			{
				buffer.writeByte(1);
				buffer.writeInt(((WildcardGristCostRecipe) recipe).wildcardCost);
			} else buffer.writeByte(-1);
		}
	}
	
	public static class UnavailableGristCostRecipe extends GristCostRecipe
	{
		public UnavailableGristCostRecipe(ResourceLocation id, Ingredient ingredient, Integer priority)
		{
			super(id, ingredient, priority);
		}
		
		@Override
		public GristSet getGristCost(ItemStack input, GristType wildcardType, boolean shouldRoundDown)
		{
			return null;
		}
		
		
		@Override
		public IRecipeSerializer<?> getSerializer()
		{
			return MSRecipeTypes.GRIST_COST;
		}
		
	}
	
	public static class SimpleGristCostRecipe extends GristCostRecipe
	{
		public GristSet cost;
		
		public SimpleGristCostRecipe(ResourceLocation id, Ingredient ingredient, GristSet cost, Integer priority)
		{
			super(id, ingredient, priority);
			this.cost = cost.asImmutable();
		}
		
		@Override
		public GristSet getGristCost(ItemStack input, GristType wildcardType, boolean shouldRoundDown)
		{
			return cost;
		}
		
		@Override
		public IRecipeSerializer<?> getSerializer()
		{
			return MSRecipeTypes.GRIST_COST;
		}
	}
	
	public static class WildcardGristCostRecipe extends GristCostRecipe
	{
		public final int wildcardCost;
		
		public WildcardGristCostRecipe(ResourceLocation id, Ingredient ingredient, int wildcardCost, Integer priority)
		{
			super(id, ingredient, priority);
			this.wildcardCost = wildcardCost;
		}
		
		@Override
		public GristSet getGristCost(ItemStack input, GristType wildcardType, boolean shouldRoundDown)
		{
			return wildcardType != null ? new GristSet(wildcardType, wildcardCost).asImmutable() : null;
		}
		
		@Override
		public boolean canPickWildcard()
		{
			return true;
		}
		
		@Override
		public IRecipeSerializer<?> getSerializer()
		{
			return MSRecipeTypes.GRIST_COST;
		}
	}
}