package com.mraof.minestuck.alchemy.recipe.generator;

import com.google.gson.JsonObject;
import com.mojang.serialization.JsonOps;
import com.mraof.minestuck.alchemy.recipe.GeneratedGristCostCache;
import com.mraof.minestuck.api.alchemy.GristSet;
import com.mraof.minestuck.api.alchemy.ImmutableGristSet;
import com.mraof.minestuck.item.crafting.MSRecipeTypes;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class ContainerGristCost extends GeneratedGristCost
{
	private static final Logger LOGGER = LogManager.getLogger();
	
	private final ImmutableGristSet addedCost;
	
	public ContainerGristCost(ResourceLocation id, Ingredient ingredient, ImmutableGristSet addedCost, @Nullable Integer priority)
	{
		super(id, ingredient, priority);
		this.addedCost = addedCost;
	}
	
	private ContainerGristCost(ResourceLocation id, Ingredient ingredient, @Nullable Integer priority, GeneratedGristCostCache cache)
	{
		super(id, ingredient, priority, cache);
		this.addedCost = null;
	}
	
	@Override
	protected GristSet generateCost(GenerationContext context)
	{
		ItemStack container = ingredient.getItems().length > 0 ? ingredient.getItems()[0].getCraftingRemainingItem() : ItemStack.EMPTY;
		if(!container.isEmpty())
		{
			GristSet cost = context.lookupCostFor(container);
			if(cost != null)
			{
				return cost.mutableCopy().add(addedCost);
			} else
			{
				if(context.isPrimary())
					LOGGER.warn("Got null grist cost when looking up container item {} for container grist cost {}. No grist cost is set for this recipe.", ForgeRegistries.ITEMS.getKey(container.getItem()), id);
			}
		} else
		{
			if(context.isPrimary())
				LOGGER.warn("No container item found for ingredient to container grist cost {}. Assuming that the container cost is zero.", id);
			return addedCost;
		}
		
		return null;
	}
	
	@Override
	public RecipeSerializer<?> getSerializer()
	{
		return MSRecipeTypes.CONTAINER_GRIST_COST.get();
	}
	
	public static class Serializer extends GeneratedCostSerializer<ContainerGristCost>
	{
		@Override
		protected ContainerGristCost read(ResourceLocation recipeId, JsonObject json, Ingredient ingredient, Integer priority)
		{
			ImmutableGristSet cost = ImmutableGristSet.MAP_CODEC.parse(JsonOps.INSTANCE, GsonHelper.getAsJsonObject(json, "grist_cost"))
					.getOrThrow(false, LOGGER::error);
			return new ContainerGristCost(recipeId, ingredient, cost, priority);
		}
		
		@Override
		protected ContainerGristCost create(ResourceLocation recipeId, FriendlyByteBuf buffer, Ingredient ingredient, int priority, GeneratedGristCostCache cache)
		{
			return new ContainerGristCost(recipeId, ingredient, priority, cache);
		}
	}
}