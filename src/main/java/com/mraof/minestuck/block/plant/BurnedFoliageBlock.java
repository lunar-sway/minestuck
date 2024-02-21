package com.mraof.minestuck.block.plant;

import com.mraof.minestuck.block.MSBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.GrassBlock;
import net.minecraft.world.level.block.TallGrassBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;

public class BurnedFoliageBlock extends TallGrassBlock
{
	public BurnedFoliageBlock(BlockBehaviour.Properties properties)
	{
		super(properties);
	}
	
	@Override
	protected boolean mayPlaceOn(BlockState state, BlockGetter level, BlockPos pos)
	{
		return state.is(MSBlocks.IGNEOUS_STONE.get());
	}
}
