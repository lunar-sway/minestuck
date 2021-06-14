package com.mraof.minestuck.world.gen.feature;

import com.mojang.serialization.Codec;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.BlockStateFeatureConfig;
import net.minecraft.world.gen.feature.Feature;

import java.util.Random;

public class PillarFeature extends Feature<BlockStateFeatureConfig>
{
	private final boolean large;
	
	public PillarFeature(Codec<BlockStateFeatureConfig> codec, boolean large)
	{
		super(codec);
		this.large = large;
	}
	
	@Override
	public boolean place(ISeedReader world, ChunkGenerator generator, Random rand, BlockPos pos, BlockStateFeatureConfig config)
	{
		BlockState state = config.state;
		int height = 4 + rand.nextInt(4);
		
		if(world.getBlockState(pos.above(height - 1)).getMaterial().isLiquid())
			return false;
		
		boolean size = large && rand.nextFloat() < 0.4;
		
		if(size)
		{
			for(int i = 0; i < height + 3; i++)
			{
				setBlock(world, pos.offset(0, i, 0), state);
				setBlock(world, pos.offset(1, i, 0), state);
				setBlock(world, pos.offset(1, i, 1), state);
				setBlock(world, pos.offset(0, i, 1), state);
			}
		} else
		{
			for(int i = 0; i < height; i++)
				setBlock(world, pos.above(i), state);
		}
		return true;
	}
}