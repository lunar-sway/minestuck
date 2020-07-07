package com.mraof.minestuck.block;

import net.minecraft.block.BlockState;
import net.minecraft.block.StairsBlock;

/**
 * Only exists because the constructor in BlockStairs is protected
 */
public class ModStairsBlock extends StairsBlock
{
	public ModStairsBlock(BlockState modelState, Properties properties)
	{
		super(modelState, properties);
	}

}
