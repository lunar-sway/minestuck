package com.mraof.minestuck.world.gen.feature;

import com.mojang.datafixers.Dynamic;
import com.mraof.minestuck.block.MSBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.CakeBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.GenerationSettings;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.ProbabilityConfig;

import java.util.Random;
import java.util.function.Function;

public class CakeFeature extends Feature<ProbabilityConfig>
{
	public CakeFeature(Function<Dynamic<?>, ? extends ProbabilityConfig> configFactoryIn)
	{
		super(configFactoryIn);
	}
	
	@Override
	public boolean place(IWorld worldIn, ChunkGenerator<? extends GenerationSettings> generator, Random rand, BlockPos pos, ProbabilityConfig config)
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
		else if(rand.nextFloat() < 0.01)
			cake = MSBlocks.REVERSE_CAKE;
		else
			cake = Blocks.CAKE;
		
		BlockState state = cake.getDefaultState().with(CakeBlock.BITES, bites);
		
		if(state.isValidPosition(worldIn, pos) && !worldIn.getBlockState(pos).getMaterial().isLiquid())
		{
			setBlockState(worldIn, pos, state);
			return true;
		}
		
		return false;
	}
}