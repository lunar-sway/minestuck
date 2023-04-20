package com.mraof.minestuck.alchemy.recipe.generator.recipe;

import com.google.gson.JsonElement;
import com.mojang.serialization.JsonOps;
import com.mraof.minestuck.alchemy.MutableGristSet;
import com.mraof.minestuck.alchemy.IGristSet;
import com.mraof.minestuck.alchemy.IImmutableGristSet;
import com.mraof.minestuck.alchemy.recipe.generator.GenerationContext;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.item.crafting.Recipe;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CookingCostInterpreter extends DefaultInterpreter
{
	private static final Logger LOGGER = LogManager.getLogger();
	private static final int STANDARD_COOKING_TIME = 200;
	
	private final IImmutableGristSet fuelCost;
	
	public CookingCostInterpreter(IGristSet fuelCost)
	{
		this.fuelCost = fuelCost.asImmutable();
	}
	
	@Override
	public MutableGristSet generateCost(Recipe<?> recipe, Item output, GenerationContext context)
	{
		MutableGristSet cost = super.generateCost(recipe, output, context);
		
		if(cost != null && recipe instanceof AbstractCookingRecipe)
		{
			float cookTime = ((AbstractCookingRecipe) recipe).getCookingTime();
			cost.addGrist(fuelCost.mutableCopy().scale(cookTime / STANDARD_COOKING_TIME, false));
		}
		
		return cost;
	}
	
	@Override
	public InterpreterSerializer<?> getSerializer()
	{
		return InterpreterSerializers.COOKING.get();
	}
	
	public static class Serializer extends InterpreterSerializer<CookingCostInterpreter>
	{
		@Override
		public CookingCostInterpreter read(JsonElement json)
		{
			IGristSet cost = IImmutableGristSet.MAP_CODEC.parse(JsonOps.INSTANCE, json).getOrThrow(false, LOGGER::error);
			return new CookingCostInterpreter(cost);
		}
		
		@Override
		public JsonElement write(CookingCostInterpreter interpreter)
		{
			return IImmutableGristSet.MAP_CODEC.encodeStart(JsonOps.INSTANCE, interpreter.fuelCost).getOrThrow(false, LOGGER::error);
		}
	}
}