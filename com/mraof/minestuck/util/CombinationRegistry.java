package com.mraof.minestuck.util;

import java.util.Arrays;
import java.util.Hashtable;

import net.minecraft.item.ItemStack;

public class CombinationRegistry {
	private static Hashtable combRecipes = new Hashtable();
	
	/**
	 * Creates an entry for a result of combining the cards of two items. Used in the Punch Designex.
	 */
	public static void addCombination(ItemStack input1, ItemStack input2, boolean mode, ItemStack output) {
		addCombination(input1, input2, mode, true, true, output);
	}
	
	
	public static void addCombination(ItemStack input1, ItemStack input2, boolean mode, boolean useDamage1, boolean useDamage2, ItemStack output) {
		combRecipes.put(Arrays.asList(input1.itemID, useDamage1 ? input1.getItemDamage() : 0, input2.itemID, useDamage2 ? input2.getItemDamage() : 0, mode, useDamage1, useDamage2), output);
	}


	/**
	 * Returns an entry for a result of combining the cards of two items. Used in the Punch Designex.
	 */
	public static ItemStack getCombination(ItemStack input1, ItemStack input2, boolean mode) {
		Object temp;
		if (input1 == null || input2 == null) {return null;}
		if ((temp = combRecipes.get(Arrays.asList(input1.itemID, input1.getItemDamage(), input2.itemID, input2.getItemDamage(), mode, true, true))) != null);
		else if ((temp = combRecipes.get(Arrays.asList(input1.itemID, 0, input2.itemID, input2.getItemDamage(), mode, false, true))) != null);
		else if ((temp = combRecipes.get(Arrays.asList(input1.itemID, input1.getItemDamage(), input2.itemID, 0, mode, true, false))) != null);
		else if ((temp = combRecipes.get(Arrays.asList(input1.itemID, 0, input2.itemID, 0, mode, false, false))) != null);
		else if ((temp = combRecipes.get(Arrays.asList(input2.itemID, 0, input1.itemID, input1.getItemDamage(), mode, false, true))) != null);
		else if ((temp = combRecipes.get(Arrays.asList(input2.itemID, input2.getItemDamage(), input1.itemID, 0, mode, true, false))) != null);
		else if ((temp = combRecipes.get(Arrays.asList(input2.itemID, 0, input1.itemID, 0, mode, false, false))) != null);
		else temp = combRecipes.get(Arrays.asList(input1.itemID, input1.getItemDamage(), input2.itemID, input2.getItemDamage(), mode, true, true));
		return (ItemStack) temp;
	}
}
