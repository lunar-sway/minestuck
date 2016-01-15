package com.mraof.minestuck.world.lands.decorator;

import java.util.Random;

import net.minecraft.block.BlockPlanks;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;
import net.minecraft.world.gen.feature.WorldGenTrees;

public class BasicTreeDecorator extends TreeDecoratorBase
{
	WorldGenTrees[] treeTypes;
	
	public BasicTreeDecorator(int[] trees)
	{
		this.treeTypes = new WorldGenTrees[trees.length];
		for(int i = 0; i < trees.length; i++)
			treeTypes[i] = new WorldGenTrees(false, 5, trees[i], trees[i], false);
	}
	
	public BasicTreeDecorator(int treeType)
	{
		this(new int[] {treeType});
	}
	
	public BasicTreeDecorator()
	{
		this(BlockPlanks.EnumType.OAK.getMetadata());
	}
	
	@Override
	protected int getTreesPerChunk(Random rand)
	{
		return rand.nextInt(5) + 5;
	}
	
	@Override
	protected WorldGenAbstractTree getTreeToGenerate(World world, BlockPos pos, Random rand)
	{
		return this.treeTypes[rand.nextInt(treeTypes.length)];
	}
}