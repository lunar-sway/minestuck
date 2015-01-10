package com.mraof.minestuck.editmode;

import java.util.ArrayList;
import java.util.Iterator;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.util.AlchemyRecipeHandler;
import com.mraof.minestuck.util.GristSet;
import com.mraof.minestuck.util.GristType;

/**
 * This class will be used to keep track of all deployable
 * items accessible by the server.
 * @author kirderf1
 */
public class DeployList {
	
	private static final ArrayList<DeployEntry> list = new ArrayList<DeployEntry>();
	
	public static void registerItems() {
		registerItem(new ItemStack(Minestuck.blockMachine,1,0), new GristSet()/*, new GristSet(GristType.Build, 100) This should be improved somehow*/, 0);
		registerItem(new ItemStack(Minestuck.blockMachine,1,2), new GristSet()/*, new GristSet(GristType.Build, 100)*/, 0);
		registerItem(AlchemyRecipeHandler.createCard(new ItemStack(Minestuck.cruxiteArtifact), true), new GristSet(), null, 0);
		registerItem(new ItemStack(Minestuck.blockMachine,1,3), new GristSet()/*, new GristSet(GristType.Build, 100)*/, 0);
		registerItem(new ItemStack(Minestuck.blockMachine,1,1), new GristSet(GristType.Shale, 4), 0);
	}
	
	public static void registerItem(ItemStack stack) {
		registerItem(stack, new GristSet(), 0);
	}
	
	public static void registerItem(ItemStack stack, GristSet cost, int tier) {
		registerItem(stack, cost, cost, tier);
	}
	
	/**
	 * Register the specific item as deployable.
	 * @param item The item to be registered.
	 * The itemstack can have nbt tags, with the exception of the display tag.
	 * @param cost1 How much it costs the first time deployed.
	 * @param cost2 How much it costs after the first times. Null if only deployable once.
	 * First cost will always be used when not in hardmode.
	 * @param tier The tier of the item; what connection position required in an unfinished chain to deploy.
	 * All will be available to all players when the chain is complete.
	 */
	public static void registerItem(ItemStack stack, GristSet cost1, GristSet cost2, int tier) {
		stack = cleanStack(stack);
		if(containsItemStack(stack))
			throw new IllegalStateException("Item stack already added to the deploy list:"+stack);
		list.add(new DeployEntry(stack, cost1, cost2, tier));
	}
	
	public static ArrayList<ItemStack> getItemList() {
		ArrayList<ItemStack> itemList = new ArrayList<ItemStack>();
		for(DeployEntry entry : list)
			itemList.add(entry.item.copy());
		return itemList;
	}
	
	public static ArrayList<ItemStack> getItemListByMaximumTier(int tier) {
		ArrayList<ItemStack> itemList = new ArrayList<ItemStack>();
		for(DeployEntry entry : list)
			if(entry.tier <= tier)
				itemList.add(entry.item);
		return itemList;
	}
	
	private static ItemStack cleanStack(ItemStack stack)
	{
		if(stack == null)
			return null;
		stack = stack.copy();
		stack.stackSize = 1;
		if(!stack.hasTagCompound())
			stack.setTagCompound(new NBTTagCompound());
		//if(stack.stackTagCompound.getName() == "")
			//stack.stackTagCompound.setName("tag");
		else
		{
			stack.getTagCompound().removeTag("display");
			if(stack.getItem().equals(Minestuck.captchaCard))
				stack.getTagCompound().setInteger("contentMeta", 0);
		}
		return stack;
	}
	
	public static GristSet getPrimaryCost(ItemStack stack) {
		stack = cleanStack(stack);
		for(DeployEntry entry : list)
			if(ItemStack.areItemStacksEqual(entry.item, stack))
				return entry.cost1;
		return null;
	}
	
	public static GristSet getSecondaryCost(ItemStack stack) {
		stack = cleanStack(stack);
		for(DeployEntry entry : list)
			if(ItemStack.areItemStacksEqual(entry.item, stack))
				return entry.cost2;
		return null;
	}
	
	public static boolean containsItemStack(ItemStack stack) {
		stack = cleanStack(stack);
		for(DeployEntry entry : list)
			if(ItemStack.areItemStacksEqual(entry.item, stack))
				return true;
		return false;
	}
	
	public static int getOrdinal(ItemStack stack) {
		stack = cleanStack(stack);
		for(int i = 0; i < list.size(); i++)
			if(ItemStack.areItemStacksEqual(list.get(i).item,stack))
				return i;
		return -1;
	}
	
	public static int getTier(ItemStack stack) {
		stack = cleanStack(stack);
		for(DeployEntry entry : list)
			if(ItemStack.areItemStacksEqual(entry.item, stack))
				return entry.tier;
		return -1;
	}
	
	public static void applyConfigValues(boolean[] booleans)
	{
		ItemStack card = AlchemyRecipeHandler.createCard(new ItemStack(Minestuck.captchaCard), true);
		if(booleans[0] && !containsItemStack(card))
			registerItem(card, new GristSet(GristType.Build, 25), null, 0);
		else if(!booleans[0] && containsItemStack(card))
		{
			Iterator<DeployEntry> iter = list.iterator();
			while(iter.hasNext())
			{
				ItemStack stack = iter.next().item;
				if(ItemStack.areItemsEqual(stack, card))
				{
					iter.remove();
					break;
				}
			}
		}
		
	}
	
	private static class DeployEntry {
		
		ItemStack item;
		
		GristSet cost1, cost2;
		
		int tier;
		
		DeployEntry(ItemStack item, GristSet cost1, GristSet cost2, int tier) {
			this.item  = item;
			this.cost1 = cost1;
			this.cost2 = cost2;
			this.tier = tier;
		}
		
	}
	
}
