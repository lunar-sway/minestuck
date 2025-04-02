package com.mraof.minestuck.world.gen.feature;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.material.FluidState;

public class VeilCraterFeature extends Feature<NoneFeatureConfiguration>
{
	public VeilCraterFeature(Codec<NoneFeatureConfiguration> codec)
	{
		super(codec);
	}
	
	@Override
	public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context)
	{
		WorldGenLevel level = context.level();
		BlockPos pos = context.origin();
		RandomSource rand = context.random();
		
		int radius = rand.nextInt(6, 18);
		pos = pos.above(radius / 2); //push the center up so that it is less obvious its a sphere
		
		for(int x = -radius; x < radius; x++)
		{
			for(int y = -radius; y < radius; y++)
			{
				for(int z = -radius; z < radius; z++)
				{
					BlockPos iteratePos = pos.offset(x, y, z);
					
					boolean shouldBeReplaced = !level.isFluidAtPosition(iteratePos, FluidState::isSource) && !level.isEmptyBlock(iteratePos);
					if(iteratePos.closerThan(pos, radius) && shouldBeReplaced && level.isAreaLoaded(iteratePos, 0))
						level.destroyBlock(iteratePos, false);
				}
			}
		}
		
		return true;
	}
}