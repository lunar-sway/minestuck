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

public class GristCostRecipe implements IRecipe<IInventory>
{
	
	public static GristSet findCostForItem(ItemStack input, World world)
	{
		return world.getRecipeManager().getRecipes(MSRecipeTypes.GRIST_COST_TYPE, new Inventory(input), world).stream().max(Comparator.comparingInt(GristCostRecipe::getPriority)).map(GristCostRecipe::getGristCost).orElse(null);
	}
	
	public final ResourceLocation id;
	public final Ingredient ingredient;
	@Nullable
	public final GristSet cost;
	@Nullable
	public final Integer priority;
	
	public GristCostRecipe(ResourceLocation id, Ingredient ingredient, GristSet cost, Integer priority)
	{
		this.id = id;
		this.ingredient = ingredient;
		this.cost = cost != null ? cost.asImmutable() : null;
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
	public IRecipeSerializer<?> getSerializer()
	{
		return MSRecipeTypes.GRIST_COST;
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
	
	public GristSet getGristCost()
	{
		return cost;
	}
	
	private static int priorityFromIngredient(Ingredient ingredient)
	{
		return 100 - (ingredient.getMatchingStacks().length - 1)*10;
	}
	
	public static class Serializer extends ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<GristCostRecipe>
	{
		@Override
		public GristCostRecipe read(ResourceLocation recipeId, JsonObject json)
		{
			Ingredient ingredient = Ingredient.deserialize(json.get("ingredient"));
			GristSet set = json.has("grist_cost") ? GristSet.deserialize(json.getAsJsonObject("grist_cost")) : null;
			Integer priority = json.has("priority") ? JSONUtils.getInt(json, "priority") : null;
			
			return new GristCostRecipe(recipeId, ingredient, set, priority);
		}
		
		@Nullable
		@Override
		public GristCostRecipe read(ResourceLocation recipeId, PacketBuffer buffer)
		{
			Ingredient ingredient = Ingredient.read(buffer);
			GristSet set = buffer.readBoolean() ? GristSet.read(buffer) : null;
			int priority = buffer.readInt();
			
			return new GristCostRecipe(recipeId, ingredient, set, priority);
		}
		
		@Override
		public void write(PacketBuffer buffer, GristCostRecipe recipe)
		{
			recipe.ingredient.write(buffer);
			buffer.writeBoolean(recipe.cost != null);
			if(recipe.cost != null)
				recipe.cost.write(buffer);
			buffer.writeInt(recipe.getPriority());
		}
	}
}