package com.mraof.minestuck.world.gen.lands.decorator;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;

import com.mraof.minestuck.util.Debug;
import com.mraof.minestuck.world.gen.ChunkProviderLands;

public class IceAndSnowDecorator implements ILandDecorator
{
	
	@Override
	public void generate(World world, Random random, int chunkX, int chunkZ, ChunkProviderLands provider)
	{
		for(int x = (chunkX << 4) + 8; x < (chunkX+1 << 4) + 8; x++)
			for(int z = (chunkZ << 4) + 8; z < (chunkZ+1 << 4) + 8; z++)
			{
				BlockPos pos = getTopSolidOrLiquidBlock(world, new BlockPos(x, 0, z));
				Block block1 = world.getBlockState(pos.down()).getBlock();
				Block block2 = world.getBlockState(pos).getBlock();
				if(block1 == Blocks.water)
					world.setBlockState(pos.down(), Blocks.ice.getDefaultState(), 2);
				else if(block2.isReplaceable(world, pos) && block1.getMaterial().isOpaque())
					world.setBlockState(pos, Blocks.snow_layer.getDefaultState(), 2);
			}
	}
	
	private BlockPos getTopSolidOrLiquidBlock(World world, BlockPos pos)//Because the method in the world class doesn't check for liquids
	{
		Chunk chunk = world.getChunkFromBlockCoords(pos);
		BlockPos blockpos1;
		BlockPos blockpos2;
		
		for (blockpos1 = new BlockPos(pos.getX(), chunk.getTopFilledSegment() + 16, pos.getZ()); blockpos1.getY() >= 0; blockpos1 = blockpos2)
		{
			blockpos2 = blockpos1.down();
			Block block = chunk.getBlock(blockpos2);
			
			if ((block.getMaterial().isOpaque() || block.getMaterial().isLiquid()) && !block.isLeaves(world, blockpos2) && !block.isFoliage(world, blockpos2))
			{
				break;
			}
		}
		
		return blockpos1;
	}
	
	@Override
	public float getPriority()
	{
		return 1F;
	}
}
