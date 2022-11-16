package com.mraof.minestuck.data.recipe;

import com.google.gson.JsonObject;
import com.mraof.minestuck.item.crafting.MSRecipeTypes;
import net.minecraft.core.Registry;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;

import javax.annotation.Nullable;
import java.util.function.Consumer;

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
	
	public void build(Consumer<FinishedRecipe> recipeSaver, ResourceLocation id)
	{
		recipeSaver.accept(new Result(id, fallbackType));
	}
	
	public static class Result implements FinishedRecipe
	{
		private final ResourceLocation id;
		private final RecipeType<? extends AbstractCookingRecipe> fallbackType;
		
		public Result(ResourceLocation id, RecipeType<? extends AbstractCookingRecipe> fallbackType)
		{
			this.id = id;
			this.fallbackType = fallbackType;
		}
		
		@Override
		public void serializeRecipeData(JsonObject jsonObject)
		{
			jsonObject.addProperty("fallback_type", Registry.RECIPE_TYPE.getKey(fallbackType).toString());
		}
		
		@Override
		public ResourceLocation getId()
		{
			return id;
		}
		
		@Override
		public RecipeSerializer<?> getType()
		{
			return MSRecipeTypes.IRRADIATING_FALLBACK.get();
		}
		
		@Nullable
		@Override
		public JsonObject serializeAdvancement()
		{
			return null;
		}
		
		@Nullable
		@Override
		public ResourceLocation getAdvancementId()
		{
			return new ResourceLocation("");
		}
	}
}