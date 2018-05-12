package com.mraof.minestuck.block;

import com.mraof.minestuck.item.TabMinestuck;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;

public class BlockCruxtruderLid extends Block
{
	public BlockCruxtruderLid()
	{
		super(Material.IRON);
		setCreativeTab(TabMinestuck.instance);
	}
	
	@Override
	public boolean isOpaqueCube(IBlockState state)
	{
		return false;
	}
}
