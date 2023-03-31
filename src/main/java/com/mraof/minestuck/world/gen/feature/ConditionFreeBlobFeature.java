package com.mraof.minestuck.world.gen.feature;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.BlockStateConfiguration;

import java.util.Iterator;

/**
 * A version of the {@link net.minecraft.world.level.levelgen.feature.BlockBlobFeature}, but without the need to be placed on dirt or stone.
 */
public class ConditionFreeBlobFeature extends Feature<BlockStateConfiguration>
{
	public ConditionFreeBlobFeature(Codec<BlockStateConfiguration> codec)
	{
		super(codec);
	}
	
	@Override
	public boolean place(FeaturePlaceContext<BlockStateConfiguration> context)
	{
		WorldGenLevel level = context.level();
		BlockPos pos = context.origin();
		RandomSource rand = context.random();
		BlockStateConfiguration config = context.config();
		
		final int startRadius = 0;
		for (int i1 = 0; i1 < 3; i1++)
		{
			int xSize = startRadius + rand.nextInt(2);
			int ySize = startRadius + rand.nextInt(2);
			int zSize = startRadius + rand.nextInt(2);
			float f = (float)(xSize + ySize + zSize) * 0.333F + 0.5F;
			Iterator<BlockPos> iterator = BlockPos.betweenClosedStream(pos.offset(-xSize, -ySize, -zSize), pos.offset(xSize, ySize, zSize)).iterator();
			
			while (iterator.hasNext())
			{
				BlockPos blockpos1 = iterator.next();
				
				if (blockpos1.distSqr(pos) <= (double)(f * f))
					setBlock(level, blockpos1, config.state);
			}
			
			pos = pos.offset(-(startRadius + 1) + rand.nextInt(2 + startRadius * 2), 0 - rand.nextInt(2), -(startRadius + 1) + rand.nextInt(2 + startRadius * 2));
		}
		
		return true;
	}
}
