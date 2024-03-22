package com.mraof.minestuck.block.plant;

import com.mraof.minestuck.util.MSTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.CactusBlock;
import net.minecraft.world.level.block.state.BlockState;

public class WoodenCactusBlock extends CactusBlock
{
	public WoodenCactusBlock(Properties properties)
	{
		super(properties);
	}
	
	@Override
	public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos)
	{
		for(Direction direction : Direction.Plane.HORIZONTAL)
		{
			BlockState blockstate = level.getBlockState(pos.relative(direction));
			if(blockstate.isSolid() || level.getFluidState(pos.relative(direction)).is(FluidTags.LAVA))
			{
				return false;
			}
		}
		
		BlockState soil = level.getBlockState(pos.below());
		return isSustainableSoil(soil) && !level.getBlockState(pos.above()).liquid();
	}
	
	protected boolean isSustainableSoil(BlockState soil)
	{
		return soil.is(this) || soil.is(MSTags.Blocks.WOOD_TERRAIN_BLOCKS) || soil.is(BlockTags.PLANKS);
	}
}