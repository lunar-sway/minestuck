package com.mraof.minestuck.block;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;

import javax.annotation.Nullable;

public class ReturnNodeBlock extends GateBlock
{
	
	public ReturnNodeBlock(Properties properties)
	{
		super(properties);
	}
	
	@Override
	protected boolean isValid(BlockPos pos, World world)
	{
		for(int x = -1; x <= 0; x++)
			for(int z = -1; z <= 0; z++)
				if(x != 0 || z != 0)
				{
					BlockState block = world.getBlockState(pos.add(x, 0, z));
					if(block.getBlock() != this || block.get(MAIN))
						return false;
				}
		
		return true;
	}
	
	@Override
	protected BlockPos findMainComponent(BlockPos pos, World world)
	{
		for(int x = 0; x <= 1; x++)
			for(int z = 0; z <= 1; z++)
				if(x != 0 || z != 0)
				{
					BlockState block = world.getBlockState(pos.add(x, 0, z));
					if(block.getBlock() == this && block.get(MAIN))
						return pos.add(x, 0, z);
				}
		
		return null;
	}
	
	@Override
	protected void removePortal(BlockPos pos, World world)
	{
		for(int x = -1; x <= 0; x++)
			for(int z = -1; z <= 0; z++)
				if(world.getBlockState(pos.add(x, 0, z)).getBlock() == this)
					world.removeBlock(pos.add(x, 0, z), false);
	}
	
	public static void placeReturnNode(IWorld world, BlockPos nodePos, @Nullable MutableBoundingBox boundingBox)
	{
		for(int i = 0; i < 4; i++)
		{
			BlockPos pos = nodePos.add(i % 2, 0, i/2);
			if(boundingBox == null || boundingBox.isVecInside(pos))
			{
				if(i == 3)
					world.setBlockState(pos, MSBlocks.RETURN_NODE.getDefaultState().cycle(GateBlock.MAIN), Constants.BlockFlags.BLOCK_UPDATE);
				else world.setBlockState(pos, MSBlocks.RETURN_NODE.getDefaultState(), Constants.BlockFlags.BLOCK_UPDATE);
			}
		}
	}
}