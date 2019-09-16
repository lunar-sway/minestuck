package com.mraof.minestuck.alchemy;

import java.util.Arrays;
import java.util.Hashtable;
import java.util.List;

import javax.annotation.Nonnull;

import com.mraof.minestuck.block.MSBlocks;

import com.mraof.minestuck.util.Debug;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tags.Tag;
import net.minecraft.util.IItemProvider;

public class CombinationRegistry {
	private static Hashtable<List<Object>, ItemStack> combRecipes = new Hashtable<>();
	
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
	public static void addCombination(@Nonnull IItemProvider input1, @Nonnull IItemProvider input2, Mode mode, @Nonnull ItemStack output) {
		addCombinationInternal(input1.asItem(), input2.asItem(), mode, output);
	}
	
	public static void addCombination(Tag<Item> tagInput, IItemProvider input, Mode mode, @Nonnull ItemStack output)
	{
		addCombinationInternal(tagInput, input.asItem(), mode, output);
	}
	
	public static void addCombination(Tag<Item> input1, Tag<Item> input2, Mode mode, @Nonnull ItemStack output)
	{
		addCombinationInternal(input1, input2, mode, output);
	}
	
	private static void addCombinationInternal(Object input1, Object input2, Mode mode, @Nonnull ItemStack output)
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
			Debug.warnf("[Minestuck] The recipe in question: %s %s %s -> %s", input1 instanceof Item ? ((Item) input1).getName() : input1, mode.getStr(), input2 instanceof Item ? ((Item) input2).getName() : input2, output);
			return;
		}
		
		int index = input1.hashCode() - input2.hashCode();
		if(index > 0)
			combRecipes.put(Arrays.asList(input1, input2, mode), output);
		else combRecipes.put(Arrays.asList(input2, input1, mode), output);
	}
	
	private static void checkIsValid(Object input) throws IllegalArgumentException
	{
		if(input == null)
			throw new IllegalArgumentException("Input is null");
		if(input instanceof String)
			if(AlchemyRecipes.getItems(input).isEmpty())
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
		
		if((item = getCombination(input1.getItem(), input2.getItem(), mode)).isEmpty())
		{
			Tag<Item>[] itemTags2 = getTags(input2);
			
			for(Tag<Item> str2 : itemTags2)
				if(!(item = getCombination(input1.getItem(), str2, mode)).isEmpty())
					return item;
			
			Tag<Item>[] itemTags1 = getTags(input1);
			for(Tag<Item> str1 : itemTags1)
				if(!(item = getCombination(str1, input2.getItem(), mode)).isEmpty())
					return item;
			
			for(Tag<Item> tag1 : itemTags1)
				for(Tag<Item> tag2 : itemTags2)
					if(!(item = getCombination(tag1, tag2, mode)).isEmpty())
						return item;
		}
		
		if(item.isEmpty())
			if(input1.getItem().equals(MSBlocks.GENERIC_OBJECT.asItem()))
				return mode == Mode.MODE_AND ? input1 : input2;
			else if(input2.getItem().equals(MSBlocks.GENERIC_OBJECT.asItem()))
				return mode == Mode.MODE_AND ? input2 : input1;
		return item;
	}
	
	@Nonnull
	private static ItemStack getCombination(Object input1, Object input2, Mode mode)
	{
		ItemStack item;
		
		int index = input1.hashCode() - input2.hashCode();
		if(index > 0)
		{
			if((item = combRecipes.get(Arrays.asList(input1, input2, mode))) != null);
		}
		else
		{
			if((item = combRecipes.get(Arrays.asList(input2, input1, mode))) != null);
		}
		
		if(item == null || item.isEmpty())
			return ItemStack.EMPTY;
		
		return item;
	}
	
	protected static Tag<Item>[] getTags(@Nonnull ItemStack stack)
	{
		/*int[] itemIDs = OreDictionary.getOreIDs(stack);
		String[] itemNames = new String[itemIDs.length];
		for(int i = 0; i < itemIDs.length; i++)
			itemNames[i] = OreDictionary.getOreName(itemIDs[i]);*/
		return null;	//TODO Figure out tags
	}
	
	public static Hashtable<List<Object>, ItemStack> getAllConversions()
	{
		return combRecipes;
	}
}
