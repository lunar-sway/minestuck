package com.mraof.minestuck.world.gen.feature;

import com.mojang.serialization.Codec;
import com.mraof.minestuck.block.MSBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CakeBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.ProbabilityFeatureConfiguration;

public class CakeFeature extends Feature<ProbabilityFeatureConfiguration>
{
	public CakeFeature(Codec<ProbabilityFeatureConfiguration> codec)
	{
		super(codec);
	}
	
	@Override
	public boolean place(FeaturePlaceContext<ProbabilityFeatureConfiguration> context)
	{
		WorldGenLevel level = context.level();
		BlockPos pos = context.origin();
		RandomSource rand = context.random();
		
		int bites = Math.max(0, (int) (rand.nextDouble()*10) - 6);
		float f = rand.nextFloat();
		Block cake;
		if(f < 0.1F)
		{
			if(rand.nextFloat() < context.config().probability)
				cake = f < 0.05F ? MSBlocks.RED_CAKE.get() : MSBlocks.HOT_CAKE.get();
			else cake = f < 0.05F ? MSBlocks.BLUE_CAKE.get() : MSBlocks.COLD_CAKE.get();
		}
		else if(f < 0.4F)
			cake = MSBlocks.APPLE_CAKE.get();
		else if (f < 0.45F)
			cake = MSBlocks.NEGATIVE_CAKE.get();
		else if (f < 0.5F)
			cake = MSBlocks.CARROT_CAKE.get();
		else if (f < 0.6F)
			cake = MSBlocks.CHOCOLATEY_CAKE.get();
		else if(rand.nextFloat() < 0.01)
			cake = MSBlocks.REVERSE_CAKE.get();
		else
			cake = Blocks.CAKE;
		
		BlockState state = cake.defaultBlockState().setValue(CakeBlock.BITES, bites);
		
		if(!state.canSurvive(level, pos))
			return false;
		
		setBlock(level, pos, state);
		return true;
	}
}
