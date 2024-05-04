package com.mraof.minestuck.block.plant;

import com.mraof.minestuck.util.MSTags;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SaplingBlock;
import net.minecraft.world.level.block.grower.TreeGrower;
import net.minecraft.world.level.block.state.BlockState;

public class AspectSaplingBlock extends SaplingBlock
{
	public AspectSaplingBlock(TreeGrower pTreeGrower, Properties pProperties)
	{
		super(pTreeGrower, pProperties);
	}
	
	@Override
	protected boolean mayPlaceOn(BlockState pState, BlockGetter pLevel, BlockPos pPos)
	{
		return pState.is(MSTags.Blocks.ASPECT_TREE_PLACEABLE) || pState.is(Blocks.DIRT);
	}
}
