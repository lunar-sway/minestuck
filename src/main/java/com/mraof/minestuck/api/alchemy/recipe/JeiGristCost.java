package com.mraof.minestuck.api.alchemy.recipe;

import com.mraof.minestuck.api.alchemy.ImmutableGristSet;
import net.minecraft.FieldsAreNonnullByDefault;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.Objects;

@FieldsAreNonnullByDefault
public sealed interface JeiGristCost permits JeiGristCost.Set, JeiGristCost.Wildcard
{
	String GRIST_COSTS = "minestuck.jei.grist_costs";
	
	Ingredient ingredient();
	
	record Set(Ingredient ingredient, ImmutableGristSet gristSet) implements JeiGristCost
	{
		public Set
		{
			Objects.requireNonNull(ingredient);
			Objects.requireNonNull(gristSet);
		}
	}
	
	record Wildcard(Ingredient ingredient, long wildcardAmount) implements JeiGristCost
	{
		public Wildcard
		{
			Objects.requireNonNull(ingredient);
		}
	}
}
