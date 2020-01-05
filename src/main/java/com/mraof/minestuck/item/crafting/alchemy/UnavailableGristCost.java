package com.mraof.minestuck.item.crafting.alchemy;

import com.google.gson.JsonObject;
import com.mraof.minestuck.item.crafting.MSRecipeTypes;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;

public class UnavailableGristCost extends GristCostRecipe
{
	public UnavailableGristCost(ResourceLocation id, Ingredient ingredient, Integer priority)
	{
		super(id, ingredient, priority);
	}
	
	@Override
	public GristSet getGristCost(ItemStack input, GristType wildcardType, boolean shouldRoundDown)
	{
		return null;
	}
	
	@Override
	public IRecipeSerializer<?> getSerializer()
	{
		return MSRecipeTypes.UNAVAILABLE_GRIST_COST;
	}
	
	public static class Serializer extends AbstractSerializer<UnavailableGristCost>
	{
		@Override
		protected UnavailableGristCost read(ResourceLocation recipeId, JsonObject json, Ingredient ingredient, Integer priority)
		{
			return new UnavailableGristCost(recipeId, ingredient, priority);
		}
		
		@Override
		protected UnavailableGristCost read(ResourceLocation recipeId, PacketBuffer buffer, Ingredient ingredient, int priority)
		{
			return new UnavailableGristCost(recipeId, ingredient, priority);
		}
		
		@Override
		public void write(PacketBuffer buffer, UnavailableGristCost recipe)
		{
			super.write(buffer, recipe);
			//Do nothing more
		}
	}
}
