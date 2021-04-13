package com.mraof.minestuck.world.gen.feature;

import com.mojang.datafixers.Dynamic;
import com.mraof.minestuck.block.MSBlocks;
import net.minecraft.block.BlockState;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.GenerationSettings;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.NoFeatureConfig;

import java.util.Random;
import java.util.function.Function;

public class ParcelPyxisFeature extends Feature<NoFeatureConfig>
{
	public ParcelPyxisFeature(Function<Dynamic<?>, ? extends NoFeatureConfig> configFactoryIn)
	{
		super(configFactoryIn);
	}
	
	@Override
	public boolean place(IWorld worldIn, ChunkGenerator<? extends GenerationSettings> generator, Random rand, BlockPos pos, NoFeatureConfig config)
	{
		BlockState state = MSBlocks.PARCEL_PYXIS.getDefaultState().rotate(Rotation.randomRotation(rand));
		
		if(state.isValidPosition(worldIn, pos) && !worldIn.getBlockState(pos).getMaterial().isLiquid())
		{
			int randInt = 10 + rand.nextInt(5);
			setBlockState(worldIn, pos.up(1), state);
			setBlockState(worldIn, pos, MSBlocks.PIPE_INTERSECTION.getDefaultState());
			for(int i = 1; i < randInt; i++)
			{
				setBlockState(worldIn, pos.down(i), MSBlocks.PIPE.getDefaultState());
			}
			setBlockState(worldIn, pos.down(randInt), MSBlocks.PIPE_INTERSECTION.getDefaultState());
			if(rand.nextBoolean())
			{
				setBlockState(worldIn, pos.up(2), MSBlocks.PYXIS_LID.getDefaultState().rotate(Rotation.randomRotation(rand)));
			}
			return true;
		}
		
		return false;
	}
}