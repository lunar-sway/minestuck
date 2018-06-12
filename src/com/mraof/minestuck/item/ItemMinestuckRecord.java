package com.mraof.minestuck.item;

import net.minecraft.item.ItemRecord;
import net.minecraft.util.SoundEvent;

public class ItemMinestuckRecord extends ItemRecord
{
	public ItemMinestuckRecord(String recordName, SoundEvent soundIn)
	{
		super(recordName, soundIn);
		setCreativeTab(MinestuckItems.tabMinestuck);
	}
}