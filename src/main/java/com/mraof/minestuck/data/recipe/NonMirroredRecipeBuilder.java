package com.mraof.minestuck.data.recipe;

import com.google.gson.JsonObject;
import com.mraof.minestuck.item.crafting.MSRecipeTypes;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeSerializer;
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
			public void accept(FinishedRecipe finishedRecipe, ICondition... conditions)
			{
				recipeOutput.accept(new ResultWrapper(finishedRecipe), conditions);
			}
		}, recipeName);
		
	}
	
	private record ResultWrapper(FinishedRecipe shapedRecipe) implements FinishedRecipe
	{
		
		@Override
		public void serializeRecipeData(JsonObject jsonObject)
		{
			shapedRecipe.serializeRecipeData(jsonObject);
		}
		
		@Override
		public ResourceLocation id()
		{
			return shapedRecipe.id();
		}
		
		@Override
		public RecipeSerializer<?> type()
		{
			return MSRecipeTypes.NON_MIRRORED.get();
		}
		
		@Nullable
		@Override
		public AdvancementHolder advancement()
		{
			return this.shapedRecipe.advancement();
		}
	}
}