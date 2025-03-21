package com.mraof.minestuck.computer.editmode;

import com.mraof.minestuck.api.alchemy.GristSet;
import com.mraof.minestuck.network.editmode.ServerEditPackets;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class ClientDeployList
{
	public static void load(ServerEditPackets.UpdateDeployList packet, HolderLookup.Provider provider)
	{
		if(entryList == null)
			entryList = new ArrayList<>();
		else entryList.clear();
		ListTag list = packet.data().getList("l", Tag.TAG_COMPOUND);
		for(int i = 0; i < list.size(); i++)
		{
			CompoundTag tag = list.getCompound(i);
			Entry entry = new Entry();
			entry.item = ItemStack.parse(provider, Objects.requireNonNull(tag.get("item"))).orElseThrow();
			entry.index = tag.getInt("i");
			
			entry.cost = GristSet.Codecs.LIST_CODEC.parse(NbtOps.INSTANCE, tag.get("cost")).getOrThrow();
			entry.category = DeployList.EntryLists.values()[tag.getInt("cat")];
			
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
		private GristSet.Immutable cost;
		private int index;
		private DeployList.EntryLists category;
		
		public GristSet getCost()
		{
			return cost;
		}
		
		public DeployList.EntryLists getCategory() { return category; }
		
		public int getIndex()
		{
			return index;
		}
		
	}
}
