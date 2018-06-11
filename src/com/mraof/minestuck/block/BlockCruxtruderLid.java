package com.mraof.minestuck.block;

import com.mraof.minestuck.item.TabMinestuck;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

public class BlockCruxtruderLid extends Block
{
	public AxisAlignedBB BLOCK_AABB = new AxisAlignedBB(2/16f, 0, 2/16f, 14/16f, 5/16f, 14/16f);
	
	public BlockCruxtruderLid()
	{
		super(Material.IRON);
		setCreativeTab(TabMinestuck.instance);
		setUnlocalizedName("cruxtruderLid");
	}
	
	@Override
	public boolean isOpaqueCube(IBlockState state)
	{
		return false;
	}
	
	@Override
	public boolean isFullCube(IBlockState state)
	{
		return false;
	}
	
	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
	{
		return BLOCK_AABB;
	}
}
