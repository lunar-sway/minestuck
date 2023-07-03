package com.mraof.minestuck.block.plant;

import com.mraof.minestuck.world.gen.feature.tree.RainbowTree;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.SaplingBlock;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class RainbowSaplingBlock extends SaplingBlock
{
	public RainbowSaplingBlock(Properties pProperties)
	{
		super(new RainbowTree(), pProperties);
	}
	
	@Override
	protected boolean mayPlaceOn(BlockState state, BlockGetter level, BlockPos pos)
	{
		return state.is(BlockTags.WOOL) || super.mayPlaceOn(state, level, pos);
	}
}
