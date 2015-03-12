package com.mraof.minestuck.util;

import java.util.Arrays;
import java.util.Hashtable;
import java.util.List;

import com.mraof.minestuck.Minestuck;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

public class CombinationRegistry {
	private static Hashtable<List<Object>, ItemStack> combRecipes = new Hashtable<List<Object>, ItemStack>();
	public static final boolean MODE_AND  = true;
	public static final boolean MODE_OR = false;
	
	/**
	 * Creates an entry for a result of combining the cards of two items. Used in the Punch Designix.
	 */
	public static void addCombination(ItemStack input1, ItemStack input2, boolean mode, ItemStack output) {
		addCombination(input1, input2, mode, true, true, output);
	}
	
	
	public static void addCombination(ItemStack input1, ItemStack input2, boolean mode, boolean useDamage1, boolean useDamage2, ItemStack output) {
		addCombination(input1.getItem(), useDamage1 ? input1.getItemDamage() : OreDictionary.WILDCARD_VALUE, input2.getItem(), useDamage2 ? input2.getItemDamage() : OreDictionary.WILDCARD_VALUE, mode, output);
	}
	
	public static void addCombination(String oreDictInput, ItemStack itemInput, boolean useDamage, boolean mode, ItemStack output)
	{
		addCombination(oreDictInput, itemInput.getItem(), useDamage ? itemInput.getItemDamage() : OreDictionary.WILDCARD_VALUE, mode, output);
	}
	
	public static void addCombination(String oreDictInput, Item item, int damage, boolean mode, ItemStack output)
	{
		addCombination(oreDictInput, OreDictionary.WILDCARD_VALUE, item, damage, mode, output);
	}
	
	public static void addCombination(String input1, String input2, boolean mode, ItemStack output)
	{
		addCombination(input1, OreDictionary.WILDCARD_VALUE, input2, OreDictionary.WILDCARD_VALUE, mode, output);
	}
	
	private static void addCombination(Object input1, int damage1, Object input2, int damage2, boolean mode, ItemStack output)
	{
		int index = input1.hashCode() - input2.hashCode();
		if(index == 0)
			index = damage1 - damage2;
		if(index > 0)
			combRecipes.put(Arrays.asList(input1, damage1, input2, damage2, mode), output);
		else combRecipes.put(Arrays.asList(input2, damage2, input1, damage1, mode), output);
	}
	
	/**
	 * Returns an entry for a result of combining the cards of two items. Used in the Punch Designix.
	 */
	public static ItemStack getCombination(ItemStack input1, ItemStack input2, boolean mode) {
		ItemStack item;
		if (input1 == null || input2 == null) {return null;}
		
		if((item = getCombination(input1.getItem(), input1.getItemDamage(), input2.getItem(), input2.getItemDamage(), mode)) == null)
		{
			String[] itemNames2 = getDictionaryNames(input2);
			
			for(String str2 : itemNames2)
				if((item = getCombination(input1.getItem(), input1.getItemDamage(), str2, OreDictionary.WILDCARD_VALUE, mode)) != null)
					return item;
			
			String[] itemNames1 = getDictionaryNames(input1);
			for(String str1 : itemNames1)
				if((item = getCombination(str1, OreDictionary.WILDCARD_VALUE, input2.getItem(), input2.getItemDamage(), mode)) != null)
					return item;
			
			for(String str1 : itemNames1)
				for(String str2 : itemNames2)
					if((item = getCombination(str1, OreDictionary.WILDCARD_VALUE, str2, OreDictionary.WILDCARD_VALUE, mode)) != null)
						return item;
		}
		
		if(item == null)
			if(input1.getItem().equals(Minestuck.blockStorage) && input1.getItemDamage() == 1)
				return mode?input1:input2;
			else if(input2.getItem().equals(Minestuck.blockStorage) && input2.getItemDamage() == 1)
				return mode?input2:input1;
		return item;
	}
	
	private static ItemStack getCombination(Object input1, int damage1, Object input2, int damage2, boolean mode)
	{
		ItemStack item;
		boolean b1 = damage1 == OreDictionary.WILDCARD_VALUE, b2 = damage1 == OreDictionary.WILDCARD_VALUE;
		
		int index = input1.hashCode() - input2.hashCode();
		if(index == 0)
			index = damage1 - damage2;
		if(index > 0)
		{
			if((item = combRecipes.get(Arrays.asList(input1, damage1, input2, damage2, mode))) != null);
			else if(b2 && (item = combRecipes.get(Arrays.asList(input1, damage1, input2, OreDictionary.WILDCARD_VALUE, mode))) != null);
			else if(b1 && (item = combRecipes.get(Arrays.asList(input1, OreDictionary.WILDCARD_VALUE, input2, damage2, mode))) != null);
			else if(b1 && b2) item = combRecipes.get(Arrays.asList(input1, OreDictionary.WILDCARD_VALUE, input2, OreDictionary.WILDCARD_VALUE, mode));
		}
		else
		{
			if((item = combRecipes.get(Arrays.asList(input2, damage2, input1, damage1, mode))) != null);
			else if(b2 && (item = combRecipes.get(Arrays.asList(input2, OreDictionary.WILDCARD_VALUE, input1, damage1, mode))) != null);
			else if(b1 && (item = combRecipes.get(Arrays.asList(input2, damage2, input1, OreDictionary.WILDCARD_VALUE, mode))) != null);
			else if(b1 && b2) item = combRecipes.get(Arrays.asList(input2, OreDictionary.WILDCARD_VALUE, input1, OreDictionary.WILDCARD_VALUE, mode));
		}
		
		return item;
	}
	
	protected static String[] getDictionaryNames(ItemStack stack)
	{
		int[] itemIDs = OreDictionary.getOreIDs(stack);
		String[] itemNames = new String[itemIDs.length];
		for(int i = 0; i < itemIDs.length; i++)
			itemNames[i] = OreDictionary.getOreName(itemIDs[i]);
		return itemNames;
	}
	
	public static Hashtable<List<Object>, ItemStack> getAllConversions() {
		return combRecipes;
	}
}
