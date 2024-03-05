package com.mraof.minestuck.world.gen.feature;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.BlockStateConfiguration;

public class PillarFeature extends Feature<BlockStateConfiguration>
{
	public PillarFeature(Codec<BlockStateConfiguration> codec)
	{
		super(codec);
	}
	
	@Override
	public boolean place(FeaturePlaceContext<BlockStateConfiguration> context)
	{
		WorldGenLevel level = context.level();
		BlockPos pos = context.origin();
		RandomSource rand = context.random();
		BlockState state = context.config().state;
		int height = 4 + rand.nextInt(4);
		
		if(!level.getFluidState(pos.above(height - 1)).isEmpty())
			return false;
		
		for(int i = 0; i < height + 3; i++)
		{
			setBlock(level, pos.offset(0, i, 0), state);
			setBlock(level, pos.offset(1, i, 0), state);
			setBlock(level, pos.offset(1, i, 1), state);
			setBlock(level, pos.offset(0, i, 1), state);
		}
		return true;
	}
}