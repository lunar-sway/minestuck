package com.mraof.minestuck.world.lands.decorator;

import java.util.Random;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;

import com.mraof.minestuck.world.lands.gen.ChunkProviderLands;

public class CogDecorator extends SimpleStructureDecorator
{
	
	@Override
	public BlockPos generate(World world, Random random, int chunkX, int chunkZ, ChunkProviderLands provider)
	{
		if(random.nextDouble() < 0.1)
		{
			boolean big = random.nextDouble() >= 0.9;
			int blocksDown = random.nextInt(big ? 4 : 3);
			rotation = random.nextBoolean();
			xCoord = ((chunkX << 4) | random.nextInt(16)) + 8;
			zCoord = ((chunkZ << 4) | random.nextInt(16)) + 8;
			yCoord = world.getPrecipitationHeight(new BlockPos(xCoord, 0, zCoord)).getY() - blocksDown;
			if(world.getBlockState(new BlockPos(xCoord, yCoord - 1, zCoord)).getBlock().getMaterial().isLiquid())
				return null;
			IBlockState[] materials = provider.aspect1.getStructureBlocks();
			IBlockState block = materials[random.nextInt(materials.length)];
			
			StructureBoundingBox boundingBox;
			if(!big)
				boundingBox = new StructureBoundingBox(rotation?zCoord:xCoord - 2, yCoord, rotation?xCoord:zCoord, rotation?zCoord:xCoord + 2, yCoord + 4, rotation?xCoord:zCoord);
			else boundingBox = new StructureBoundingBox(rotation?zCoord:xCoord - 3, yCoord, rotation?xCoord:zCoord, rotation?zCoord:xCoord + 4, yCoord + 7, rotation?xCoord:zCoord + 1);
			if(provider.isBBInSpawn(boundingBox))
				return null;
			
			if(!big)
			{
				placeBlock(world, block, -2, 0, 0);
				placeBlock(world, block, 0, 0, 0);
				placeBlock(world, block, 2, 0, 0);
				placeBlock(world, block, -2, 4, 0);
				placeBlock(world, block, 0, 4, 0);
				placeBlock(world, block, 2, 4, 0);
				placeBlocks(world, block, -1, 1, 0, 1, 1, 0);
				placeBlocks(world, block, -1, 3, 0, 1, 3, 0);
				placeBlocks(world, block, -2, 2, 0, -1, 2, 0);
				placeBlocks(world, block, 1, 2, 0, 2, 2, 0);
			}
			else
			{
				IBlockState block2 = block;
				if(random.nextBoolean())
					block2 = materials[random.nextInt(materials.length)];
				placeBlocks(world, block, -3, 0, 0, -2, 0, 1);
				placeBlocks(world, block, -3, 1, 0, -3, 1, 1);
				placeBlocks(world, block, 0, 0, 0, 1, 0, 1);
				
				placeBlocks(world, block, 3, 0, 0, 4, 0, 1);
				placeBlocks(world, block, 4, 1, 0, 4, 1, 1);
				placeBlocks(world, block, 4, 3, 0, 4, 4, 1);
				
				placeBlocks(world, block, 3, 7, 0, 4, 7, 1);
				placeBlocks(world, block, 4, 6, 0, 4, 6, 1);
				placeBlocks(world, block, 0, 7, 0, 1, 7, 1);
				
				placeBlocks(world, block, -3, 7, 0, -2, 7, 1);
				placeBlocks(world, block, -3, 6, 0, -3, 6, 1);
				placeBlocks(world, block, -3, 3, 0, -3, 4, 1);
				
				placeBlocks(world, block2, -2, 1, 0, 3, 1, 1);
				placeBlocks(world, block2, -2, 6, 0, 3, 6, 1);
				placeBlocks(world, block2, -2, 2, 0, -2, 5, 1);
				placeBlocks(world, block2, 3, 2, 0, 3, 5, 1);
				
				placeBlocks(world, block, -1, 2, 0, 2, 2, 1);
				placeBlocks(world, block, -1, 5, 0, 2, 5, 1);
				placeBlocks(world, block, -1, 3, 0, -1, 4, 1);
				placeBlocks(world, block, 2, 3, 0, 2, 4, 1);
			}
		}
		return null;
	}
	
	@Override
	public float getPriority()
	{
		return 0.5F;
	}
	
}
