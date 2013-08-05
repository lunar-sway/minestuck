package com.mraof.minestuck.util;

import java.util.Arrays;
import java.util.Hashtable;

import net.minecraft.item.ItemStack;
import net.minecraft.util.Tuple;

public class CombinationRegistry {
	private static Hashtable combRecipes = new Hashtable();
	
	/*
	 * Creates an entry for a result of combining the cards of two items. Used in the Punch Designex.
	 */
	public static void addCombination(ItemStack input1,ItemStack input2,CombinationMode mode,ItemStack output) {
		combRecipes.put(Arrays.asList(input1.itemID,input1.getItemDamage(),input2.itemID,input2.getItemDamage(),mode),output);
	}
	
	/*
	 * Returns an entry for a result of combining the cards of two items. Used in the Punch Designex.
	 */
	public static ItemStack getCombination(ItemStack input1,ItemStack input2,CombinationMode mode) {
		Object temp = combRecipes.get(Arrays.asList(input1.itemID,input1.getItemDamage(),input2.itemID,input2.getItemDamage(),mode));
		if (temp == null) {
			return (ItemStack) combRecipes.get(Arrays.asList(input2.itemID,input2.getItemDamage(),input1.itemID,input1.getItemDamage(),mode));
		} else {
			return (ItemStack) temp;
		}
	}
}
