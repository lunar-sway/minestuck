package com.mraof.minestuck.editmode;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.item.ItemStack;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.util.GristSet;
import com.mraof.minestuck.util.GristType;

/**
 * This class will be used to keep track of all deployable
 * items accessible by the server.
 * @author kirderf1
 */
public class DeployList {
	
	private static final List<DeployEntry> list = new ArrayList<DeployEntry>();
	
	public static void registerItems() {
		registerItem(new ItemStack(Minestuck.blockMachine,1,0), new GristSet(), new GristSet(GristType.Build, 100), 0);
		registerItem(new ItemStack(Minestuck.blockMachine,1,1), new GristSet(GristType.Shale, 4), 0);
		registerItem(new ItemStack(Minestuck.blockMachine,1,2), new GristSet(), new GristSet(GristType.Build, 100), 0);
		registerItem(new ItemStack(Minestuck.blockMachine,1,3), new GristSet(), new GristSet(GristType.Build, 100), 0);
//		ItemStack cardStack = new ItemStack(Minestuck.punchedCard);	Will be a special case instead.
//		cardStack.stackTagCompound = new NBTTagCompound();
//		cardStack.stackTagCompound.setInteger("contentID", Minestuck.cruxiteArtifact.itemID);
//		registerItem(cardStack, new GristSet(), null);
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
	 * @param cost1 How much it costs the first time deployed.
	 * @param cost2 How much it costs after the first times. Null if only deployable once.
	 * First cost will always be used when not in hardmode.
	 * @param tier The tier of the item; what connection position required in an unfinished chain to deploy.
	 * All will be available to all players when the chain is complete.
	 */
	public static void registerItem(ItemStack stack, GristSet cost1, GristSet cost2, int tier) {
		stack.stackSize = 1;
		for(DeployEntry entry : list)
			if(ItemStack.areItemStacksEqual(entry.item, stack))
				throw new IllegalStateException("Item stack already added to the deploy list:"+stack);
		list.add(new DeployEntry(stack, cost1, cost2, tier));
	}
	
	public static List<ItemStack> getItemList() {
		List<ItemStack> itemList = new ArrayList<ItemStack>();
		for(DeployEntry entry : list)
			itemList.add(entry.item.copy());
		return itemList;
	}
	
	public static List<ItemStack> getItemListByMaximumTier(int tier) {
		List<ItemStack> itemList = new ArrayList<ItemStack>();
		for(DeployEntry entry : list)
			if(entry.tier <= tier)
				itemList.add(entry.item);
		return itemList;
	}
	
	public static GristSet getPrimaryCost(ItemStack stack) {
		for(DeployEntry entry : list)
			if(ItemStack.areItemStacksEqual(entry.item, stack))
				return entry.cost1;
		return null;
	}
	
	public static GristSet getSecondaryCost(ItemStack stack) {
		for(DeployEntry entry : list)
			if(ItemStack.areItemStacksEqual(entry.item, stack))
				return entry.cost2;
		return null;
	}
	
	public static boolean containsItemStack(ItemStack stack) {
		for(DeployEntry entry : list)
			if(ItemStack.areItemStacksEqual(entry.item, stack))
				return true;
		return false;
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
