package com.mraof.minestuck.data.recipe;

import com.mraof.minestuck.alchemy.recipe.generator.recipe.RecipeGeneratedGristCost;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceLocation;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public final class GeneratedGristCostBuilder
{
	public static GeneratedGristCostBuilder create()
	{
		return new GeneratedGristCostBuilder();
	}
	
	private GeneratedGristCostBuilder()
	{}
	
	public void build(RecipeOutput recipeOutput, ResourceLocation id)
	{
		recipeOutput.accept(id.withPrefix("grist_costs/"), new RecipeGeneratedGristCost(), null);
	}
}
