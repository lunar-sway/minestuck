package com.mraof.minestuck.data.recipe;

import com.mraof.minestuck.item.crafting.IrradiatingFallbackRecipe;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.item.crafting.RecipeType;

@MethodsReturnNonnullByDefault
public class IrradiatingFallbackRecipeBuilder
{
	private final RecipeType<? extends AbstractCookingRecipe> fallbackType;
	
	public IrradiatingFallbackRecipeBuilder(RecipeType<? extends AbstractCookingRecipe> fallbackType)
	{
		this.fallbackType = fallbackType;
	}
	
	public static IrradiatingFallbackRecipeBuilder fallback(RecipeType<? extends AbstractCookingRecipe> fallbackType)
	{
		return new IrradiatingFallbackRecipeBuilder(fallbackType);
	}
	
	public void build(RecipeOutput recipeOutput, ResourceLocation id)
	{
		recipeOutput.accept(id, new IrradiatingFallbackRecipe(fallbackType), null);
	}
}