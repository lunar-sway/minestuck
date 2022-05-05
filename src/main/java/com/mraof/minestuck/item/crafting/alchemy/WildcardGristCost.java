package com.mraof.minestuck.item.crafting.alchemy;

import com.google.gson.JsonObject;
import com.mraof.minestuck.item.crafting.MSRecipeTypes;
import com.mraof.minestuck.jei.JeiGristCost;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;

import java.util.Collections;
import java.util.List;

public class WildcardGristCost extends GristCostRecipe
{
	private final long wildcardCost;
	
	public WildcardGristCost(ResourceLocation id, Ingredient ingredient, long wildcardCost, Integer priority)
	{
		super(id, ingredient, priority);
		this.wildcardCost = wildcardCost;
	}
	
	@Override
	public GristSet getGristCost(ItemStack input, GristType wildcardType, boolean shouldRoundDown, Level level)
	{
		return wildcardType != null ? scaleToCountAndDurability(new GristSet(wildcardType, wildcardCost), input, shouldRoundDown) : null;
	}
	
	@Override
	public boolean canPickWildcard()
	{
		return true;
	}
	
	@Override
	public List<JeiGristCost> getJeiCosts(Level level)
	{
		return Collections.singletonList(new JeiGristCost.Wildcard(ingredient, wildcardCost));
	}
	
	@Override
	public RecipeSerializer<?> getSerializer()
	{
		return MSRecipeTypes.WILDCARD_GRIST_COST;
	}
	
	public static class Serializer extends AbstractSerializer<WildcardGristCost>
	{
		@Override
		protected WildcardGristCost read(ResourceLocation recipeId, JsonObject json, Ingredient ingredient, Integer priority)
		{
			long wildcardCost = GsonHelper.getAsLong(json, "grist_cost");
			return new WildcardGristCost(recipeId, ingredient, wildcardCost, priority);
		}
		
		@Override
		protected WildcardGristCost read(ResourceLocation recipeId, FriendlyByteBuf buffer, Ingredient ingredient, int priority)
		{
			long wildcardCost = buffer.readLong();
			return new WildcardGristCost(recipeId, ingredient, wildcardCost, priority);
		}
		
		@Override
		public void toNetwork(FriendlyByteBuf buffer, WildcardGristCost recipe)
		{
			super.toNetwork(buffer, recipe);
			buffer.writeLong(recipe.wildcardCost);
		}
	}
}
