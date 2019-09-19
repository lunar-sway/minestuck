package com.mraof.minestuck.item.crafting.alchemy;

import java.util.Hashtable;

import javax.annotation.Nonnull;

import com.mraof.minestuck.util.Debug;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tags.Tag;
import net.minecraft.util.IItemProvider;

public class AlchemyCostRegistry
{
	private static Hashtable<Item, GristSet> costsByItem = new Hashtable<>();
	private static Hashtable<Tag<Item>, GristSet> costsByTag = new Hashtable<>();
	
	public static void addGristConversion(@Nonnull IItemProvider item, GristSet grist)
	{
		costsByItem.put(item.asItem(), grist);
	}
	
	public static void addGristConversion(Tag<Item> tag, GristSet grist)
	{
		costsByTag.put(tag, grist);
	}
	
	public static void removeGristConversion(@Nonnull ItemStack stack)
	{
		removeGristConversion(stack.getItem());
	}
	
	public static void removeGristConversion(@Nonnull Block block)
	{
		removeGristConversion(block.asItem());
	}
	
	public static void removeGristConversion(@Nonnull Item item)
	{
		if(costsByItem.remove(item) == null)
			Debug.warnf("Tried removing grist conversion for %s, but couldn't find grist conversion", item);
	}
	
	public static void removeGristConversion(Tag<Item> tag)
	{
		if(costsByTag.remove(tag) == null)
			Debug.warnf("Tried removing grist conversion for tag %s, but couldn't find grist conversion", tag);
	}
	/**
	 * Returns a item-grist conversion ratio, given an ItemStack. Used in the Alchemiter and GristWidget.
	 */
	public static GristSet getGristConversion(@Nonnull ItemStack stack)
	{
		if(stack.isEmpty())
		{return null;}
		return getGristConversion(stack.getItem());
	}
	
	public static GristSet getGristConversion(@Nonnull Item item)
	{
		GristSet grist;
		
		if((grist = costsByItem.get(item.getItem())) != null);
		else
		{
			
			/*String[] names = CombinationRegistry.getDictionaryNames(item);TODO How to get tags from items
			for(String str : names)
				if((grist = gristRecipes.get(Arrays.asList(str, OreDictionary.WILDCARD_VALUE))) != null)
					break;*/
		}
		return grist == null ? null : grist.copy();
	}
	
	public static Hashtable<Item, GristSet> getItemConversions()
	{
		return costsByItem;
	}
}
