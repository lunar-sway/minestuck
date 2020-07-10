package com.mraof.minestuck.world.gen.feature;

import com.mojang.datafixers.Dynamic;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.GenerationSettings;
import net.minecraft.world.gen.feature.BlockBlobConfig;
import net.minecraft.world.gen.feature.Feature;

import java.util.Iterator;
import java.util.Random;
import java.util.function.Function;

/**
 * A version of the {@link net.minecraft.world.gen.feature.BlockBlobFeature}, but without the need to be placed on dirt or stone.
 */
public class ConditionFreeBlobFeature extends Feature<BlockBlobConfig>
{
	public ConditionFreeBlobFeature(Function<Dynamic<?>, ? extends BlockBlobConfig> configFactoryIn)
	{
		super(configFactoryIn);
	}
	
	@Override
	public boolean place(IWorld worldIn, ChunkGenerator<? extends GenerationSettings> generator, Random rand, BlockPos pos, BlockBlobConfig config)
	{
		for (int i1 = 0; i1 < 3; i1++)
		{
			int xSize = config.startRadius + rand.nextInt(2);
			int ySize = config.startRadius + rand.nextInt(2);
			int zSize = config.startRadius + rand.nextInt(2);
			float f = (float)(xSize + ySize + zSize) * 0.333F + 0.5F;
			Iterator iterator = BlockPos.getAllInBox(pos.add(-xSize, -ySize, -zSize), pos.add(xSize, ySize, zSize)).iterator();
			
			while (iterator.hasNext())
			{
				BlockPos blockpos1 = (BlockPos)iterator.next();
				
				if (blockpos1.distanceSq(pos) <= (double)(f * f))
					setBlockState(worldIn, blockpos1, config.state);
			}
			
			pos = pos.add(-(config.startRadius + 1) + rand.nextInt(2 + config.startRadius * 2), 0 - rand.nextInt(2), -(config.startRadius + 1) + rand.nextInt(2 + config.startRadius * 2));
		}
		
		return true;
	}
}
