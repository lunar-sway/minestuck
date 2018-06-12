package com.mraof.minestuck.world.lands.decorator.structure;

import java.util.Random;

import com.mraof.minestuck.block.MinestuckBlocks;
import com.mraof.minestuck.world.lands.gen.ChunkProviderLands;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.structure.StructureBoundingBox;

public class BucketDecorator extends SimpleStructureDecorator
{
	
	private Block[] liquidBlocks = {Blocks.AIR, Blocks.WATER, Blocks.LAVA, MinestuckBlocks.blockBlood, MinestuckBlocks.blockOil, MinestuckBlocks.blockBrainJuice};
	
	public BucketDecorator(Biome... biomes)
	{
		super(biomes);
	}
	
	@Override
	protected BlockPos generateStructure(World world, Random random, BlockPos pos, ChunkProviderLands provider)
	{
		rotation = random.nextBoolean();
		xCoord = pos.getX();
		zCoord = pos.getZ();
		yCoord = world.getPrecipitationHeight(pos).getY();
		if(world.getBlockState(new BlockPos(xCoord, yCoord -1, zCoord)).getMaterial().isLiquid())
			return null;
		yCoord -= random.nextInt(3);
		boolean variant = random.nextDouble() < 0.4;
		IBlockState block = provider.blockRegistry.getBlockState(random.nextDouble() < 0.3 ? "bucket2" : "bucket1");
		IBlockState liquid;
		if(random.nextBoolean())
			liquid = liquidBlocks[random.nextInt(liquidBlocks.length)].getDefaultState();
		else liquid = provider.blockRegistry.getBlockState(random.nextBoolean() ? "ocean" : "river");
		
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
		return null;
	}
	
	@Override
	public int getCount(Random random)
	{
		return random.nextDouble() < 0.07F ? 1 : 0;
	}
	
	@Override
	public float getPriority()
	{
		return 0.5F;
	}
	
}