package com.mraof.minestuck.data.recipe;

import com.google.gson.JsonObject;
import com.mraof.minestuck.item.crafting.MSRecipeTypes;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeSerializer;

import javax.annotation.Nullable;
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
		recipeOutput.accept(new GeneratedGristCostBuilder.Result(id.withPrefix("grist_costs/")));
	}
	
	private record Result(ResourceLocation id) implements FinishedRecipe
	{
		@Override
		public void serializeRecipeData(JsonObject jsonObject)
		{
		}
		
		@Override
		public ResourceLocation id()
		{
			return id;
		}
		
		@Override
		public RecipeSerializer<?> type()
		{
			return MSRecipeTypes.RECIPE_GRIST_COST.get();
		}
		
		@Nullable
		@Override
		public AdvancementHolder advancement()
		{
			return null;
		}
	}
}
