package com.mraof.minestuck.item.crafting.alchemy.generator;

import com.google.gson.JsonObject;
import com.mraof.minestuck.item.crafting.MSRecipeTypes;
import com.mraof.minestuck.item.crafting.alchemy.*;
import com.mraof.minestuck.jei.JeiGristCost;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Collections;
import java.util.List;
import java.util.function.Function;

public class ContainerGristCost extends GristCostRecipe
{
	//Note to self: Having a recipe that looks up and uses other recipes is kinda difficult in that there are often special cases where things can go wrong.
	// Perhaps an extension to the grist cost generator should be made that incorporates this kind of recipe.
	private static final Logger LOGGER = LogManager.getLogger();
	
	
	private final ImmutableGristSet addedCost;
	private ImmutableGristSet cachedCost;
	//Is false when it should use cachedCost, and true when it should calculate it from the container cost.
	// Solves the scenario of recursion from looking up the cost, by causing the recursion to terminate with a null cost returned.
	private boolean shouldFindCost = true;
	
	public ContainerGristCost(ResourceLocation id, Ingredient ingredient, GristSet addedCost, Integer priority)
	{
		super(id, ingredient, priority);
		this.addedCost = addedCost.asImmutable();
	}
	
	@Override
	public GristSet getGristCost(ItemStack input, GristType wildcardType, boolean shouldRoundDown, World world)
	{
		return scaleToCountAndDurability(findGristCost(world), input, shouldRoundDown);
	}
	
	@Override
	protected GristSet getLookupCost(ItemStack input, Function<ItemStack, GristSet> costLookup)
	{
		return scaleToCountAndDurability(findGristCost(costLookup), input, false);
	}
	
	@Override
	public List<JeiGristCost> getJeiCosts(World world)
	{
		GristSet cost = findGristCost(world);
		if(cost != null)
			return Collections.singletonList(new JeiGristCost.Set(ingredient, cost));
		else return Collections.emptyList();
	}
	
	private GristSet findGristCost(World world)
	{
		return findGristCost(stack -> GristCostRecipe.findCostForItem(stack, GristTypes.BUILD, false, world));
	}
	
	private GristSet findGristCost(Function<ItemStack, GristSet> costLookup)
	{
		if(shouldFindCost)
		{
			ItemStack container = ingredient.getMatchingStacks().length > 0 ? ingredient.getMatchingStacks()[0].getContainerItem() : ItemStack.EMPTY;
			if(!container.isEmpty())
			{
				shouldFindCost = false;
				GristSet cost = costLookup.apply(container);
				if(cost != null)
				{
					cachedCost = cost.copy().addGrist(addedCost).asImmutable();
				} else
				{
					LOGGER.warn("Got null grist cost when looking up container item {} for container grist cost {}. No grist cost is set for this recipe.", container.getItem().getRegistryName(), id);
					cachedCost = null;
				}
			} else
			{
				LOGGER.warn("No container item found for ingredient to container grist cost {}. Assuming that the container cost is zero.", id);
				cachedCost = addedCost;
			}
		}
		
		return cachedCost;
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