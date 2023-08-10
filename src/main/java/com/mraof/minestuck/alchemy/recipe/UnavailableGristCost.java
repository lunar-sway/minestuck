package com.mraof.minestuck.alchemy.recipe;

import com.google.gson.JsonObject;
import com.mraof.minestuck.api.alchemy.GristSet;
import com.mraof.minestuck.api.alchemy.GristType;
import com.mraof.minestuck.item.crafting.MSRecipeTypes;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class UnavailableGristCost extends SimpleGristCost
{
	public UnavailableGristCost(ResourceLocation id, Ingredient ingredient, Integer priority)
	{
		super(id, ingredient, priority);
	}
	
	@Nullable
	@Override
	public GristSet getGristCost(ItemStack input, @Nullable GristType wildcardType, boolean shouldRoundDown, @Nullable Level level)
	{
		return null;
	}
	
	@Override
	public RecipeSerializer<?> getSerializer()
	{
		return MSRecipeTypes.UNAVAILABLE_GRIST_COST.get();
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
