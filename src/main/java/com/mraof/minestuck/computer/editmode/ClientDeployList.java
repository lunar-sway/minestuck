package com.mraof.minestuck.computer.editmode;

import com.mraof.minestuck.item.crafting.alchemy.GristSet;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraftforge.common.util.Constants;

import java.util.ArrayList;
import java.util.List;

public final class ClientDeployList
{
	static void load(CompoundNBT nbt)
	{
		if(entryList == null)
			entryList = new ArrayList<>();
		else entryList.clear();
		ListNBT list = nbt.getList("l", Constants.NBT.TAG_COMPOUND);
		for(int i = 0; i < list.size(); i++)
		{
			CompoundNBT tag = list.getCompound(i);
			Entry entry = new Entry();
			entry.item = ItemStack.read(tag);
			entry.index = tag.getInt("i");
			
			entry.cost = GristSet.read(tag.getList("cost", Constants.NBT.TAG_COMPOUND));
			
			entryList.add(entry);
		}
	}
	
	private static List<Entry> entryList;
	
	public static Entry getEntry(ItemStack stack)
	{
		stack = DeployList.cleanStack(stack);
		for(Entry entry : entryList)
			if(ItemStack.areItemStacksEqual(entry.item, stack))
				return entry;
		return null;
	}
	
	public static class Entry
	{
		private ItemStack item;
		private GristSet cost;
		private int index;
		
		public GristSet getCost()
		{
			return cost;
		}
		
		public int getIndex()
		{
			return index;
		}
	}
}