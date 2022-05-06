package com.mraof.minestuck.item.crafting.alchemy;

import com.google.gson.JsonObject;
import com.mraof.minestuck.item.crafting.MSRecipeTypes;
import com.mraof.minestuck.item.crafting.alchemy.generator.GeneratedCostProvider;
import com.mraof.minestuck.item.crafting.alchemy.generator.GenerationContext;
import com.mraof.minestuck.item.crafting.alchemy.generator.GristCostResult;
import com.mraof.minestuck.jei.JeiGristCost;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.*;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.registries.ForgeRegistryEntry;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.function.BiConsumer;

public abstract class GristCostRecipe implements IRecipe<IInventory>
{
	
	public static GristSet findCostForItem(ItemStack input, GristType type, boolean shouldRoundDown, World world)
	{
		return findRecipeForItem(input, world).map(recipe -> recipe.getGristCost(input, type, shouldRoundDown, world)).orElse(null);
	}
	
	public static Optional<GristCostRecipe> findRecipeForItem(ItemStack input, World world)
	{
		return findRecipeForItem(input, world, world.getRecipeManager());
	}
	
	public static Optional<GristCostRecipe> findRecipeForItem(ItemStack input, World world, RecipeManager recipeManager)
	{
		return recipeManager.getRecipesFor(MSRecipeTypes.GRIST_COST_TYPE, new Inventory(input), world).stream().max(Comparator.comparingInt(GristCostRecipe::getPriority));
	}
	
	public final ResourceLocation id;
	protected final Ingredient ingredient;
	@Nullable
	protected final Integer priority;
	
	public GristCostRecipe(ResourceLocation id, Ingredient ingredient, @Nullable Integer priority)
	{
		this.id = id;
		this.ingredient = ingredient;
		this.priority = priority;
	}
	
	@Override
	public boolean matches(IInventory inv, World worldIn)
	{
		return ingredient.test(inv.getItem(0));
	}
	
	@Override
	public ItemStack assemble(IInventory inv)
	{
		return inv.getItem(0);
	}
	
	@Override
	public boolean canCraftInDimensions(int width, int height)
	{
		return true;
	}
	
	@Override
	public boolean isSpecial()	//Makes sure that the recipe is not unlockable (because recipe book categories are hardcoded to vanilla categories)
	{
		return true;
	}
	
	@Override
	public ItemStack getResultItem()
	{
		return ItemStack.EMPTY;
	}
	
	@Override
	public NonNullList<Ingredient> getIngredients()
	{
		return NonNullList.of(this.ingredient);
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
	
	public abstract GristSet getGristCost(ItemStack input, GristType wildcardType, boolean shouldRoundDown, @Nullable World world);
	
	public boolean canPickWildcard()
	{
		return false;
	}
	
	public void addCostProvider(BiConsumer<Item, GeneratedCostProvider> consumer)
	{
		GeneratedCostProvider provider = new DefaultProvider();
		for(ItemStack stack : ingredient.getItems())
			consumer.accept(stack.getItem(), provider);
	}
	
	private class DefaultProvider implements GeneratedCostProvider
	{
		@Override
		public GristCostResult generate(Item item, GristCostResult lastCost, GenerationContext context)
		{
			if(lastCost == null && ingredient.test(new ItemStack(item)))
				return new GristCostResult(getGristCost(new ItemStack(item), GristTypes.BUILD.get(), false, null));
			else return null;
		}
	}
	
	public List<JeiGristCost> getJeiCosts(World world)
	{
		return Collections.emptyList();
	}
	
	private static int priorityFromIngredient(Ingredient ingredient)
	{
		return 100 - (ingredient.getItems().length - 1)*10;
	}
	
	public static GristSet scaleToCountAndDurability(GristSet cost, ItemStack stack, boolean shouldRoundDown)
	{
		if(cost == null)
			return null;
		
		cost = cost.copy();
		if (stack.getCount() != 1)
			cost.scale(stack.getCount());
		
		if (stack.isDamaged())
		{
			float multiplier = 1 - stack.getItem().getDamage(stack) / ((float) stack.getMaxDamage());
			cost.scale(multiplier, shouldRoundDown);
		}
		
		return cost.asImmutable();
	}
	
	//Helper class for implementing serializer classes
	public abstract static class AbstractSerializer<T extends GristCostRecipe> extends ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<T>
	{
		@Override
		public T fromJson(ResourceLocation recipeId, JsonObject json)
		{
			Ingredient ingredient = Ingredient.fromJson(json.get("ingredient"));
			Integer priority = json.has("priority") ? JSONUtils.getAsInt(json, "priority") : null;
			
			return read(recipeId, json, ingredient, priority);
		}
		
		protected abstract T read(ResourceLocation recipeId, JsonObject json, Ingredient ingredient, Integer priority);
		
		@Nullable
		@Override
		public T fromNetwork(ResourceLocation recipeId, PacketBuffer buffer)
		{
			Ingredient ingredient = Ingredient.fromNetwork(buffer);
			int priority = buffer.readInt();
			
			return read(recipeId, buffer, ingredient, priority);
		}
		
		protected abstract T read(ResourceLocation recipeId, PacketBuffer buffer, Ingredient ingredient, int priority);
		
		@Override
		public void toNetwork(PacketBuffer buffer, T recipe)
		{
			recipe.ingredient.toNetwork(buffer);
			buffer.writeInt(recipe.getPriority());
		}
	}
}