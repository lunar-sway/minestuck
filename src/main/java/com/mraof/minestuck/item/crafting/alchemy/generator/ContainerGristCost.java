package com.mraof.minestuck.item.crafting.alchemy.generator;

import com.google.gson.JsonObject;
import com.mraof.minestuck.item.crafting.MSRecipeTypes;
import com.mraof.minestuck.item.crafting.alchemy.GristSet;
import com.mraof.minestuck.item.crafting.alchemy.ImmutableGristSet;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
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
		ItemStack container = ingredient.getItems().length > 0 ? ingredient.getItems()[0].getContainerItem() : ItemStack.EMPTY;
		if(!container.isEmpty())
		{
			GristSet cost = context.lookupCostFor(container);
			if(cost != null)
			{
				return cost.copy().addGrist(addedCost);
			} else
			{
				if(context.isPrimary())
					LOGGER.warn("Got null grist cost when looking up container item {} for container grist cost {}. No grist cost is set for this recipe.", container.getItem().getRegistryName(), id);
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
		return MSRecipeTypes.CONTAINER_GRIST_COST;
	}
	
	public static class Serializer extends GeneratedCostSerializer<ContainerGristCost>
	{
		@Override
		protected ContainerGristCost read(ResourceLocation recipeId, JsonObject json, Ingredient ingredient, Integer priority)
		{
			GristSet cost = GristSet.deserialize(GsonHelper.getAsJsonObject(json, "grist_cost"));
			return new ContainerGristCost(recipeId, ingredient, cost, priority);
		}
		
		@Override
		protected ContainerGristCost create(ResourceLocation recipeId, FriendlyByteBuf buffer, Ingredient ingredient, int priority, GristSet cost)
		{
			return new ContainerGristCost(recipeId, ingredient, priority, cost);
		}
	}
}