package com.mraof.minestuck.data;

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
	public void build(Consumer<IFinishedRecipe> recipeBuilder, ResourceLocation recipeName)
	{
		super.build(recipe -> recipeBuilder.accept(new ResultWrapper(recipe)), recipeName);
		
	}
	
	private static class ResultWrapper implements IFinishedRecipe
	{
		private final IFinishedRecipe shapedRecipe;
		
		private ResultWrapper(IFinishedRecipe shapedRecipe)
		{
			this.shapedRecipe = shapedRecipe;
		}
		
		@Override
		public void serialize(JsonObject jsonObject)
		{
			shapedRecipe.serialize(jsonObject);
		}
		
		@Override
		public ResourceLocation getID()
		{
			return shapedRecipe.getID();
		}
		
		@Override
		public IRecipeSerializer<?> getSerializer()
		{
			return MSRecipeTypes.NON_MIRRORED;
		}
		
		@Nullable
		@Override
		public JsonObject getAdvancementJson()
		{
			return shapedRecipe.getAdvancementJson();
		}
		
		@Nullable
		@Override
		public ResourceLocation getAdvancementID()
		{
			return shapedRecipe.getAdvancementID();
		}
	}
}