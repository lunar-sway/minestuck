package com.mraof.minestuck.item;

import net.minecraft.item.ItemRecord;
import net.minecraft.util.SoundEvent;

/**
 * Identical to {@link ItemRecord} exept with a public constructor
 */
public class ModRecordItem extends ItemRecord
{
	public ModRecordItem(int comparatorValueIn, SoundEvent soundIn, Properties builder)
	{
		super(comparatorValueIn, soundIn, builder);
	}
}