package com.mraof.minestuck.item.crafting.alchemy;

import com.mraof.minestuck.block.MSBlocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.Tag;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.Collection;
import java.util.Hashtable;
import java.util.List;
import java.util.stream.Collectors;

public class CombinationRegistry {
	private static Hashtable<List<Object>, ItemStack> combRecipes = new Hashtable<>();
	
	public enum Mode
	{
		MODE_AND("and"),
		MODE_OR("or");
		
		final String name;
		
		Mode(String name)
		{
			this.name = name;
		}
		
		public String asString()
		{
			return name;
		}
		
		public boolean asBoolean()
		{
			return this == MODE_AND;
		}
		
		public static Mode fromBoolean(boolean b)
		{
			return b ? MODE_AND : MODE_OR;
		}
		
		public static Mode fromString(String str)
		{
			if(str.equals("&&") || str.equalsIgnoreCase("and"))
				return MODE_AND;
			else if(str.equals("||") || str.equalsIgnoreCase("or"))
				return MODE_OR;
			else throw new IllegalArgumentException(str+" is not a valid combination type");
		}
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
			Collection<Tag<Item>> itemTags2 = getTags(input2);
			
			for(Tag<Item> str2 : itemTags2)
				if(!(item = getCombination(input1.getItem(), str2, mode)).isEmpty())
					return item;
			
			Collection<Tag<Item>> itemTags1 = getTags(input1);
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
	
	protected static Collection<Tag<Item>> getTags(@Nonnull ItemStack stack)
	{
		return ItemTags.getCollection().getOwningTags(stack.getItem()).stream().map(ItemTags.getCollection()::get).collect(Collectors.toSet());
	}
	
	public static Hashtable<List<Object>, ItemStack> getAllConversions()
	{
		return combRecipes;
	}
}
