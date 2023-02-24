package com.mraof.minestuck.alchemy.generator.recipe;

import com.google.gson.JsonElement;
import com.mraof.minestuck.alchemy.GristSet;
import com.mraof.minestuck.alchemy.generator.GenerationContext;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.item.crafting.Recipe;

public class CookingCostInterpreter extends DefaultInterpreter
{
	private static final int STANDARD_COOKING_TIME = 200;
	
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
		return InterpreterSerializers.COOKING.get();
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