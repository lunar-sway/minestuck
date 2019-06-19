package com.mraof.minestuck.world.lands.decorator;

import java.util.Random;

import com.mraof.minestuck.world.gen.feature.RainbowTree;

import net.minecraft.block.trees.AbstractTree;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;

public class WorldgenTreeDecorator extends TreeDecoratorBase
{
	private int tC; 
	//private AbstractTree tree = new RainbowTree(false);
	private float priority = 0.6F;
	public WorldgenTreeDecorator(int treeCount, AbstractTree tree, float priority, Biome... biomes)
	{
		this(treeCount, tree, biomes);
		this.priority = priority;
	}
	
	public WorldgenTreeDecorator(int treeCount, AbstractTree tree, Biome... biomes)
	{
		super(biomes);
		//this.tree=tree;
		tC = treeCount;
	}
	
	/*@Override
	protected WorldGenAbstractTree getTreeToGenerate(World world, BlockPos pos, Random rand)
	{
		return tree;
	}*/

	@Override
	public int getCount(Random random)
	{
		return tC;
	}
	
	@Override
	public float getPriority()
	{
		return priority;
	}
}
