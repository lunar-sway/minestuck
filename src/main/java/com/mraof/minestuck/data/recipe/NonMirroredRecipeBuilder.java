package com.mraof.minestuck.data.recipe;

import com.mraof.minestuck.item.crafting.NonMirroredRecipe;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.world.level.ItemLike;
import net.neoforged.neoforge.common.conditions.ICondition;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class NonMirroredRecipeBuilder extends ShapedRecipeBuilder
{
	public NonMirroredRecipeBuilder(RecipeCategory category, ItemLike result, int size)
	{
		super(category, result, size);
	}
	
	public static NonMirroredRecipeBuilder nonMirroredRecipe(RecipeCategory category, ItemLike result)
	{
		return nonMirroredRecipe(category, result, 1);
	}
	
	public static NonMirroredRecipeBuilder nonMirroredRecipe(RecipeCategory category, ItemLike result, int size)
	{
		return new NonMirroredRecipeBuilder(category, result, size);
	}
	
	@Override
	public void save(RecipeOutput recipeOutput, ResourceLocation recipeName)
	{
		super.save(new RecipeOutput()
		{
			@Override
			public Advancement.Builder advancement()
			{
				return recipeOutput.advancement();
			}
			
			@Override
			public void accept(ResourceLocation id, Recipe<?> recipe, @Nullable AdvancementHolder advancement, ICondition... conditions)
			{
				if(recipe instanceof ShapedRecipe shapedRecipe)
					recipeOutput.accept(id, new NonMirroredRecipe(shapedRecipe), advancement, conditions);
			}
		}, recipeName);
		
	}
}