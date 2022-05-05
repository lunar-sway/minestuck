package com.mraof.minestuck.item.crafting.alchemy;

import com.google.gson.JsonObject;
import com.mraof.minestuck.item.crafting.MSRecipeTypes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;

public class UnavailableGristCost extends GristCostRecipe
{
	public UnavailableGristCost(ResourceLocation id, Ingredient ingredient, Integer priority)
	{
		super(id, ingredient, priority);
	}
	
	@Override
	public GristSet getGristCost(ItemStack input, GristType wildcardType, boolean shouldRoundDown, Level level)
	{
		return null;
	}
	
	@Override
	public RecipeSerializer<?> getSerializer()
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
		protected UnavailableGristCost read(ResourceLocation recipeId, FriendlyByteBuf buffer, Ingredient ingredient, int priority)
		{
			return new UnavailableGristCost(recipeId, ingredient, priority);
		}
		
		@Override
		public void toNetwork(FriendlyByteBuf buffer, UnavailableGristCost recipe)
		{
			super.toNetwork(buffer, recipe);
			//Do nothing more
		}
	}
}
