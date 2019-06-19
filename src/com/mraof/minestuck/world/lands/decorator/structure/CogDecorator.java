package com.mraof.minestuck.world.lands.decorator.structure;

import java.util.Random;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.Heightmap;

import com.mraof.minestuck.world.lands.gen.ChunkProviderLands;

public class CogDecorator extends SimpleStructureDecorator
{
	
	@Override
	public BlockPos generateStructure(World world, Random random, BlockPos pos, ChunkProviderLands provider)
	{
		boolean big = random.nextDouble() >= 0.9;
		int blocksDown = random.nextInt(big ? 4 : 3);
		rotation = random.nextBoolean();
		
		xCoord = pos.getX();
		zCoord = pos.getZ();
		yCoord = world.getHeight(Heightmap.Type.WORLD_SURFACE_WG, pos).getY() - blocksDown;
		if(world.getBlockState(new BlockPos(xCoord, yCoord - 1, zCoord)).getMaterial().isLiquid())
			return null;
		IBlockState block = provider.blockRegistry.getBlockState(random.nextBoolean() ? "structure_primary" : "structure_secondary");
		/*
		StructureBoundingBox boundingBox;
		if(!big)
			boundingBox = new StructureBoundingBox(rotation?zCoord:xCoord - 2, yCoord, rotation?xCoord:zCoord, rotation?zCoord:xCoord + 2, yCoord + 4, rotation?xCoord:zCoord);
		else boundingBox = new StructureBoundingBox(rotation?zCoord:xCoord - 3, yCoord, rotation?xCoord:zCoord, rotation?zCoord:xCoord + 4, yCoord + 7, rotation?xCoord:zCoord + 1);
		if(provider.isBBInSpawn(boundingBox))
			return null;
		*/
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
			IBlockState block2 = provider.blockRegistry.getBlockState(random.nextBoolean() ? "structure_primary" : "structure_secondary");
			
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
		return null;
	}
	
	@Override
	public int getCount(Random random)
	{
		return random.nextFloat() < 0.1 ? 1 : 0;
	}
	
	@Override
	public float getPriority()
	{
		return 0.5F;
	}
	
}
