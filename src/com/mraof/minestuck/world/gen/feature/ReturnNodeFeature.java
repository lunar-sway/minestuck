package com.mraof.minestuck.world.gen.feature;

import com.mojang.datafixers.Dynamic;
import com.mraof.minestuck.block.ReturnNodeBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.GenerationSettings;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.NoFeatureConfig;

import java.util.Random;
import java.util.function.Function;

public class ReturnNodeFeature extends Feature<NoFeatureConfig>
{
	ReturnNodeFeature(Function<Dynamic<?>, ? extends NoFeatureConfig> configFactoryIn)
	{
		super(configFactoryIn);
	}
	
	@Override
	public boolean place(IWorld worldIn, ChunkGenerator<? extends GenerationSettings> generator, Random rand, BlockPos pos, NoFeatureConfig config)
	{
		for(; !World.isOutsideBuildHeight(pos); pos = pos.up())
		{
			if(isAreaClear(worldIn, pos))
			{
				ReturnNodeBlock.placeReturnNode(worldIn, pos, null);
				return true;
			}
		}
		return false;
	}
	
	private static boolean isAreaClear(IWorld world, BlockPos pos)
	{
		for(int i = 0; i < 4; i++)
		{
			BlockPos blockPos = pos.add(i % 2, 0, i / 2);
			if(world.getBlockState(blockPos).isSolid())
				return false;
		}
		return true;
	}
}