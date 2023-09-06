package com.mraof.minestuck.data.recipe;

import com.google.gson.JsonObject;
import com.mraof.minestuck.item.crafting.MSRecipeTypes;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.ItemLike;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.function.Consumer;

@ParametersAreNonnullByDefault
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
	public void save(Consumer<FinishedRecipe> recipeBuilder, ResourceLocation recipeName)
	{
		super.save(recipe -> recipeBuilder.accept(new ResultWrapper(recipe)), recipeName);
		
	}
	
	private static class ResultWrapper implements FinishedRecipe
	{
		private final FinishedRecipe shapedRecipe;
		
		private ResultWrapper(FinishedRecipe shapedRecipe)
		{
			this.shapedRecipe = shapedRecipe;
		}
		
		@Override
		public void serializeRecipeData(JsonObject jsonObject)
		{
			shapedRecipe.serializeRecipeData(jsonObject);
		}
		
		@Override
		public ResourceLocation getId()
		{
			return shapedRecipe.getId();
		}
		
		@Override
		public RecipeSerializer<?> getType()
		{
			return MSRecipeTypes.NON_MIRRORED.get();
		}
		
		@Nullable
		@Override
		public JsonObject serializeAdvancement()
		{
			return shapedRecipe.serializeAdvancement();
		}
		
		@Nullable
		@Override
		public ResourceLocation getAdvancementId()
		{
			return shapedRecipe.getAdvancementId();
		}
	}
}