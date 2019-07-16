package com.mraof.minestuck.world.lands.decorator;

import java.util.Random;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;

public class CanopyTreeDecorator extends TreeDecoratorBase
{
	//WorldGenCanopyTree[] treeTypes;
	protected int treeCount;
	
	public CanopyTreeDecorator(BlockState[] trees, BlockState[] leaves, int treeCount, Biome... biomes)
	{
		super(biomes);
		this.treeCount = treeCount;
		/*this.treeTypes = new WorldGenCanopyTree[trees.length];
		for(int i = 0; i < trees.length; i++)
			treeTypes[i] = new WorldGenCanopyTree(false);*/
	}
	
	public CanopyTreeDecorator(BlockState treeType, BlockState leafType, int treeCount, Biome... biomes)
	{
		this(new BlockState[] {treeType}, new BlockState[] {leafType}, treeCount, biomes);
	}
	
	public CanopyTreeDecorator(int treeCount, Biome... biomes)
	{
		this(Blocks.ACACIA_LOG.getDefaultState(), Blocks.ACACIA_LEAVES.getDefaultState(), treeCount, biomes);
	}
	
	@Override
	public int getCount(Random random)
	{
		return random.nextInt(treeCount) + treeCount;
	}
	/*
	@Override
	protected WorldGenAbstractTree getTreeToGenerate(World world, BlockPos pos, Random rand)
	{
		return this.treeTypes[rand.nextInt(treeTypes.length)];
	}*/
}