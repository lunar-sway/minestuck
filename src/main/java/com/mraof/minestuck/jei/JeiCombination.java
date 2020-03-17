package com.mraof.minestuck.jei;

import com.mraof.minestuck.item.crafting.alchemy.CombinationMode;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;

/**
 * A recipe object that takes away the complexity and open-endedness of minecraft recipes in favor
 * of the exposure needed for displaying the recipe. Also allows dynamic recipes or multi-recipes
 * to disguise themselves as multiple regular recipes if appropriate.
 */
public class JeiCombination
{
	private final Ingredient input1, input2;
	private final ItemStack output;
	private final CombinationMode mode;
	
	public JeiCombination(Ingredient input1, Ingredient input2, ItemStack output, CombinationMode mode)
	{
		this.input1 = input1;
		this.input2 = input2;
		this.output = output.copy();
		this.mode = mode;
	}
	
	public Ingredient getInput1()
	{
		return input1;
	}
	
	public Ingredient getInput2()
	{
		return input2;
	}
	
	public ItemStack getOutput()
	{
		return output;
	}
	
	public CombinationMode getMode()
	{
		return mode;
	}
}