package com.mraof.minestuck.jei;

import com.mraof.minestuck.api.alchemy.ImmutableGristSet;
import net.minecraft.world.item.crafting.Ingredient;

public sealed interface JeiGristCost permits JeiGristCost.Set, JeiGristCost.Wildcard
{
	String GRIST_COSTS = "minestuck.jei.grist_costs";
	
	Ingredient ingredient();
	
	record Set(Ingredient ingredient, ImmutableGristSet gristSet) implements JeiGristCost
	{}
	
	record Wildcard(Ingredient ingredient, long wildcardAmount) implements JeiGristCost
	{}
}
