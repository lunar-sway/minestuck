package com.mraof.minestuck.world.gen.feature;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.BlockStateConfiguration;

import java.util.Random;

public class PillarFeature extends Feature<BlockStateConfiguration>
{
	private final boolean large;
	
	public PillarFeature(Codec<BlockStateConfiguration> codec, boolean large)
	{
		super(codec);
		this.large = large;
	}
	
	@Override
	public boolean place(FeaturePlaceContext<BlockStateConfiguration> context)
	{
		WorldGenLevel level = context.level();
		BlockPos pos = context.origin();
		Random rand = context.random();
		BlockState state = context.config().state;
		int height = 4 + rand.nextInt(4);
		
		if(level.getBlockState(pos.above(height - 1)).getMaterial().isLiquid())
			return false;
		
		boolean size = large && rand.nextFloat() < 0.4;
		
		if(size)
		{
			for(int i = 0; i < height + 3; i++)
			{
				setBlock(level, pos.offset(0, i, 0), state);
				setBlock(level, pos.offset(1, i, 0), state);
				setBlock(level, pos.offset(1, i, 1), state);
				setBlock(level, pos.offset(0, i, 1), state);
			}
		} else
		{
			for(int i = 0; i < height; i++)
				setBlock(level, pos.above(i), state);
		}
		return true;
	}
}