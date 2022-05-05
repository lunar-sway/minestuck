package com.mraof.minestuck.item.crafting.alchemy;

import com.google.gson.JsonObject;
import com.mraof.minestuck.item.crafting.MSRecipeTypes;
import com.mraof.minestuck.item.crafting.alchemy.generator.GeneratedCostProvider;
import com.mraof.minestuck.item.crafting.alchemy.generator.GenerationContext;
import com.mraof.minestuck.item.crafting.alchemy.generator.GristCostResult;
import com.mraof.minestuck.jei.JeiGristCost;
import net.minecraft.core.NonNullList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.ForgeRegistryEntry;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.function.BiConsumer;

public abstract class GristCostRecipe implements Recipe<Container>
{
	
	public static GristSet findCostForItem(ItemStack input, GristType type, boolean shouldRoundDown, Level level)
	{
		return findRecipeForItem(input, level).map(recipe -> recipe.getGristCost(input, type, shouldRoundDown, level)).orElse(null);
	}
	
	public static Optional<GristCostRecipe> findRecipeForItem(ItemStack input, Level level)
	{
		return findRecipeForItem(input, level, level.getRecipeManager());
	}
	
	public static Optional<GristCostRecipe> findRecipeForItem(ItemStack input, Level level, RecipeManager recipeManager)
	{
		return recipeManager.getRecipesFor(MSRecipeTypes.GRIST_COST_TYPE, new SimpleContainer(input), level).stream().max(Comparator.comparingInt(GristCostRecipe::getPriority));
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
	public boolean matches(Container inv, Level level)
	{
		return ingredient.test(inv.getItem(0));
	}
	
	@Override
	public ItemStack assemble(Container inv)
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
	public RecipeType<?> getType()
	{
		return MSRecipeTypes.GRIST_COST_TYPE;
	}
	
	public int getPriority()
	{
		if(priority == null)
			return priorityFromIngredient(ingredient);
		else return priority;
	}
	
	public abstract GristSet getGristCost(ItemStack input, GristType wildcardType, boolean shouldRoundDown, @Nullable Level level);
	
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
	
	public List<JeiGristCost> getJeiCosts(Level level)
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
	public abstract static class AbstractSerializer<T extends GristCostRecipe> extends ForgeRegistryEntry<RecipeSerializer<?>> implements RecipeSerializer<T>
	{
		@Override
		public T fromJson(ResourceLocation recipeId, JsonObject json)
		{
			Ingredient ingredient = Ingredient.fromJson(json.get("ingredient"));
			Integer priority = json.has("priority") ? GsonHelper.getAsInt(json, "priority") : null;
			
			return read(recipeId, json, ingredient, priority);
		}
		
		protected abstract T read(ResourceLocation recipeId, JsonObject json, Ingredient ingredient, Integer priority);
		
		@Nullable
		@Override
		public T fromNetwork(ResourceLocation recipeId, FriendlyByteBuf buffer)
		{
			Ingredient ingredient = Ingredient.fromNetwork(buffer);
			int priority = buffer.readInt();
			
			return read(recipeId, buffer, ingredient, priority);
		}
		
		protected abstract T read(ResourceLocation recipeId, FriendlyByteBuf buffer, Ingredient ingredient, int priority);
		
		@Override
		public void toNetwork(FriendlyByteBuf buffer, T recipe)
		{
			recipe.ingredient.toNetwork(buffer);
			buffer.writeInt(recipe.getPriority());
		}
	}
}