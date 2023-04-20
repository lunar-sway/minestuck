package com.mraof.minestuck.alchemy.recipe;

import com.google.gson.JsonObject;
import com.mojang.serialization.JsonOps;
import com.mraof.minestuck.alchemy.MutableGristSet;
import com.mraof.minestuck.alchemy.GristType;
import com.mraof.minestuck.alchemy.GristSet;
import com.mraof.minestuck.alchemy.ImmutableGristSet;
import com.mraof.minestuck.item.crafting.MSRecipeTypes;
import com.mraof.minestuck.jei.JeiGristCost;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Collections;
import java.util.List;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class GristCost extends GristCostRecipe
{
	private static final Logger LOGGER = LogManager.getLogger();
	
	private final ImmutableGristSet cost;
	
	public GristCost(ResourceLocation id, Ingredient ingredient, GristSet cost, @Nullable Integer priority)
	{
		super(id, ingredient, priority);
		this.cost = cost.asImmutable();
	}
	
	@Override
	public GristSet getGristCost(ItemStack input, @Nullable GristType wildcardType, boolean shouldRoundDown, @Nullable Level level)
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
		return MSRecipeTypes.GRIST_COST.get();
	}
	
	public static class Serializer extends AbstractSerializer<GristCost>
	{
		@Override
		protected GristCost read(ResourceLocation recipeId, JsonObject json, Ingredient ingredient, @Nullable Integer priority)
		{
			GristSet cost = ImmutableGristSet.MAP_CODEC.parse(JsonOps.INSTANCE, json.getAsJsonObject("grist_cost")).getOrThrow(false, LOGGER::error);
			return new GristCost(recipeId, ingredient, cost, priority);
		}
		
		@Override
		protected GristCost read(ResourceLocation recipeId, FriendlyByteBuf buffer, Ingredient ingredient, int priority)
		{
			GristSet cost = MutableGristSet.read(buffer);
			return new GristCost(recipeId, ingredient, cost, priority);
		}
		
		@Override
		public void toNetwork(FriendlyByteBuf buffer, GristCost recipe)
		{
			super.toNetwork(buffer, recipe);
			MutableGristSet.write(recipe.cost, buffer);
		}
	}
}