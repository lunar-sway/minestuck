package com.mraof.minestuck.block.plant;

import com.mraof.minestuck.block.MSBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.PlantType;

import java.util.Random;

public class GlowingMushroomBlock extends BushBlock
{
	public GlowingMushroomBlock(Properties properties)
	{
		super(properties);
	}
	
	@Override
	protected boolean mayPlaceOn(BlockState state, BlockGetter level, BlockPos pos)
	{
		return state.isSolidRender(level, pos);
	}
	
	@Override
	@SuppressWarnings("deprecation")
	public void tick(BlockState state, ServerLevel level, BlockPos pos, Random random)
	{
		super.tick(state, level, pos, random);
		if(canSpread(level, pos, state) && random.nextInt(25) == 0)
		{
			int count = 0;
			Iterable<BlockPos> blocks = BlockPos.betweenClosed(pos.offset(-4, -1, -4), pos.offset(4, 1, 4));
			
			for(BlockPos checkPos : blocks)
				if(level.getBlockState(checkPos).is(this))
				{
					count++;
					if (count >= 5)
						return;
				}
			
			for (int i = 0; i < 5; ++i)
			{
				BlockPos spreadPos = pos.offset(random.nextInt(3) - 1, random.nextInt(2) - random.nextInt(2), random.nextInt(3) - 1);
				if (level.isEmptyBlock(spreadPos) && this.canSpread(level, spreadPos, this.defaultBlockState()))
				{
					level.setBlock(spreadPos, this.defaultBlockState(), Block.UPDATE_CLIENTS);
					return;
				}
			}
		}
	}
	
	@Override
	public PlantType getPlantType(BlockGetter level, BlockPos pos)
	{
		return PlantType.CAVE;
	}
	
	public boolean canSpread(Level level, BlockPos pos, BlockState state)
	{
		BlockState soil = level.getBlockState(pos.below());
		return soil.is(MSBlocks.BLUE_DIRT.get());
	}
}