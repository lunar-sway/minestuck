package com.mraof.minestuck.world.gen.lands.decorator;

import java.util.Random;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.world.gen.ChunkProviderLands;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class BucketDecorator extends SimpleStructureDecorator
{
	
	private Block[] liquidBlocks = {Blocks.air, Blocks.water, Blocks.lava/*, Minestuck.blockBlood, Minestuck.blockOil, Minestuck.blockBrainJuice*/};
	
	@Override
	public void generate(World world, Random random, int chunkX, int chunkZ, ChunkProviderLands provider)
	{
		if(random.nextDouble() < 0.07F)
		{
			rotation = random.nextBoolean();
			xCoord = (chunkX << 4) + random.nextInt(16) + 8;
			zCoord = (chunkZ << 4) + random.nextInt(16) + 8;
			yCoord = world.getHorizon(new BlockPos(xCoord, 0, zCoord)).getY() - random.nextInt(3);
			IBlockState block = random.nextDouble() < 0.3 ? Blocks.iron_block.getDefaultState() : Blocks.quartz_block.getDefaultState();
			IBlockState liquid;
			if(random.nextBoolean())
				liquid = liquidBlocks[random.nextInt(liquidBlocks.length)].getDefaultState();
			else liquid = provider.oceanBlock;
			if(yCoord < 60)
				return;
			if(random.nextDouble() < 0.4)
			{
				placeBlocks(world, block, -1, 0, -1, 2, 0, 2);
				placeBlocks(world, block, -1, 1, -1, -1, 3, -1);
				placeBlocks(world, block, -1, 1, 2, -1, 3, 2);
				placeBlocks(world, block, 2, 1, -1, 2, 3, -1);
				placeBlocks(world, block, 2, 1, 2, 2, 3, 2);
				
				placeBlocks(world, block, 0, 1, -2, 1, 3, -2);
				placeBlocks(world, block, 0, 1, 3, 1, 3, 3);
				placeBlocks(world, block, -2, 1, 0, -2, 5, 1);
				placeBlocks(world, block, 3, 1, 0, 3, 5, 1);
				placeBlocks(world, block, -1, 6, 0, 2, 6, 1);
				
				placeBlocks(world, liquid, -1, 1, 0, 2, 3, 1);
				placeBlocks(world, liquid, 0, 1, -1, 1, 3, -1);
				placeBlocks(world, liquid, 0, 1, 2, 1, 3, 2);
			}
			else
			{
				placeBlocks(world, block, -1, 0, -1, 1, 0, 1);
				placeBlock(world, block, 0, 1, 2);
				placeBlock(world, block, 0, 1, -2);
				placeBlock(world, block, 2, 1, 0);
				placeBlock(world, block, -2, 1, 0);
				placeBlock(world, block, 1, 1, 1);
				placeBlock(world, block, 1, 1, -1);
				placeBlock(world, block, -1, 1, 1);
				placeBlock(world, block, -1, 1, -1);
				
				placeBlocks(world, block, -1, 2, -2, 1, 4, -2);
				placeBlocks(world, block, -1, 2, 2, 1, 4, 2);
				placeBlocks(world, block, -2, 2, -1, -2, 4, 1);
				placeBlocks(world, block, 2, 2, -1, 2, 4, 1);
				
				placeBlocks(world, liquid,-1, 1, 0, 1, 1, 0);
				placeBlock(world, liquid, 0, 1, -1);
				placeBlock(world, liquid, 0, 1, 1);
				placeBlocks(world, liquid, -1, 2, -1, 1, 3, 1);
			}
		}
	}

	@Override
	public float getPriority()
	{
		return 0.5F;
	}
	
}
