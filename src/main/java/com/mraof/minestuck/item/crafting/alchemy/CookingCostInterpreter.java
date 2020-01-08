package com.mraof.minestuck.item.crafting.alchemy;

import com.google.gson.JsonElement;
import net.minecraft.item.Item;
import net.minecraft.item.crafting.AbstractCookingRecipe;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.JSONUtils;
import net.minecraftforge.registries.ObjectHolder;

import java.util.function.Function;

public class CookingCostInterpreter extends DefaultInterpreter
{
	private static final int standardCookingTime = 200;
	
	@ObjectHolder("minestuck:cooking")
	public static final InterpreterSerializer<CookingCostInterpreter> SERIALIZER = null;
	
	private final GristSet fuelCost;
	
	public CookingCostInterpreter(GristSet fuelCost)
	{
		this.fuelCost = fuelCost;
	}
	
	@Override
	public GristSet generateCost(IRecipe<?> recipe, Item item, Function<Ingredient, GristSet> ingredientInterpreter)
	{
		GristSet cost = super.generateCost(recipe, item, ingredientInterpreter);
		
		if(cost != null && recipe instanceof AbstractCookingRecipe)
		{
			float cookTime = ((AbstractCookingRecipe) recipe).getCookTime();
			cost.addGrist(fuelCost.copy().scale(cookTime/standardCookingTime, false));
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
			GristSet cost = GristSet.deserialize(JSONUtils.getJsonObject(json, "grist cost"));
			return new CookingCostInterpreter(cost);
		}
		
		@Override
		public JsonElement write(CookingCostInterpreter interpreter)
		{
			return interpreter.fuelCost.serialize();
		}
	}
}