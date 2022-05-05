package com.mraof.minestuck.item.crafting.alchemy;

import com.google.gson.JsonObject;
import com.mraof.minestuck.item.crafting.MSRecipeTypes;
import com.mraof.minestuck.jei.JeiGristCost;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;

import java.util.Collections;
import java.util.List;

public class GristCost extends GristCostRecipe
{
	private final ImmutableGristSet cost;
	
	public GristCost(ResourceLocation id, Ingredient ingredient, GristSet cost, Integer priority)
	{
		super(id, ingredient, priority);
		this.cost = cost.asImmutable();
	}
	
	@Override
	public GristSet getGristCost(ItemStack input, GristType wildcardType, boolean shouldRoundDown, Level level)
	{
		return scaleToCountAndDurability(cost, input, shouldRoundDown);
	}
	
	@Override
	public List<JeiGristCost> getJeiCosts(Level level)
	{
		return Collections.singletonList(new JeiGristCost.Set(ingredient, cost));
	}
	
	@Override
	public RecipeSerializer<?> getSerializer()
	{
		return MSRecipeTypes.GRIST_COST;
	}
	
	public static class Serializer extends AbstractSerializer<GristCost>
	{
		@Override
		protected GristCost read(ResourceLocation recipeId, JsonObject json, Ingredient ingredient, Integer priority)
		{
			GristSet cost = GristSet.deserialize(json.getAsJsonObject("grist_cost"));
			return new GristCost(recipeId, ingredient, cost, priority);
		}
		
		@Override
		protected GristCost read(ResourceLocation recipeId, FriendlyByteBuf buffer, Ingredient ingredient, int priority)
		{
			GristSet cost = GristSet.read(buffer);
			return new GristCost(recipeId, ingredient, cost, priority);
		}
		
		@Override
		public void toNetwork(FriendlyByteBuf buffer, GristCost recipe)
		{
			super.toNetwork(buffer, recipe);
			recipe.cost.write(buffer);
		}
	}
}