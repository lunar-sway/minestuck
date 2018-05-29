package com.mraof.minestuck.world.lands.decorator;

import java.util.Random;

import com.mraof.minestuck.world.gen.feature.WorldGenRainbowTree;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;

public class RainbowTreeDecorator extends TreeDecoratorBase
{
	private int tC; 
	public RainbowTreeDecorator(int treeCount, Biome... biomes)
	{
		super(biomes);
		tC = treeCount;
	}
	
	@Override
	protected WorldGenAbstractTree getTreeToGenerate(World world, BlockPos pos, Random rand)
	{
		return new WorldGenRainbowTree(false);
	}

	@Override
	public int getCount(Random random)
	{
		return tC;
	}
}
