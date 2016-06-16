package com.mraof.minestuck.world.lands.decorator;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;
import net.minecraft.world.gen.feature.WorldGenTaiga1;
import net.minecraft.world.gen.feature.WorldGenTaiga2;

public class SpruceTreeDecorator extends TreeDecoratorBase	//TODO: F1X TH1S
{
	protected WorldGenTaiga1 tree1 = new WorldGenTaiga1()
	{
		@Override
		protected boolean canGrowInto(Block block) {return super.canGrowInto(block) || block == Blocks.snow_layer;}
	};
	protected WorldGenTaiga2 tree2 = new WorldGenTaiga2(false)
	{
		@Override
		protected boolean canGrowInto(Block block) {return super.canGrowInto(block) || block == Blocks.snow_layer;}
	};
	
	@Override
	protected int getTreesPerChunk(Random rand)
	{
		return Math.max(0, rand.nextInt(10) - 7);
	}

	@Override
	protected WorldGenAbstractTree getTreeToGenerate(World world, BlockPos pos, Random rand)
	{
		return rand.nextBoolean() ? tree1 : tree2;
	}

}
