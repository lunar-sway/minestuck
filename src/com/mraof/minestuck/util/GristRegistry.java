package com.mraof.minestuck.util;

import java.util.Arrays;
import java.util.Hashtable;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;

public class GristRegistry {
	private static Hashtable<List<Object>, GristSet> gristRecipes = new Hashtable<List<Object>, GristSet>();
	
	/**
	 * Creates a item-grist conversion ratio for an ItemStack. Used in the Alchemiter and GristWidget.
	 */
	public static void addGristConversion(ItemStack item,GristSet grist) {
		addGristConversion(item,true,grist);
	}

	public static void addGristConversion(ItemStack item,boolean useDamage,GristSet grist) {
		//System.out.printf("adding grist conversion for id %d and metadata %d, %susing metadata\n", item.itemID, item.getItemDamage(), useDamage ? "" : "not ");
		gristRecipes.put(Arrays.asList(item.getItem(), useDamage ? item.getItemDamage() : 0,useDamage), grist);
	}
	public static void addGristConversion(Block block, GristSet grist)
	{
		addGristConversion(block, 0, false, grist);
	}
	public static void addGristConversion(Block block, int metadata, boolean useDamage, GristSet grist)
	{
		gristRecipes.put(Arrays.asList(block, useDamage ? metadata : 0, useDamage), grist);
	}
	
	/**
	 * Returns a item-grist conversion ratio, given an ItemStack. Used in the Alchemiter and GristWidget.
	 */
	public static GristSet getGristConversion(ItemStack item) {
		if (item == null) {return null;}
		if (gristRecipes.get(Arrays.asList(item.getItem(),item.getItemDamage(),true)) == null) {
			return (GristSet) gristRecipes.get(Arrays.asList(item.getItem(),0,false));
		} else {
			return (GristSet) gristRecipes.get(Arrays.asList(item.getItem(),item.getItemDamage(),true));
		}
	}
	
	public static Hashtable<List<Object>, GristSet> getAllConversions() {
		return gristRecipes;
	}
}
