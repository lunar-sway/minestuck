package com.mraof.minestuck.api.alchemy.recipe.combination;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

/**
 * A recipe object that takes away the complexity and open-endedness of minecraft recipes in favor
 * of the exposure needed for displaying the recipe. Also allows dynamic recipes or multi-recipes
 * to disguise themselves as multiple regular recipes if appropriate.
 */
public record JeiCombination(Ingredient input1, Ingredient input2, ItemStack output, CombinationMode mode)
{
	public JeiCombination
	{
		output = output.copy();
	}
}
