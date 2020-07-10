package com.mraof.minestuck.item;

import net.minecraft.item.MusicDiscItem;
import net.minecraft.util.SoundEvent;

/**
 * Identical to {@link MusicDiscItem} exept with a public constructor
 */
public class ModMusicDiscItem extends MusicDiscItem
{
	public ModMusicDiscItem(int comparatorValueIn, SoundEvent soundIn, Properties builder)
	{
		super(comparatorValueIn, soundIn, builder);
	}
}