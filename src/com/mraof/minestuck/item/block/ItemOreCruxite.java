package com.mraof.minestuck.item.block;

import com.mraof.minestuck.block.OreCruxite;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;

public class ItemOreCruxite extends ItemBlock
{
	
	public ItemOreCruxite(Block block)
	{
		super(block);
	}
	
	@Override
	public int getMetadata(int damage)
	{
		return damage % OreCruxite.BLOCK_TYPE.getAllowedValues().size();
	}
	
}
