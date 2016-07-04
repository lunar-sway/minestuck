package com.mraof.minestuck.world.lands.decorator.structure;

import java.util.Random;

import com.mraof.minestuck.world.lands.decorator.BiomeSpecificDecorator;
import com.mraof.minestuck.world.lands.gen.ChunkProviderLands;

import net.minecraft.block.state.IBlockState;
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
	
	protected void placeBlocks(World world, IBlockState block, int fromX, int fromY, int fromZ, int toX, int toY, int toZ)
	{
		for(int x = fromX; x <= toX; x++)
			for(int y = fromY; y <= toY; y++)
				for(int z = fromZ; z <= toZ; z++)
					placeBlock(world, block, x, y, z);
	}
	
	protected void placeBlock(World world, IBlockState block, int xOffset, int yOffset, int zOffset)
	{
		int xPos = xCoord + (rotation ? zOffset : xOffset);
		int yPos = yCoord + yOffset;
		int zPos = zCoord + (rotation ? xOffset : zOffset);
		
		world.setBlockState(new BlockPos(xPos, yPos, zPos), block, 2);
	}
	
}
