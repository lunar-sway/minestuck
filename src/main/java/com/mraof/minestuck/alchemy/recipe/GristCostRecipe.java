package com.mraof.minestuck.alchemy.recipe;

import com.mraof.minestuck.alchemy.recipe.generator.GeneratedCostProvider;
import com.mraof.minestuck.alchemy.recipe.generator.GristCostResult;
import com.mraof.minestuck.api.alchemy.GristSet;
import com.mraof.minestuck.api.alchemy.GristType;
import com.mraof.minestuck.api.alchemy.GristTypes;
import com.mraof.minestuck.api.alchemy.MutableGristSet;
import com.mraof.minestuck.item.crafting.MSRecipeTypes;
import com.mraof.minestuck.jei.JeiGristCost;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.RegistryAccess;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.function.BiConsumer;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public interface GristCostRecipe extends Recipe<Container>
{
	@Nullable
	static GristSet findCostForItem(ItemStack input, @Nullable GristType wildcardType, boolean shouldRoundDown, Level level)
	{
		return findRecipeForItem(input, level).map(recipe -> recipe.getGristCost(input, wildcardType, shouldRoundDown, level)).orElse(null);
	}
	
	static Optional<GristCostRecipe> findRecipeForItem(ItemStack input, Level level)
	{
		return findRecipeForItem(input, level, level.getRecipeManager());
	}
	
	static Optional<GristCostRecipe> findRecipeForItem(ItemStack input, Level level, RecipeManager recipeManager)
	{
		return recipeManager.getRecipesFor(MSRecipeTypes.GRIST_COST_TYPE.get(), new SimpleContainer(input), level).stream().max(Comparator.comparingInt(GristCostRecipe::getPriority));
	}
	
	@Override
	default ItemStack assemble(Container inv, RegistryAccess registryAccess)
	{
		return inv.getItem(0);
	}
	
	@Override
	default boolean canCraftInDimensions(int width, int height)
	{
		return true;
	}
	
	@Override
	default boolean isSpecial()
	{
		//Makes sure that the recipe is not unlockable (because recipe book categories are hardcoded to vanilla categories)
		return true;
	}
	
	@Override
	default ItemStack getResultItem(RegistryAccess registryAccess)
	{
		return ItemStack.EMPTY;
	}
	
	@Override
	default RecipeType<GristCostRecipe> getType()
	{
		return MSRecipeTypes.GRIST_COST_TYPE.get();
	}
	
	int getPriority();
	
	@Nullable
	GristSet getGristCost(ItemStack input, @Nullable GristType wildcardType, boolean shouldRoundDown, @Nullable Level level);
	
	default boolean canPickWildcard()
	{
		return false;
	}
	
	/**
	 * Adds grist cost providers for all items which this recipe might potentially provide a grist cost for,
	 * which then get used during grist cost generation.
	 */
	void addCostProvider(BiConsumer<Item, GeneratedCostProvider> consumer);
	
	default List<JeiGristCost> getJeiCosts(Level level)
	{
		return Collections.emptyList();
	}
	
	static int defaultPriority(Ingredient ingredient)
	{
		return 100 - (ingredient.getItems().length - 1)*10;
	}
	
	static void addSimpleCostProvider(BiConsumer<Item, GeneratedCostProvider> consumer, GristCostRecipe recipe, Ingredient ingredient)
	{
		addCostProviderForIngredient(consumer, ingredient, (item, context) ->
				new GristCostResult(recipe.getGristCost(new ItemStack(item), GristTypes.BUILD.get(), false, null)));
	}
	
	static void addCostProviderForIngredient(BiConsumer<Item, GeneratedCostProvider> consumer, Ingredient ingredient, GeneratedCostProvider provider)
	{
		for(ItemStack stack : ingredient.getItems())
			consumer.accept(stack.getItem(), provider);
	}
	
	@Nullable
	static GristSet scaleToCountAndDurability(@Nullable GristSet cost, ItemStack stack, boolean shouldRoundDown)
	{
		if(cost == null)
			return null;
		
		if(stack.getCount() == 1 && !stack.isDamaged())
			return cost;
		
		MutableGristSet mutableCost = cost.mutableCopy();
		if (stack.getCount() != 1)
			mutableCost.scale(stack.getCount());
		
		if (stack.isDamaged())
		{
			float multiplier = 1 - stack.getItem().getDamage(stack) / ((float) stack.getMaxDamage());
			mutableCost.scale(multiplier, shouldRoundDown);
		}
		
		return mutableCost;
	}
}