package com.mraof.minestuck.block.plant;

import com.mraof.minestuck.util.MSTags;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.state.BlockState;

public class PetrifiedFloraBlock extends BushBlock
{
	public PetrifiedFloraBlock(Properties properties)
	{
		super(properties);
	}
	
	@Override
	protected boolean mayPlaceOn(BlockState state, BlockGetter level, BlockPos pos)
	{
		return state.is(MSTags.Blocks.PETRIFIED_FLORA_PLACEABLE);
	}
}