package com.mraof.minestuck.data.recipe;

import com.google.gson.JsonObject;
import com.mraof.minestuck.item.crafting.MSRecipeTypes;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeSerializer;

import java.util.function.Consumer;

public final class GeneratedGristCostBuilder
{
	public static GeneratedGristCostBuilder create()
	{
		return new GeneratedGristCostBuilder();
	}
	
	private GeneratedGristCostBuilder()
	{}
	
	public void build(Consumer<FinishedRecipe> recipeSaver, ResourceLocation id)
	{
		recipeSaver.accept(new GeneratedGristCostBuilder.Result(id.withPrefix("grist_costs/")));
	}
	
	private record Result(ResourceLocation id) implements AdvancementFreeRecipe
	{
		@Override
		public void serializeRecipeData(JsonObject jsonObject)
		{
		}
		
		@Override
		public ResourceLocation getId()
		{
			return id;
		}
		
		@Override
		public RecipeSerializer<?> getType()
		{
			return MSRecipeTypes.RECIPE_GRIST_COST.get();
		}
	}
}
