package com.mraof.minestuck.alchemy;

import java.util.Arrays;
import java.util.Hashtable;
import java.util.List;

import javax.annotation.Nonnull;

import com.mraof.minestuck.block.MinestuckBlocks;

import com.mraof.minestuck.event.AlchemyCombinationEvent;
import com.mraof.minestuck.util.Debug;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.oredict.OreDictionary;

public class CombinationRegistry {
	private static Hashtable<List<Object>, ItemStack> combRecipes = new Hashtable<List<Object>, ItemStack>();
	
	public enum Mode
	{
		MODE_AND("&&"),
		MODE_OR("||");
		
		String str;
		
		Mode(String str)
		{
			this.str = str;
		}
		
		public String getStr()
		{
			return str;
		}
		
		public boolean asBool()
		{
			return this == MODE_AND;
		}
	}
	
	/**
	 * Creates an entry for a result of combining the cards of two items. Used in the Punch Designix.
	 */
	public static void addCombination(@Nonnull ItemStack input1, @Nonnull ItemStack input2, Mode mode, @Nonnull ItemStack output) {
		addCombination(input1, input2, mode, !input1.getItem().isDamageable(), !input2.getItem().isDamageable(), output);
	}
	
	
	public static void addCombination(@Nonnull ItemStack input1, @Nonnull ItemStack input2, Mode mode, boolean useDamage1, boolean useDamage2, @Nonnull ItemStack output)
	{
		if(useDamage1 && input1.getItem().isDamageable())
			Debug.warnf("Item %s in a recipe for %s appears to be using damage value. This might not be intended.", input1, output);
		if(useDamage2 && input2.getItem().isDamageable())
			Debug.warnf("Item %s in a recipe for %s appears to be using damage value. This might not be intended.", input2, output);
		addCombination(input1.getItem(), useDamage1 ? input1.getItemDamage() : OreDictionary.WILDCARD_VALUE, input2.getItem(), useDamage2 ? input2.getItemDamage() : OreDictionary.WILDCARD_VALUE, mode, output);
	}
	
	public static void addCombination(String oreDictInput, @Nonnull ItemStack itemInput, boolean useDamage, Mode mode, @Nonnull ItemStack output)
	{
		addCombination(oreDictInput, itemInput.getItem(), useDamage ? itemInput.getItemDamage() : OreDictionary.WILDCARD_VALUE, mode, output);
	}
	
	public static void addCombination(String oreDictInput, Item item, int damage, Mode mode, @Nonnull ItemStack output)
	{
		addCombination(oreDictInput, OreDictionary.WILDCARD_VALUE, item, damage, mode, output);
	}
	
	public static void addCombination(String oreDictInput, Block block, int damage, Mode mode, @Nonnull ItemStack output)
	{
		addCombination(oreDictInput, OreDictionary.WILDCARD_VALUE, Item.getItemFromBlock(block), damage, mode, output);
	}
	
	public static void addCombination(String input1, String input2, Mode mode, @Nonnull ItemStack output)
	{
		addCombination(input1, OreDictionary.WILDCARD_VALUE, input2, OreDictionary.WILDCARD_VALUE, mode, output);
	}
	
	private static void addCombination(Object input1, int damage1, Object input2, int damage2, Mode mode, @Nonnull ItemStack output)
	{
		try
		{
			checkIsValid(input1);
			checkIsValid(input2);
			if(output.isEmpty())
				throw new IllegalArgumentException("Output is not defined.");
		} catch(IllegalArgumentException e)
		{
			Debug.warnf("[Minestuck] An argument for a combination recipe was found invalid. Reason: "+e.getMessage());
			Debug.warnf("[Minestuck] The recipe in question: %s %s %s -> %s", input1 instanceof Item ? ((Item) input1).getUnlocalizedName() : input1, mode.getStr(), input2 instanceof Item ? ((Item) input2).getUnlocalizedName() : input2, output == null || output.getItem() == null ? null : output);
			return;
		}
		
		int index = input1.hashCode() - input2.hashCode();
		if(index == 0)
			index = damage1 - damage2;
		if(index > 0)
			combRecipes.put(Arrays.asList(input1, damage1, input2, damage2, mode), output);
		else combRecipes.put(Arrays.asList(input2, damage2, input1, damage1, mode), output);
	}
	
	private static void checkIsValid(Object input) throws IllegalArgumentException
	{
		if(input == null)
			throw new IllegalArgumentException("Input is null");
		if(input instanceof String)
			if(AlchemyRecipes.getItems(input, 0).isEmpty())
				throw new IllegalArgumentException("No oredict item found for \""+input+"\"");
	}
	
	/**
	 * Returns an entry for a result of combining the cards of two items. Used in the Punch Designix.
	 */
	@Nonnull
	public static ItemStack getCombination(@Nonnull ItemStack input1, @Nonnull ItemStack input2, Mode mode)
	{
		ItemStack item;
		if (input1.isEmpty() || input2.isEmpty()) {return ItemStack.EMPTY;}

		String[] itemNames1 = getDictionaryNames(input1);
		String[] itemNames2 = getDictionaryNames(input2);

		item = getCombination(input1.getItem(), input1.getItemDamage(), input2.getItem(), input2.getItemDamage(), mode);

		if(item.isEmpty())
			for(String str2 : itemNames2)
				if(!(item = getCombination(input1.getItem(), input1.getItemDamage(), str2, OreDictionary.WILDCARD_VALUE, mode)).isEmpty())
					break;

		if(item.isEmpty())
			for(String str1 : itemNames1)
				if(!(item = getCombination(str1, OreDictionary.WILDCARD_VALUE, input2.getItem(), input2.getItemDamage(), mode)).isEmpty())
					break;

		if(item.isEmpty())
			for(String str1 : itemNames1)
				for(String str2 : itemNames2)
					if(!(item = getCombination(str1, OreDictionary.WILDCARD_VALUE, str2, OreDictionary.WILDCARD_VALUE, mode)).isEmpty())
						break;

		if(item.isEmpty())
			if(input1.getItem().equals(Item.getItemFromBlock(MinestuckBlocks.genericObject)))
				item = mode == Mode.MODE_AND ? input1 : input2;
			else if(input2.getItem().equals(Item.getItemFromBlock(MinestuckBlocks.genericObject)))
				item = mode == Mode.MODE_AND ? input2 : input1;

		AlchemyCombinationEvent alchemyCombinationEvent = new AlchemyCombinationEvent(input1, input2, item);
		MinecraftForge.EVENT_BUS.post(alchemyCombinationEvent);
		return alchemyCombinationEvent.getResultItem();
	}
	
	@Nonnull
	private static ItemStack getCombination(Object input1, int damage1, Object input2, int damage2, Mode mode)
	{
		ItemStack item;
		boolean b1 = damage1 != OreDictionary.WILDCARD_VALUE, b2 = damage2 != OreDictionary.WILDCARD_VALUE;
		
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
		
		if(item == null || item.isEmpty())
			return ItemStack.EMPTY;
		
		return item;
	}
	
	protected static String[] getDictionaryNames(@Nonnull ItemStack stack)
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
