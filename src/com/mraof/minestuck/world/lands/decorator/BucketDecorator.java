package com.mraof.minestuck.world.lands.decorator;

import java.util.Random;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.world.lands.gen.ChunkProviderLands;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;

public class BucketDecorator extends SimpleStructureDecorator
{
	
	private Block[] liquidBlocks = {Blocks.air, Blocks.water, Blocks.lava, Minestuck.blockBlood, Minestuck.blockOil, Minestuck.blockBrainJuice};
	
	@Override
	public BlockPos generateStructure(World world, Random random, int chunkX, int chunkZ, ChunkProviderLands provider)
	{
		if(random.nextDouble() < 0.07F)
		{
			rotation = random.nextBoolean();
			xCoord = (chunkX << 4) + random.nextInt(16) + 8;
			zCoord = (chunkZ << 4) + random.nextInt(16) + 8;
			yCoord = world.getPrecipitationHeight(new BlockPos(xCoord, 0, zCoord)).getY();
			if(world.getBlockState(new BlockPos(xCoord, yCoord -1, zCoord)).getBlock().getMaterial().isLiquid())
				return null;
			yCoord -= random.nextInt(3);
			boolean variant = random.nextDouble() < 0.4;
			IBlockState block = random.nextDouble() < 0.3 ? Blocks.iron_block.getDefaultState() /*<- Should this continue to be a thing?*/ : Blocks.quartz_block.getDefaultState();
			IBlockState liquid;
			if(random.nextBoolean())
				liquid = liquidBlocks[random.nextInt(liquidBlocks.length)].getDefaultState();
			else if(random.nextBoolean())
				liquid = provider.oceanBlock;
			else liquid = provider.riverBlock;
			
			StructureBoundingBox boundingBox;
			if(variant)
				boundingBox = new StructureBoundingBox(rotation?zCoord:xCoord - 2, yCoord, rotation?xCoord:zCoord - 2, rotation?zCoord:xCoord + 3, yCoord + 6, rotation?xCoord:zCoord + 3);
			else boundingBox = new StructureBoundingBox(rotation?zCoord:xCoord - 2, yCoord, rotation?xCoord:zCoord - 2, rotation?zCoord:xCoord + 2, yCoord + 4, rotation?xCoord:zCoord + 2);
			if(provider.isBBInSpawn(boundingBox))
				return null;
			
			if(variant)
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
		return null;
	}

	@Override
	public float getPriority()
	{
		return 0.5F;
	}
	
}
