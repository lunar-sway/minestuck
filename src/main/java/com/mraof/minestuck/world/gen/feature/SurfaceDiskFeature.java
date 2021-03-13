package com.mraof.minestuck.world.gen.feature;

import com.mojang.datafixers.Dynamic;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.GenerationSettings;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.SphereReplaceConfig;

import java.util.Random;
import java.util.function.Function;

/**
 * A version of the {@link net.minecraft.world.gen.feature.SphereReplaceFeature}, but without the need to be placed in water
 * INTRODUCES randomized edges to the feature to look more natural
 */
public class SurfaceDiskFeature extends Feature<SphereReplaceConfig>
{
	private final boolean shouldCheckBlockAbove;
	
	public SurfaceDiskFeature(Function<Dynamic<?>, ? extends SphereReplaceConfig> configFactoryIn, boolean shouldCheckBlockAbove)
	{
		super(configFactoryIn);
		this.shouldCheckBlockAbove = shouldCheckBlockAbove;
	}
	
	public boolean place(IWorld worldIn, ChunkGenerator<? extends GenerationSettings> generator, Random rand, BlockPos pos, SphereReplaceConfig config)
	{
		int i = 0;
		int j = rand.nextInt(config.radius - 2) + 2;
		
		for(int k = pos.getX() - j; k <= pos.getX() + j; ++k)
		{
			for(int l = pos.getZ() - j; l <= pos.getZ() + j; ++l)
			{
				int circleMathX = k - pos.getX();
				int circleMathZ = l - pos.getZ();
				if(circleMathX * circleMathX + circleMathZ * circleMathZ <= j * j)
				{
					for(int configuredY = pos.getY() - config.ySize; configuredY <= pos.getY() + config.ySize; ++configuredY)
					{
						BlockPos blockpos = new BlockPos(k, configuredY, l);
						BlockPos blockposRand = new BlockPos(k + rand.nextInt(2) - 1, configuredY, l + rand.nextInt(2) - 1);
						BlockState posBlockState = worldIn.getBlockState(blockpos);
						
						for(BlockState configBlockState : config.targets)
						{
							if(configBlockState.getBlock() == posBlockState.getBlock())
							{
								boolean blockBelowIsNewBlock = worldIn.getBlockState(blockpos.down(1)) != config.state;
								boolean blockBelowIsNewRandomBlock = worldIn.getBlockState(blockposRand.down(1)) != config.state;
								if(shouldCheckBlockAbove)
								{
									if(!worldIn.getBlockState(blockpos.up(1)).getMaterial().isSolid())
									{
										worldIn.setBlockState(blockpos, config.state, 2);
										if(blockBelowIsNewBlock)
											worldIn.setBlockState(blockpos.down(1), config.targets.get(0), 2);
									}
									if(!worldIn.getBlockState(blockposRand.up(1)).getMaterial().isSolid() && configBlockState.getBlock() == worldIn.getBlockState(blockposRand).getBlock())
									{
										worldIn.setBlockState(blockposRand, config.state, 2);
										if(blockBelowIsNewRandomBlock)
											worldIn.setBlockState(blockposRand.down(1), config.targets.get(0), 2);
									}
								} else
								{
									worldIn.setBlockState(blockpos, config.state, 2);
									if(configBlockState.getBlock() == worldIn.getBlockState(blockposRand).getBlock())
										worldIn.setBlockState(blockposRand, config.state, 2);
								}
								
								++i;
								break;
							}
						}
					}
				}
			}
		}
		
		return i > 0;
	}
}