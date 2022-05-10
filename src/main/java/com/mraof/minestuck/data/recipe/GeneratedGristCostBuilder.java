package com.mraof.minestuck.data.recipe;

import com.google.gson.JsonObject;
import com.mraof.minestuck.item.crafting.MSRecipeTypes;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.util.ResourceLocation;

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
	
	public void build(Consumer<IFinishedRecipe> recipeSaver, ResourceLocation id)
	{
		recipeSaver.accept(new GeneratedGristCostBuilder.Result(new ResourceLocation(id.getNamespace(), "grist_costs/"+id.getPath())));
	}
	
	public static class Result implements IFinishedRecipe
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
		public IRecipeSerializer<?> getType()
		{
			return MSRecipeTypes.RECIPE_GRIST_COST;
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
