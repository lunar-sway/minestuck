package com.mraof.minestuck.world.gen.feature;

import com.mojang.serialization.Codec;
import com.mraof.minestuck.block.MSBlocks;
import net.minecraft.block.BlockState;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.NoFeatureConfig;

import java.util.Random;

public class ParcelPyxisFeature extends Feature<NoFeatureConfig>
{
	public ParcelPyxisFeature(Codec<NoFeatureConfig> codec)
	{
		super(codec);
	}
	
	@Override
	public boolean place(ISeedReader worldIn, ChunkGenerator generator, Random rand, BlockPos pos, NoFeatureConfig config)
	{
		Rotation rotation = Rotation.getRandom(rand);
		BlockState state = MSBlocks.PARCEL_PYXIS.defaultBlockState().rotate(worldIn, pos, rotation);
		
		if(state.canSurvive(worldIn, pos) && !worldIn.getBlockState(pos).getMaterial().isLiquid())
		{
			int randInt = 10 + rand.nextInt(5);
			setBlock(worldIn, pos.above(1), state);
			setBlock(worldIn, pos, MSBlocks.PIPE_INTERSECTION.defaultBlockState());
			for(int i = 1; i < randInt; i++)
			{
				setBlock(worldIn, pos.below(i), MSBlocks.PIPE.defaultBlockState());
			}
			setBlock(worldIn, pos.below(randInt), MSBlocks.PIPE_INTERSECTION.defaultBlockState());
			if(rand.nextBoolean())
			{
				setBlock(worldIn, pos.above(2), MSBlocks.PYXIS_LID.defaultBlockState().rotate(worldIn, pos, rotation));
			}
			return true;
		}
		
		return false;
	}
}