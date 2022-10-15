package com.mraof.minestuck.data.recipe;

import com.google.gson.JsonObject;
import com.mraof.minestuck.item.crafting.MSRecipeTypes;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.ItemLike;

import javax.annotation.Nullable;
import java.util.function.Consumer;

public class NonMirroredRecipeBuilder extends ShapedRecipeBuilder
{
	public NonMirroredRecipeBuilder(ItemLike result, int size)
	{
		super(result, size);
	}
	
	public static NonMirroredRecipeBuilder nonMirroredRecipe(ItemLike result)
	{
		return nonMirroredRecipe(result, 1);
	}
	
	public static NonMirroredRecipeBuilder nonMirroredRecipe(ItemLike result, int size)
	{
		return new NonMirroredRecipeBuilder(result, size);
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