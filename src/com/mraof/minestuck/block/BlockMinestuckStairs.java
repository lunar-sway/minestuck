package com.mraof.minestuck.block;

import com.mraof.minestuck.Minestuck;
import net.minecraft.block.BlockStairs;
import net.minecraft.block.state.IBlockState;

public class BlockMinestuckStairs extends BlockStairs
{
	public BlockMinestuckStairs(IBlockState modelState)
	{
		super(modelState);
		setCreativeTab(Minestuck.tabMinestuck);
		this.useNeighborBrightness = true;
	}

}
