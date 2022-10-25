package com.mraof.minestuck.data.recipe;

import com.google.gson.JsonObject;
import com.mraof.minestuck.item.crafting.MSRecipeTypes;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeSerializer;

import javax.annotation.Nullable;
import java.util.function.Consumer;

public class GeneratedGristCostBuilder
{
	public static GeneratedGristCostBuilder create()
	{
		return new GeneratedGristCostBuilder();
	}
	
	protected GeneratedGristCostBuilder()
	{}
	
	public void build(Consumer<FinishedRecipe> recipeSaver, ResourceLocation id)
	{
		recipeSaver.accept(new GeneratedGristCostBuilder.Result(new ResourceLocation(id.getNamespace(), "grist_costs/"+id.getPath())));
	}
	
	public static class Result implements FinishedRecipe
	{
		public final ResourceLocation id;
		
		public Result(ResourceLocation id)
		{
			this.id = id;
		}
		
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
