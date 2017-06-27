package com.mraof.minestuck.item;

import com.mraof.minestuck.Minestuck;

import net.minecraft.item.ItemRecord;
import net.minecraft.util.SoundEvent;

public class ItemMinestuckRecord extends ItemRecord
{
	public ItemMinestuckRecord(String recordName, SoundEvent soundIn)
	{
		super(recordName, soundIn);
		setCreativeTab(Minestuck.tabMinestuck);
	}
}