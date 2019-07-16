package com.mraof.minestuck.world.lands.decorator.structure;

import java.util.Random;

import com.mraof.minestuck.world.lands.decorator.BiomeSpecificDecorator;
import com.mraof.minestuck.world.lands.gen.ChunkProviderLands;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;

public abstract class SimpleStructureDecorator extends BiomeSpecificDecorator
{
	
	protected boolean rotation;
	protected int xCoord, yCoord, zCoord;
	
	public SimpleStructureDecorator(Biome... biomes)
	{
		super(biomes);
	}
	
	@Override
	public BlockPos generate(World world, Random random, BlockPos pos, ChunkProviderLands provider)
	{
		if(provider.generatingStructure)
			return null;
		else return generateStructure(world, random, pos, provider);
	}
	
	protected abstract BlockPos generateStructure(World world, Random random, BlockPos pos, ChunkProviderLands provider);
	
	protected void placeBlocks(World world, BlockState block, int fromX, int fromY, int fromZ, int toX, int toY, int toZ)
	{
		for(int x = fromX; x <= toX; x++)
			for(int y = fromY; y <= toY; y++)
				for(int z = fromZ; z <= toZ; z++)
					placeBlock(world, block, x, y, z);
	}
	
	protected void placeBlock(World world, BlockState block, int xOffset, int yOffset, int zOffset)
	{
		int xPos = xCoord + (rotation ? zOffset : xOffset);
		int yPos = yCoord + yOffset;
		int zPos = zCoord + (rotation ? xOffset : zOffset);
		
		world.setBlockState(new BlockPos(xPos, yPos, zPos), block, 2);
	}
	
	protected void placeFloor(World world, BlockState floor, int xOffset, int yOffset, int zOffset)
	{
		do
		{
			placeBlock(world, floor, xOffset, yOffset, zOffset);
			yOffset--;
		} while(yCoord + yOffset >= 0 && !getBlockState(world, xOffset, yOffset, zOffset).getMaterial().isSolid());
	}
	
	protected BlockState getBlockState(World world, int xOffset, int yOffset, int zOffset)
	{
		int xPos = xCoord + (rotation ? zOffset : xOffset);
		int yPos = yCoord + yOffset;
		int zPos = zCoord + (rotation ? xOffset : zOffset);
		
		return world.getBlockState(new BlockPos(xPos, yPos, zPos));
	}
}
