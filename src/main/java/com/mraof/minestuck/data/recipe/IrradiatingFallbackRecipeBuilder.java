package com.mraof.minestuck.data.recipe;

import com.google.gson.JsonObject;
import com.mraof.minestuck.item.crafting.MSRecipeTypes;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;

import javax.annotation.Nullable;
import java.util.Objects;

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
		recipeOutput.accept(new Result(id, fallbackType));
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
			ResourceLocation typeLocation = Objects.requireNonNull(BuiltInRegistries.RECIPE_TYPE.getKey(fallbackType));
			jsonObject.addProperty("fallback_type", typeLocation.toString());
		}
		
		@Override
		public ResourceLocation id()
		{
			return id;
		}
		
		@Override
		public RecipeSerializer<?> type()
		{
			return MSRecipeTypes.IRRADIATING_FALLBACK.get();
		}
		
		@Nullable
		@Override
		public AdvancementHolder advancement()
		{
			return null;
		}
	}
}