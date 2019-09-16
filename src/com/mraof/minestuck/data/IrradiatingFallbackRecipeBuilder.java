package com.mraof.minestuck.data;

import com.google.gson.JsonObject;
import com.mraof.minestuck.item.crafting.MSRecipeTypes;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.item.crafting.AbstractCookingRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;

import javax.annotation.Nullable;
import java.util.function.Consumer;

public class IrradiatingFallbackRecipeBuilder
{
	private final IRecipeType<? extends AbstractCookingRecipe> fallbackType;
	
	public IrradiatingFallbackRecipeBuilder(IRecipeType<? extends AbstractCookingRecipe> fallbackType)
	{
		this.fallbackType = fallbackType;
	}
	
	public static IrradiatingFallbackRecipeBuilder fallback(IRecipeType<? extends AbstractCookingRecipe> fallbackType)
	{
		return new IrradiatingFallbackRecipeBuilder(fallbackType);
	}
	
	public void build(Consumer<IFinishedRecipe> recipeSaver, ResourceLocation id)
	{
		recipeSaver.accept(new Result(id, fallbackType));
	}
	
	public static class Result implements IFinishedRecipe
	{
		private final ResourceLocation id;
		private final IRecipeType<? extends AbstractCookingRecipe> fallbackType;
		
		public Result(ResourceLocation id, IRecipeType<? extends AbstractCookingRecipe> fallbackType)
		{
			this.id = id;
			this.fallbackType = fallbackType;
		}
		
		@Override
		public void serialize(JsonObject jsonObject)
		{
			jsonObject.addProperty("fallback_type", Registry.RECIPE_TYPE.getKey(fallbackType).toString());
		}
		
		@Override
		public ResourceLocation getID()
		{
			return id;
		}
		
		@Override
		public IRecipeSerializer<?> getSerializer()
		{
			return MSRecipeTypes.IRRADIATING_FALLBACK;
		}
		
		@Nullable
		@Override
		public JsonObject getAdvancementJson()
		{
			return null;
		}
		
		@Nullable
		@Override
		public ResourceLocation getAdvancementID()
		{
			return new ResourceLocation("");
		}
	}
}