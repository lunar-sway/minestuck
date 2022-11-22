package com.mraof.minestuck.world.gen.feature;

import com.mojang.serialization.Codec;
import com.mraof.minestuck.block.MSBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

import java.util.Random;

public class ParcelPyxisFeature extends Feature<NoneFeatureConfiguration>
{
	public ParcelPyxisFeature(Codec<NoneFeatureConfiguration> codec)
	{
		super(codec);
	}
	
	@Override
	public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context)
	{
		WorldGenLevel level = context.level();
		BlockPos pos = context.origin();
		Random rand = context.random();
		Rotation rotation = Rotation.getRandom(rand);
		BlockState state = MSBlocks.PARCEL_PYXIS.get().defaultBlockState().rotate(level, pos, rotation);
		
		if(state.canSurvive(level, pos) && !level.getBlockState(pos).getMaterial().isLiquid())
		{
			int randInt = 10 + rand.nextInt(5);
			setBlock(level, pos.above(1), state);
			setBlock(level, pos, MSBlocks.PIPE_INTERSECTION.get().defaultBlockState());
			for(int i = 1; i < randInt; i++)
			{
				setBlock(level, pos.below(i), MSBlocks.PIPE.get().defaultBlockState());
			}
			setBlock(level, pos.below(randInt), MSBlocks.PIPE_INTERSECTION.get().defaultBlockState());
			if(rand.nextBoolean())
			{
				setBlock(level, pos.above(2), MSBlocks.PYXIS_LID.get().defaultBlockState().rotate(level, pos, rotation));
			}
			return true;
		}
		
		return false;
	}
}