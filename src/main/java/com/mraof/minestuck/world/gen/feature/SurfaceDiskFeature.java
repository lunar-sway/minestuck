package com.mraof.minestuck.world.gen.feature;

import com.mojang.datafixers.Dynamic;
import net.minecraft.block.BlockState;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.GenerationSettings;
import net.minecraft.world.gen.feature.BlockBlobConfig;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.SphereReplaceConfig;

import java.util.Iterator;
import java.util.Random;
import java.util.function.Function;

/**
 * A version of the {@link net.minecraft.world.gen.feature.SphereReplaceFeature}, but without the need to be placed in water
 */
public class SurfaceDiskFeature extends Feature<SphereReplaceConfig>
{
	public SurfaceDiskFeature(Function<Dynamic<?>, ? extends SphereReplaceConfig> configFactoryIn)
	{
		super(configFactoryIn);
	}
	
	public boolean place(IWorld worldIn, ChunkGenerator<? extends GenerationSettings> generator, Random rand, BlockPos pos, SphereReplaceConfig config) {
		int i = 0;
		int j = rand.nextInt(config.radius - 2) + 2;
		
		for(int k = pos.getX() - j; k <= pos.getX() + j; ++k) {
			for(int l = pos.getZ() - j; l <= pos.getZ() + j; ++l) {
				int i1 = k - pos.getX();
				int j1 = l - pos.getZ();
				if (i1 * i1 + j1 * j1 <= j * j) {
					for(int k1 = pos.getY() - config.ySize; k1 <= pos.getY() + config.ySize; ++k1) {
						BlockPos blockpos = new BlockPos(k, k1, l);
						BlockPos blockposRand = new BlockPos(k+rand.nextInt(2)-1, k1, l+rand.nextInt(2)-1);
						BlockState blockstate = worldIn.getBlockState(blockpos);
						
						for(BlockState blockstate1 : config.targets) {
							if (blockstate1.getBlock() == blockstate.getBlock()) {
								worldIn.setBlockState(blockpos, config.state, 2);
								worldIn.setBlockState(blockposRand, config.state, 2);
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