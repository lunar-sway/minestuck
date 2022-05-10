package com.mraof.minestuck.world.gen.feature;

import com.mojang.serialization.Codec;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.BlockStateFeatureConfig;
import net.minecraft.world.gen.feature.Feature;

import java.util.Iterator;
import java.util.Random;

/**
 * A version of the {@link net.minecraft.world.gen.feature.BlockBlobFeature}, but without the need to be placed on dirt or stone.
 */
public class ConditionFreeBlobFeature extends Feature<BlockStateFeatureConfig>
{
	public ConditionFreeBlobFeature(Codec<BlockStateFeatureConfig> codec)
	{
		super(codec);
	}
	
	@Override
	public boolean place(ISeedReader world, ChunkGenerator generator, Random rand, BlockPos pos, BlockStateFeatureConfig config)
	{
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
					setBlock(world, blockpos1, config.state);
			}
			
			pos = pos.offset(-(startRadius + 1) + rand.nextInt(2 + startRadius * 2), 0 - rand.nextInt(2), -(startRadius + 1) + rand.nextInt(2 + startRadius * 2));
		}
		
		return true;
	}
}
