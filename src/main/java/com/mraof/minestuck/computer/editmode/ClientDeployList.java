package com.mraof.minestuck.computer.editmode;

import com.mraof.minestuck.alchemy.GristSet;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public final class ClientDeployList
{
	static void load(CompoundTag nbt)
	{
		if(entryList == null)
			entryList = new ArrayList<>();
		else entryList.clear();
		ListTag list = nbt.getList("l", Tag.TAG_COMPOUND);
		for(int i = 0; i < list.size(); i++)
		{
			CompoundTag tag = list.getCompound(i);
			Entry entry = new Entry();
			entry.item = ItemStack.of(tag);
			entry.index = tag.getInt("i");
			
			entry.cost = GristSet.read(tag.getList("cost", Tag.TAG_COMPOUND));
			
			entryList.add(entry);
		}
	}
	
	private static List<Entry> entryList;
	
	public static Entry getEntry(ItemStack stack)
	{
		stack = DeployList.cleanStack(stack);
		for(Entry entry : entryList)
			if(ItemStack.matches(entry.item, stack))
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