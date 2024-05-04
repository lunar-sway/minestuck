package com.mraof.minestuck.api.alchemy.recipe.combination;

import com.mraof.minestuck.item.crafting.MSRecipeTypes;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;

import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;

@MethodsReturnNonnullByDefault
public interface CombinationRecipe extends Recipe<CombinerContainer>
{
	Supplier<RecipeType<CombinationRecipe>> RECIPE_TYPE = MSRecipeTypes.COMBINATION_TYPE;
	
	static ItemStack findResult(CombinerContainer combiner, Level level)
	{
		return level.getRecipeManager().getRecipeFor(CombinationRecipe.RECIPE_TYPE.get(), combiner, level)
				.map(recipe -> recipe.value().assemble(combiner, level.registryAccess())).orElse(ItemStack.EMPTY);
	}
	
	@Override
	default boolean canCraftInDimensions(int width, int height)
	{
		return width * height >= 2;
	}
	
	@Override
	default RecipeType<?> getType()
	{
		return CombinationRecipe.RECIPE_TYPE.get();
	}
	
	default List<JeiCombination> getJeiCombinations()
	{
		return Collections.emptyList();
	}
}