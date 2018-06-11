package com.mraof.minestuck.util;

import java.util.Arrays;
import java.util.Hashtable;
import java.util.List;

import javax.annotation.Nonnull;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

public class GristRegistry {
	private static Hashtable<List<Object>, GristSet> gristRecipes = new Hashtable<>();
	
	/**
	 * Creates a item-grist conversion ratio for an ItemStack. Used in the Alchemiter and GristWidget.
	 */
	public static void addGristConversion(@Nonnull ItemStack item, GristSet grist)
	{
		addGristConversion(item, !item.getItem().isDamageable(), grist);
	}

	public static void addGristConversion(@Nonnull ItemStack item, boolean useDamage, GristSet grist)
	{
		if(useDamage && item.getItem().isDamageable())
			Debug.warnf("Item %s appears to be using damage value in a grist recipe. This might not be intended.", item);
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
		gristRecipes.put(Arrays.asList(name, OreDictionary.WILDCARD_VALUE), grist);
	}
	
	public static void removeGristConversion(@Nonnull ItemStack stack)
	{
		removeGristConversion(stack, stack.getItem().isDamageable());
	}
	
	public static void removeGristConversion(@Nonnull ItemStack stack, boolean useDamage)
	{
		if(gristRecipes.remove(Arrays.asList(stack.getItem(), useDamage ? stack.getItemDamage() : OreDictionary.WILDCARD_VALUE)) == null)
			Debug.warnf("Tried removing grist conversion for %s, but couldn't find grist conversion", stack);
	}
	
	public static void removeGristConversion(String name)
	{
		if(gristRecipes.remove(Arrays.asList(name, OreDictionary.WILDCARD_VALUE)) == null)
			Debug.warnf("Tried removing grist conversion for oredict %s, but couldn't find grist conversion", name);
	}
	/**
	 * Returns a item-grist conversion ratio, given an ItemStack. Used in the Alchemiter and GristWidget.
	 */
	public static GristSet getGristConversion(@Nonnull ItemStack item)
	{
		GristSet grist;
		if(item.isEmpty())
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
