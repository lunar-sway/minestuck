package com.mraof.minestuck.world.lands.decorator;

import java.util.Random;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;

import com.mraof.minestuck.world.lands.gen.ChunkProviderLands;

public abstract class TreeDecoratorBase extends BiomeSpecificDecorator
{
	
	public TreeDecoratorBase(Biome... biomes)
	{
		super(biomes);
	}
	
	@Override
	public BlockPos generate(World world, Random random, BlockPos pos, ChunkProviderLands provider)
	{
		pos = world.getHeight(pos);
		
		WorldGenAbstractTree gen = getTreeToGenerate(world, pos, random);
		if (gen.generate(world, random, pos))
			gen.generateSaplings(world, random, pos);
		
		return null;
	}
	
	protected abstract WorldGenAbstractTree getTreeToGenerate(World world, BlockPos pos, Random rand);
	
	@Override
	public float getPriority()
	{
		return 0.6F;
	}
}