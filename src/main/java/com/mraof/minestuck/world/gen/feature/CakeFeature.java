package com.mraof.minestuck.world.gen.feature;

import com.mojang.serialization.Codec;
import com.mraof.minestuck.block.MSBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.CakeBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.ProbabilityConfig;

import java.util.Random;

public class CakeFeature extends Feature<ProbabilityConfig>
{
	public CakeFeature(Codec<ProbabilityConfig> codec)
	{
		super(codec);
	}
	
	@Override
	public boolean place(ISeedReader world, ChunkGenerator generator, Random rand, BlockPos pos, ProbabilityConfig config)
	{
		int bites = Math.max(0, (int) (rand.nextDouble()*10) - 6);
		float f = rand.nextFloat();
		Block cake;
		if(f < 0.1F)
		{
			if(rand.nextFloat() < config.probability)
				cake = f < 0.05F ? MSBlocks.RED_CAKE : MSBlocks.HOT_CAKE;
			else cake = f < 0.05F ? MSBlocks.BLUE_CAKE : MSBlocks.COLD_CAKE;
		}
		else if(f < 0.4F)
			cake = MSBlocks.APPLE_CAKE;
		else if (f < 0.45F)
			cake = MSBlocks.NEGATIVE_CAKE;
		else if (f < 0.5F)
			cake = MSBlocks.CARROT_CAKE;
		else if(rand.nextFloat() < 0.01)
			cake = MSBlocks.REVERSE_CAKE;
		else
			cake = Blocks.CAKE;
		
		BlockState state = cake.defaultBlockState().setValue(CakeBlock.BITES, bites);
		
		if(state.canSurvive(world, pos) && !world.getBlockState(pos).getMaterial().isLiquid())
		{
			setBlock(world, pos, state);
			return true;
		}
		
		return false;
	}
}