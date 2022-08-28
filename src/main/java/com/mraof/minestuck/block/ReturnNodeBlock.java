package com.mraof.minestuck.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.BoundingBox;

import javax.annotation.Nullable;

public class ReturnNodeBlock extends AbstractGateBlock
{
	public ReturnNodeBlock(Properties properties)
	{
		super(properties);
	}
	
	@Override
	protected boolean isValid(BlockPos pos, Level level)
	{
		for(int x = -1; x <= 0; x++)
			for(int z = -1; z <= 0; z++)
				if(x != 0 || z != 0)
				{
					BlockState block = level.getBlockState(pos.offset(x, 0, z));
					if(block.getBlock() != this || block.getValue(MAIN))
						return false;
				}
		
		return true;
	}
	
	@Override
	protected BlockPos findMainComponent(BlockPos pos, Level level)
	{
		for(int x = 0; x <= 1; x++)
			for(int z = 0; z <= 1; z++)
				if(x != 0 || z != 0)
				{
					BlockState block = level.getBlockState(pos.offset(x, 0, z));
					if(block.getBlock() == this && block.getValue(MAIN))
						return pos.offset(x, 0, z);
				}
		
		return null;
	}
	
	@Override
	protected void removePortal(BlockPos pos, Level level)
	{
		for(int x = -1; x <= 0; x++)
			for(int z = -1; z <= 0; z++)
				if(level.getBlockState(pos.offset(x, 0, z)).getBlock() == this)
					level.removeBlock(pos.offset(x, 0, z), false);
	}
	
	public static void placeReturnNode(LevelAccessor level, BlockPos nodePos, @Nullable BoundingBox boundingBox)
	{
		for(int i = 0; i < 4; i++)
		{
			BlockPos pos = nodePos.offset(i % 2, 0, i/2);
			if(boundingBox == null || boundingBox.isInside(pos))
			{
				if(i == 3)
					level.setBlock(pos, MSBlocks.RETURN_NODE.get().defaultBlockState().setValue(MAIN, true), Block.UPDATE_CLIENTS);
				else level.setBlock(pos, MSBlocks.RETURN_NODE.get().defaultBlockState(), Block.UPDATE_CLIENTS);
			}
		}
	}
}