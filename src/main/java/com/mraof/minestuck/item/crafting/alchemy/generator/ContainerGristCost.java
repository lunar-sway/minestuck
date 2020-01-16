package com.mraof.minestuck.item.crafting.alchemy.generator;

import com.google.gson.JsonObject;
import com.mraof.minestuck.item.crafting.MSRecipeTypes;
import com.mraof.minestuck.item.crafting.alchemy.GristSet;
import com.mraof.minestuck.item.crafting.alchemy.ImmutableGristSet;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;

public class ContainerGristCost extends GeneratedGristCost
{
	//Note to self: Having a recipe that looks up and uses other recipes is kinda difficult in that there are often special cases where things can go wrong.
	// Perhaps an extension to the grist cost generator should be made that incorporates this kind of recipe.
	private static final Logger LOGGER = LogManager.getLogger();
	
	private final ImmutableGristSet addedCost;
	
	public ContainerGristCost(ResourceLocation id, Ingredient ingredient, GristSet addedCost, @Nullable Integer priority)
	{
		super(id, ingredient, priority);
		this.addedCost = addedCost.asImmutable();
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
	
	public static class Serializer extends AbstractSerializer<ContainerGristCost>
	{
		@Override
		protected ContainerGristCost read(ResourceLocation recipeId, JsonObject json, Ingredient ingredient, Integer priority)
		{
			GristSet cost = GristSet.deserialize(json.getAsJsonObject("grist_cost"));
			return new ContainerGristCost(recipeId, ingredient, cost, priority);
		}
		
		@Override
		protected ContainerGristCost read(ResourceLocation recipeId, PacketBuffer buffer, Ingredient ingredient, int priority)
		{
			GristSet cost = GristSet.read(buffer);
			return new ContainerGristCost(recipeId, ingredient, cost, priority);
		}
		
		@Override
		public void write(PacketBuffer buffer, ContainerGristCost recipe)
		{
			super.write(buffer, recipe);
			recipe.addedCost.write(buffer);
		}
	}
}