package com.mraof.minestuck.world.lands.decorator;

import java.util.Random;

import com.mraof.minestuck.block.BlockMinestuckLeaves1;
import com.mraof.minestuck.block.BlockMinestuckLog1;
import com.mraof.minestuck.block.MinestuckBlocks;
import com.mraof.minestuck.world.biome.BiomeMinestuck;

import net.minecraft.block.Block;
import net.minecraft.block.BlockLeaves;
import net.minecraft.block.BlockLog;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;
import net.minecraft.world.gen.feature.WorldGenTaiga1;
import net.minecraft.world.gen.feature.WorldGenTaiga2;

public class SpruceTreeDecorator extends TreeDecoratorBase	//TODO: F1X TH1S
{
	
	protected WorldGenTaiga1 tree1 = new WorldGenTaiga1()
	{
		@Override
		protected boolean canGrowInto(Block block) {return super.canGrowInto(block) || block == Blocks.SNOW_LAYER;}
	};
	
	protected WorldGenTaiga2 tree2 = new WorldGenTaiga2(false)
	{
		@Override
		protected boolean canGrowInto(Block block) {return super.canGrowInto(block) || block == Blocks.SNOW_LAYER;}	
	};
	
	@Override
	public int getCount(Random random)
	{
		return Math.max(0, random.nextInt(10) - 7);
	}
	

	@Override
	protected WorldGenAbstractTree getTreeToGenerate(World world, BlockPos pos, Random rand)
	{
		return rand.nextBoolean() ? tree1 : tree2;
	}

}
