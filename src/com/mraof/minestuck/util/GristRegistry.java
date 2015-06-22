package com.mraof.minestuck.util;

import java.util.Arrays;
import java.util.Hashtable;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

public class GristRegistry {
	private static Hashtable<List<Object>, GristSet> gristRecipes = new Hashtable<List<Object>, GristSet>();
	
	/**
	 * Creates a item-grist conversion ratio for an ItemStack. Used in the Alchemiter and GristWidget.
	 */
	public static void addGristConversion(ItemStack item, GristSet grist) {
		addGristConversion(item, true, grist);
	}

	public static void addGristConversion(ItemStack item,boolean useDamage,GristSet grist) {
		//System.out.printf("adding grist conversion for id %d and metadata %d, %susing metadata\n", item.itemID, item.getItemDamage(), useDamage ? "" : "not ");
		gristRecipes.put(Arrays.asList(item.getItem(), useDamage ? item.getItemDamage() : OreDictionary.WILDCARD_VALUE), grist);
	}
	public static void addGristConversion(Block block, GristSet grist)
	{
		addGristConversion(block, OreDictionary.WILDCARD_VALUE, grist);
	}
	public static void addGristConversion(Block block, int metadata, GristSet grist)
	{
		gristRecipes.put(Arrays.asList(Item.getItemFromBlock(block), metadata), grist);
	}
	
	public static void addGristConversion(String name, GristSet grist)
	{
		gristRecipes.put(Arrays.asList((Object) name, OreDictionary.WILDCARD_VALUE), grist);
	}
	
	/**
	 * Returns a item-grist conversion ratio, given an ItemStack. Used in the Alchemiter and GristWidget.
	 */
	public static GristSet getGristConversion(ItemStack item)
	{
		GristSet grist;
		if(item == null || item.getItem() == null)
		{return null;}
		
		if((grist = gristRecipes.get(Arrays.asList(item.getItem(),item.getItemDamage()))) != null);
		else if((grist = gristRecipes.get(Arrays.asList(item.getItem(), OreDictionary.WILDCARD_VALUE))) != null);
		else
		{
			String[] names = CombinationRegistry.getDictionaryNames(item);
			for(String str : names)
				if((grist = gristRecipes.get(Arrays.asList(str, OreDictionary.WILDCARD_VALUE))) != null)
					break;
		}
		return grist == null ? null : grist.copy();
	}
	
	public static Hashtable<List<Object>, GristSet> getAllConversions() {
		return gristRecipes;
	}
}
