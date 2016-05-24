package com.mraof.minestuck.item.block;

import com.mraof.minestuck.block.BlockCruxiteOre;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;

public class ItemCruxiteOre extends ItemBlock
{
	
	public ItemCruxiteOre(Block block)
	{
		super(block);
		setHasSubtypes(true);
	}
	
	@Override
	public int getMetadata(int damage)
	{
		return damage % BlockCruxiteOre.BLOCK_TYPE.getAllowedValues().size();
	}
	
}
