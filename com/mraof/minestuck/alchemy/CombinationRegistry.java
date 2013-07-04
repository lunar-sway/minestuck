package com.mraof.minestuck.alchemy;

import java.util.Hashtable;

import net.minecraft.item.ItemStack;
import net.minecraft.util.Tuple;

public class CombinationRegistry {
	private static Hashtable combRecipes = new Hashtable();
	
	/*
	 * Creates an entry for a result of combining the cards of two items. Used in the Punch Designex.
	 */
	public static void addCombination(ItemStack input1,ItemStack input2,ItemStack output) {
		combRecipes.put(new ItemStack[] {input1,input2},output);
	}
	
	/*
	 * Returns an entry for a result of combining the cards of two items. Used in the Punch Designex.
	 */
	public static GristSet getCombination(ItemStack input1,ItemStack input2) {
		return (GristSet) combRecipes.get(new ItemStack[] {input1,input2});
	}
}
