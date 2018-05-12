package com.mraof.minestuck.block;

import com.mraof.minestuck.item.TabMinestuck;
import net.minecraft.block.BlockStairs;
import net.minecraft.block.state.IBlockState;

public class BlockMinestuckStairs extends BlockStairs
{
	public BlockMinestuckStairs(IBlockState modelState)
	{
		super(modelState);
		setCreativeTab(TabMinestuck.instance);
		this.useNeighborBrightness = true;
	}

}
