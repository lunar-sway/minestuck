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
	public boolean generate(ISeedReader worldIn, ChunkGenerator generator, Random rand, BlockPos pos, NoFeatureConfig config)
	{
		Rotation rotation = Rotation.randomRotation(rand);
		BlockState state = MSBlocks.PARCEL_PYXIS.getDefaultState().rotate(worldIn, pos, rotation);
		
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
				setBlockState(worldIn, pos.up(2), MSBlocks.PYXIS_LID.getDefaultState().rotate(worldIn, pos, rotation));
			}
			return true;
		}
		
		return false;
	}
}