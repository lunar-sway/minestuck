package com.mraof.minestuck.world.gen.feature;

import com.mojang.datafixers.Dynamic;
import com.mraof.minestuck.block.MSBlocks;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.GenerationSettings;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.ProbabilityConfig;

import java.util.Random;
import java.util.function.Function;

public class ParcelPyxisFeature extends Feature<ProbabilityConfig>
{
	public ParcelPyxisFeature(Function<Dynamic<?>, ? extends ProbabilityConfig> configFactoryIn)
	{
		super(configFactoryIn);
	}
	
	@Override
	public boolean place(IWorld worldIn, ChunkGenerator<? extends GenerationSettings> generator, Random rand, BlockPos pos, ProbabilityConfig config)
	{
		BlockState state = MSBlocks.PARCEL_PYXIS.getDefaultState();
		
		if(state.isValidPosition(worldIn, pos) && !worldIn.getBlockState(pos).getMaterial().isLiquid())
		{
			setBlockState(worldIn, pos, state);
			if(rand.nextBoolean())
				setBlockState(worldIn, pos.up(1), MSBlocks.PYXIS_LID.getDefaultState());
			for(int i = 0; i < rand.nextInt(20); i++){
				setBlockState(worldIn, pos.down(i), MSBlocks.PIPE.getDefaultState());
			}
			return true;
		}
		
		return false;
	}
}