package com.mraof.minestuck.item.crafting.alchemy.generator;

import com.google.gson.JsonObject;
import com.mraof.minestuck.item.crafting.MSRecipeTypes;
import com.mraof.minestuck.item.crafting.alchemy.GristSet;
import com.mraof.minestuck.item.crafting.alchemy.ImmutableGristSet;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;

public class ContainerGristCost extends GeneratedGristCost
{
	private static final Logger LOGGER = LogManager.getLogger();
	
	private final ImmutableGristSet addedCost;
	
	public ContainerGristCost(ResourceLocation id, Ingredient ingredient, GristSet addedCost, @Nullable Integer priority)
	{
		super(id, ingredient, priority);
		this.addedCost = addedCost.asImmutable();
	}
	
	private ContainerGristCost(ResourceLocation id, Ingredient ingredient, @Nullable Integer priority, GristSet cost)
	{
		super(id, ingredient, priority, cost);
		this.addedCost = null;
	}
	
	@Override
	protected GristSet generateCost(GenerationContext context)
	{
		ItemStack container = ingredient.getMatchingStacks().length > 0 ? ingredient.getMatchingStacks()[0].getContainerItem() : ItemStack.EMPTY;
		if(!container.isEmpty())
		{
			GristSet cost = context.lookupCostFor(container);
			if(cost != null)
			{
				return cost.copy().addGrist(addedCost);
			} else
			{
				LOGGER.warn("Got null grist cost when looking up container item {} for container grist cost {}. No grist cost is set for this recipe.", container.getItem().getRegistryName(), id);
			}
		} else
		{
			LOGGER.warn("No container item found for ingredient to container grist cost {}. Assuming that the container cost is zero.", id);
			return addedCost;
		}
		
		return null;
	}
	
	@Override
	public IRecipeSerializer<?> getSerializer()
	{
		return MSRecipeTypes.CONTAINER_GRIST_COST;
	}
	
	public static class Serializer extends GeneratedCostSerializer<ContainerGristCost>
	{
		@Override
		protected ContainerGristCost read(ResourceLocation recipeId, JsonObject json, Ingredient ingredient, Integer priority)
		{
			GristSet cost = GristSet.deserialize(JSONUtils.getJsonObject(json, "grist_cost"));
			return new ContainerGristCost(recipeId, ingredient, cost, priority);
		}
		
		@Override
		protected ContainerGristCost create(ResourceLocation recipeId, PacketBuffer buffer, Ingredient ingredient, int priority, GristSet cost)
		{
			return new ContainerGristCost(recipeId, ingredient, priority, cost);
		}
	}
}