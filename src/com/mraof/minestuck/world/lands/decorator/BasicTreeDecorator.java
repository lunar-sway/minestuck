package com.mraof.minestuck.world.lands.decorator;

import java.util.Random;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;

public class BasicTreeDecorator extends TreeDecoratorBase
{
	//WorldGenTrees[] treeTypes;
	protected int treeCount;
	
	public BasicTreeDecorator(BlockState[] trees, BlockState[] leaves, int treeCount, Biome... biomes)
	{
		super(biomes);
		this.treeCount = treeCount;
		/*this.treeTypes = new WorldGenTrees[trees.length];
		for(int i = 0; i < trees.length; i++)
			treeTypes[i] = new WorldGenTrees(false, 5, trees[i], leaves[i], false);*/
	}
	
	public BasicTreeDecorator(BlockState treeType, BlockState leafType, int treeCount, Biome... biomes)
	{
		this(new BlockState[] {treeType}, new BlockState[] {leafType}, treeCount, biomes);
	}
	
	public BasicTreeDecorator(int treeCount, Biome... biomes)
	{
		this(Blocks.OAK_LOG.getDefaultState(), Blocks.OAK_LEAVES.getDefaultState(), treeCount, biomes);
	}
	
	@Override
	public int getCount(Random random)
	{
		return random.nextInt(treeCount) + treeCount;
	}
	
/*	@Override
	protected WorldGenAbstractTree getTreeToGenerate(World world, BlockPos pos, Random rand)
	{
		return this.treeTypes[rand.nextInt(treeTypes.length)];
	}*/
}