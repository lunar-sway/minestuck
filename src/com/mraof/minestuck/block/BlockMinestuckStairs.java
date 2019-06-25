package com.mraof.minestuck.block;

import net.minecraft.block.BlockStairs;
import net.minecraft.block.state.IBlockState;

/**
 * Only exists because the constructor in BlockStairs is protected
 */
public class BlockMinestuckStairs extends BlockStairs
{
	public BlockMinestuckStairs(IBlockState modelState, Properties properties)
	{
		super(modelState, properties);
	}

}
