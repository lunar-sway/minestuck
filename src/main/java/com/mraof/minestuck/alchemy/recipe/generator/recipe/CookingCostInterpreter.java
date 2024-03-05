package com.mraof.minestuck.alchemy.recipe.generator.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.mraof.minestuck.api.alchemy.ImmutableGristSet;
import com.mraof.minestuck.api.alchemy.MutableGristSet;
import com.mraof.minestuck.api.alchemy.recipe.generator.GeneratorCallback;
import com.mraof.minestuck.api.alchemy.recipe.generator.LookupTracker;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.item.crafting.Recipe;

import java.util.List;

public record CookingCostInterpreter(ImmutableGristSet fuelCost) implements RecipeInterpreter
{
	public static final Codec<CookingCostInterpreter> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			ImmutableGristSet.MAP_CODEC.fieldOf("added_cost").forGetter(CookingCostInterpreter::fuelCost)
	).apply(instance, CookingCostInterpreter::new));
	
	private static final int STANDARD_COOKING_TIME = 200;
	
	@Override
	public List<Item> getOutputItems(Recipe<?> recipe)
	{
		return DefaultInterpreter.INSTANCE.getOutputItems(recipe);
	}
	
	@Override
	public MutableGristSet generateCost(Recipe<?> recipe, Item output, GeneratorCallback callback)
	{
		MutableGristSet cost = DefaultInterpreter.INSTANCE.generateCost(recipe, output, callback);
		
		if(cost != null && recipe instanceof AbstractCookingRecipe)
		{
			float cookTime = ((AbstractCookingRecipe) recipe).getCookingTime();
			cost.add(this.fuelCost.mutableCopy().scale(cookTime / STANDARD_COOKING_TIME, false));
		}
		
		return cost;
	}
	
	@Override
	public void reportPreliminaryLookups(Recipe<?> recipe, LookupTracker tracker)
	{
		DefaultInterpreter.INSTANCE.reportPreliminaryLookups(recipe, tracker);
	}
	
	@Override
	public Codec<? extends RecipeInterpreter> codec()
	{
		return CODEC;
	}
}