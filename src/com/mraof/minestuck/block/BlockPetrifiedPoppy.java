	package com.mraof.minestuck.block;

import com.mraof.minestuck.item.TabMinestuck;

import net.minecraft.block.Block;
import net.minecraft.block.BlockBush;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockPetrifiedPoppy extends BlockBush {
	public BlockPetrifiedPoppy()
	{
		super();
		setCreativeTab(TabMinestuck.instance);
		setUnlocalizedName("petrifiedPoppy");
		setSoundType(SoundType.STONE);
	}
	
	protected boolean canSustainPlant(IBlockState state)
    {
        return state.getBlock() == Blocks.COBBLESTONE || state.getBlock() == Blocks.STONE || state.getBlock() == Blocks.GRAVEL;
    }

	@Override
	public MapColor getMapColor(IBlockState state, IBlockAccess worldIn, BlockPos pos)
	{
		return MapColor.GRAY;
	}
}
