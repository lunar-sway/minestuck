package com.mraof.minestuck.world.gen.feature;

import com.mojang.datafixers.Dynamic;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.GenerationSettings;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.NoFeatureConfig;

import java.util.Random;
import java.util.function.Function;

public class SmallLibraryFeature extends Feature<NoFeatureConfig>
{
	public SmallLibraryFeature(Function<Dynamic<?>, ? extends NoFeatureConfig> deserialize)
	{
		super(deserialize);
	}
	
	@Override
	public boolean place(IWorld worldIn, ChunkGenerator<? extends GenerationSettings> generator, Random rand, BlockPos pos, NoFeatureConfig config)
	{
		return false;
	}
}
