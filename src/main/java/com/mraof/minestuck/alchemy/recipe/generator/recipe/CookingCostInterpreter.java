package com.mraof.minestuck.alchemy.recipe.generator.recipe;

import com.google.gson.JsonElement;
import com.mojang.serialization.JsonOps;
import com.mraof.minestuck.api.alchemy.ImmutableGristSet;
import com.mraof.minestuck.api.alchemy.MutableGristSet;
import com.mraof.minestuck.api.alchemy.recipe.generator.GeneratorCallback;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.item.crafting.Recipe;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CookingCostInterpreter extends DefaultInterpreter
{
	private static final Logger LOGGER = LogManager.getLogger();
	private static final int STANDARD_COOKING_TIME = 200;
	
	private final ImmutableGristSet fuelCost;
	
	public CookingCostInterpreter(ImmutableGristSet fuelCost)
	{
		this.fuelCost = fuelCost;
	}
	
	@Override
	public MutableGristSet generateCost(Recipe<?> recipe, Item output, GeneratorCallback callback)
	{
		MutableGristSet cost = super.generateCost(recipe, output, callback);
		
		if(cost != null && recipe instanceof AbstractCookingRecipe)
		{
			float cookTime = ((AbstractCookingRecipe) recipe).getCookingTime();
			cost.add(fuelCost.mutableCopy().scale(cookTime / STANDARD_COOKING_TIME, false));
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
			ImmutableGristSet cost = ImmutableGristSet.MAP_CODEC.parse(JsonOps.INSTANCE, json).getOrThrow(false, LOGGER::error);
			return new CookingCostInterpreter(cost);
		}
		
		@Override
		public JsonElement write(CookingCostInterpreter interpreter)
		{
			return ImmutableGristSet.MAP_CODEC.encodeStart(JsonOps.INSTANCE, interpreter.fuelCost).getOrThrow(false, LOGGER::error);
		}
	}
}