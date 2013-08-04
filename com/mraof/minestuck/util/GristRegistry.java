package com.mraof.minestuck.util;

import java.util.Arrays;
import java.util.Hashtable;

import net.minecraft.item.ItemStack;

public class GristRegistry {
	private static Hashtable gristRecipes = new Hashtable();
	
	/**
	 * Creates a item-grist conversion ratio for an ItemStack. Used in the Alchemiter and GristWidget.
	 */
	public static void addGristConversion(ItemStack item,GristSet grist) {
		gristRecipes.put(Arrays.asList(item.itemID,item.getItemDamage()), grist);
	}
	
	/**
	 * Returns a item-grist conversion ratio, given an ItemStack. Used in the Alchemiter and GristWidget.
	 */
	public static GristSet getGristConversion(ItemStack item) {
		return (GristSet) gristRecipes.get(Arrays.asList(item.itemID,item.getItemDamage()));
	}
}
