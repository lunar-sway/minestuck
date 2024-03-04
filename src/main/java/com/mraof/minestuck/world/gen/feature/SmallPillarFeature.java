package com.mraof.minestuck.world.gen.feature;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.BlockStateConfiguration;

public class SmallPillarFeature extends Feature<BlockStateConfiguration>
{
	public SmallPillarFeature(Codec<BlockStateConfiguration> codec)
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
		
		for(int i = 0; i < height; i++)
			setBlock(level, pos.above(i), state);
		return true;
	}
}