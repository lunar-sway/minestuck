package com.mraof.minestuck.data.recipe;

import com.google.gson.JsonObject;
import com.mraof.minestuck.item.crafting.MSRecipeTypes;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.data.ShapedRecipeBuilder;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;
import java.util.function.Consumer;

public class NonMirroredRecipeBuilder extends ShapedRecipeBuilder
{
	public NonMirroredRecipeBuilder(IItemProvider result, int size)
	{
		super(result, size);
	}
	
	public static NonMirroredRecipeBuilder nonMirroredRecipe(IItemProvider result)
	{
		return nonMirroredRecipe(result, 1);
	}
	
	public static NonMirroredRecipeBuilder nonMirroredRecipe(IItemProvider result, int size)
	{
		return new NonMirroredRecipeBuilder(result, size);
	}
	
	@Override
	public void save(Consumer<IFinishedRecipe> recipeBuilder, ResourceLocation recipeName)
	{
		super.save(recipe -> recipeBuilder.accept(new ResultWrapper(recipe)), recipeName);
		
	}
	
	private static class ResultWrapper implements IFinishedRecipe
	{
		private final IFinishedRecipe shapedRecipe;
		
		private ResultWrapper(IFinishedRecipe shapedRecipe)
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
		public IRecipeSerializer<?> getType()
		{
			return MSRecipeTypes.NON_MIRRORED;
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