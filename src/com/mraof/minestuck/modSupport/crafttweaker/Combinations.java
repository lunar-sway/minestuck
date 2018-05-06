package com.mraof.minestuck.modSupport.crafttweaker;

import com.mraof.minestuck.util.CombinationRegistry;
import crafttweaker.IAction;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.item.IItemStack;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@ZenClass("minestuck.Combinations")
@ZenRegister
public class Combinations
{
	static List<IAction> recipes = new ArrayList<>();
	
	@ZenMethod
	public static void addRecipe(IItemStack input1, IItemStack input2, String mode, IItemStack output)
	{
		ItemStack stack1 = (ItemStack) input1.getInternal();
		ItemStack stack2 = (ItemStack) input2.getInternal();
		
		recipes.add(new SetRecipe(stack1.getItem(), stack1.getItemDamage(), stack2.getItem(), stack2.getItemDamage(), getMode(mode), (ItemStack) output.getInternal()));
	}
	
	@ZenMethod
	public static void addOreDictRecipe(IItemStack input1, String input2, String mode, IItemStack output)
	{
		ItemStack stack = (ItemStack) input1.getInternal();
		recipes.add(new SetRecipe(stack.getItem(), stack.getItemDamage(), input2, OreDictionary.WILDCARD_VALUE, getMode(mode), (ItemStack) output.getInternal()));
	}
	
	@ZenMethod
	public static void addFullOreDictRecipe(String input1, String input2, String mode, IItemStack output)
	{
		recipes.add(new SetRecipe(input1, OreDictionary.WILDCARD_VALUE, input2, OreDictionary.WILDCARD_VALUE, getMode(mode), (ItemStack) output.getInternal()));
	}
	
	@ZenMethod
	public static void removeRecipe(IItemStack input1, IItemStack input2,  String mode)
	{
		ItemStack stack1 = (ItemStack) input1.getInternal();
		ItemStack stack2 = (ItemStack) input2.getInternal();
		recipes.add(new SetRecipe(stack1.getItem(), stack1.getItemDamage(), stack2.getItem(), stack2.getItemDamage(), getMode(mode), null));
	}
	
	@ZenMethod
	public static void removeOreDictRecipe(IItemStack input1, String input2, String mode)
	{
		ItemStack stack = (ItemStack) input1.getInternal();
		recipes.add(new SetRecipe(stack.getItem(), stack.getItemDamage(), input2, OreDictionary.WILDCARD_VALUE, getMode(mode), null));
	}
	
	@ZenMethod
	public static void removeFullOreDictRecipe(String input1, String input2, String mode)
	{
		recipes.add(new SetRecipe(input1, OreDictionary.WILDCARD_VALUE, input2, OreDictionary.WILDCARD_VALUE, getMode(mode), null));
	}
	
	private static CombinationRegistry.Mode getMode(String mode)
	{
		if(mode.equals("&") || mode.equals("&&") || mode.toLowerCase().equals("and"))
			return CombinationRegistry.Mode.MODE_AND;
		else if(mode.equals("|") || mode.equals("||") || mode.toLowerCase().equals("or"))
			return CombinationRegistry.Mode.MODE_OR;
		else throw new IllegalArgumentException("\""+mode+"doesn't match either AND or OR!");
	}
	
	private static class SetRecipe implements IAction
	{
		
		private final List<Object> inputs;
		private final ItemStack output;
		
		public SetRecipe(Object input1, int meta1, Object input2, int meta2, CombinationRegistry.Mode mode, ItemStack output)
		{
			int index = input1.hashCode() - input2.hashCode();
			if(index == 0)
				index = meta1 - meta2;
			if(index > 0)
				inputs = Arrays.asList(input1, meta1, input2, meta2, mode);
			else inputs = Arrays.asList(input2, meta2, input1, meta1, mode);
			this.output = output;
		}
		
		@Override
		public void apply()
		{
			CombinationRegistry.getAllConversions().remove(inputs);
			if(output != null)
				CombinationRegistry.getAllConversions().put(inputs, output);
		}

		@Override
		public String describe()
		{
			if(output == null)
				return "Removing Combination Recipe with ingredients \""+inputs+"\"";
			else return "Adding Combination Recipe for \""+output.getDisplayName()+"\"";
		}
	}
}