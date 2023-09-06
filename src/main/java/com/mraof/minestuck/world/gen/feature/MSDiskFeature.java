package com.mraof.minestuck.world.gen.feature;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.DiskConfiguration;

/**
 * A version of the {@link net.minecraft.world.level.levelgen.feature.DiskFeature}
 * INTRODUCES randomized edges to the feature to look more natural
 * If shouldCheckBlockAbove is true, blocks will only be placed is the block above is non-solid
 */
public class MSDiskFeature extends Feature<DiskConfiguration>
{
	private final boolean shouldCheckBlockAbove;
	
	public MSDiskFeature(Codec<DiskConfiguration> codec, boolean shouldCheckBlockAbove)
	{
		super(codec);
		this.shouldCheckBlockAbove = shouldCheckBlockAbove;
	}
	
	@Override
	public boolean place(FeaturePlaceContext<DiskConfiguration> context)
	{
		WorldGenLevel level = context.level();
		BlockPos featurePos = context.origin();
		DiskConfiguration config = context.config();
		RandomSource rand = context.random();
		
		boolean affectedBlocks = false;
		int radius = config.radius().sample(rand);
		
		for(int x = featurePos.getX() - radius; x <= featurePos.getX() + radius; x++)
		{
			for(int z = featurePos.getZ() - radius; z <= featurePos.getZ() + radius; z++)
			{
				int offsetX = x - featurePos.getX();
				int offsetZ = z - featurePos.getZ();
				if(offsetX * offsetX + offsetZ * offsetZ <= radius * radius)
				{
					for(int y = featurePos.getY() - config.halfHeight(); y <= featurePos.getY() + config.halfHeight(); y++)
					{
						BlockPos pos = new BlockPos(x, y, z);
						BlockPos randPos = new BlockPos(x + rand.nextInt(2) - 1, y, z + rand.nextInt(2) - 1);
						
						affectedBlocks |= tryPlaceBlock(level, rand, pos, config);
						
						affectedBlocks |= !randPos.equals(pos) && tryPlaceBlock(level, rand, randPos, config);
						
					}
				}
			}
		}
		
		return affectedBlocks;
	}
	
	private boolean tryPlaceBlock(WorldGenLevel level, RandomSource random, BlockPos pos, DiskConfiguration config)
	{
		if(!shouldCheckBlockAbove || !level.getBlockState(pos.above(1)).isSolid())	//TODO condition can now be built into the block predicate, I think
		{
			if(config.target().test(level, pos))
			{
				level.setBlock(pos, config.stateProvider().getState(level, random, pos), 2);
				return true;
			}
		}
		return false;
	}
}