package com.mraof.minestuck.world.gen.feature;

import com.mojang.serialization.Codec;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.SphereReplaceConfig;

import java.util.Random;

/**
 * A version of the {@link net.minecraft.world.gen.feature.SphereReplaceFeature}, but without the need to be placed in water
 * INTRODUCES randomized edges to the feature to look more natural
 * If shouldCheckBlockAbove is true, blocks will only be placed is the block above is non-solid
 */
public class SurfaceDiskFeature extends Feature<SphereReplaceConfig>
{
	private final boolean shouldCheckBlockAbove;
	
	public SurfaceDiskFeature(Codec<SphereReplaceConfig> codec, boolean shouldCheckBlockAbove)
	{
		super(codec);
		this.shouldCheckBlockAbove = shouldCheckBlockAbove;
	}
	
	@Override
	public boolean place(ISeedReader worldIn, ChunkGenerator generator, Random rand, BlockPos featurePos, SphereReplaceConfig config)
	{
		boolean affectedBlocks = false;
		int radius = config.radius.sample(rand);
		
		for(int x = featurePos.getX() - radius; x <= featurePos.getX() + radius; x++)
		{
			for(int z = featurePos.getZ() - radius; z <= featurePos.getZ() + radius; z++)
			{
				int offsetX = x - featurePos.getX();
				int offsetZ = z - featurePos.getZ();
				if(offsetX * offsetX + offsetZ * offsetZ <= radius * radius)
				{
					for(int y = featurePos.getY() - config.halfHeight; y <= featurePos.getY() + config.halfHeight; y++)
					{
						BlockPos pos = new BlockPos(x, y, z);
						BlockPos randPos = new BlockPos(x + rand.nextInt(2) - 1, y, z + rand.nextInt(2) - 1);
						
						affectedBlocks |= tryPlaceBlock(worldIn, pos, config);
						
						affectedBlocks |= !randPos.equals(pos) && tryPlaceBlock(worldIn, randPos, config);
						
					}
				}
			}
		}
		
		return affectedBlocks;
	}
	
	private boolean tryPlaceBlock(IWorld world, BlockPos pos, SphereReplaceConfig config)
	{
		if(!shouldCheckBlockAbove || !world.getBlockState(pos.above(1)).getMaterial().isSolid())
		{
			BlockState existingState = world.getBlockState(pos);
			
			for(BlockState targetState : config.targets)
			{
				if(targetState.getBlock() == existingState.getBlock())
				{
					world.setBlock(pos, config.state, 2);
					return true;
				}
			}
		}
		return false;
	}
}