package com.mraof.minestuck.alchemy;

import java.util.Arrays;
import java.util.Hashtable;

import net.minecraft.item.ItemStack;
import net.minecraft.util.Tuple;

public class CombinationRegistry {
	private static Hashtable combRecipes = new Hashtable();
	
	/*
	 * Creates an entry for a result of combining the cards of two items. Used in the Punch Designex.
	 */
	public static void addCombination(ItemStack input1,ItemStack input2,ItemStack output) {
		combRecipes.put(Arrays.asList(input1.itemID,input1.getItemDamage(),input2.itemID,input2.getItemDamage()),output);
	}
	
	/*
	 * Returns an entry for a result of combining the cards of two items. Used in the Punch Designex.
	 */
	public static ItemStack getCombination(ItemStack input1,ItemStack input2) {
		return (ItemStack) combRecipes.get(Arrays.asList(input1.itemID,input1.getItemDamage(),input2.itemID,input2.getItemDamage()));
	}
}
