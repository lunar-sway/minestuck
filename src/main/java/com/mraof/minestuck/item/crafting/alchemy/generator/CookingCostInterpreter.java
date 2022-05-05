package com.mraof.minestuck.item.crafting.alchemy.generator;

import com.google.gson.JsonElement;
import com.mraof.minestuck.item.crafting.alchemy.GristSet;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraftforge.registries.ObjectHolder;

public class CookingCostInterpreter extends DefaultInterpreter
{
	private static final int STANDARD_COOKING_TIME = 200;
	
	@ObjectHolder("minestuck:cooking")
	public static final InterpreterSerializer<CookingCostInterpreter> SERIALIZER = null;
	
	private final GristSet fuelCost;
	
	public CookingCostInterpreter(GristSet fuelCost)
	{
		this.fuelCost = fuelCost;
	}
	
	@Override
	public GristSet generateCost(Recipe<?> recipe, Item output, GenerationContext context)
	{
		GristSet cost = super.generateCost(recipe, output, context);
		
		if(cost != null && recipe instanceof AbstractCookingRecipe)
		{
			float cookTime = ((AbstractCookingRecipe) recipe).getCookingTime();
			cost.addGrist(fuelCost.copy().scale(cookTime / STANDARD_COOKING_TIME, false));
		}
		
		return cost;
	}
	
	@Override
	public InterpreterSerializer<?> getSerializer()
	{
		return SERIALIZER;
	}
	
	public static class Serializer extends InterpreterSerializer<CookingCostInterpreter>
	{
		@Override
		public CookingCostInterpreter read(JsonElement json)
		{
			GristSet cost = GristSet.deserialize(GsonHelper.convertToJsonObject(json, "grist cost"));
			return new CookingCostInterpreter(cost);
		}
		
		@Override
		public JsonElement write(CookingCostInterpreter interpreter)
		{
			return interpreter.fuelCost.serialize();
		}
	}
}