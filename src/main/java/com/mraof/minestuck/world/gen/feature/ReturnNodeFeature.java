package com.mraof.minestuck.world.gen.feature;

import com.mojang.serialization.Codec;
import com.mraof.minestuck.block.ReturnNodeBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

public class ReturnNodeFeature extends Feature<NoneFeatureConfiguration>
{
	public ReturnNodeFeature(Codec<NoneFeatureConfiguration> codec)
	{
		super(codec);
	}
	
	@Override
	public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context)
	{
		WorldGenLevel level = context.level();
		BlockPos pos = context.origin();
		
		for(; !level.isOutsideBuildHeight(pos); pos = pos.above())
		{
			if(isAreaClear(level, pos))
			{
				ReturnNodeBlock.placeReturnNode(level, pos, null);
				return true;
			}
		}
		return false;
	}
	
	private static boolean isAreaClear(LevelAccessor level, BlockPos pos)
	{
		for(int i = 0; i < 4; i++)
		{
			BlockPos blockPos = pos.offset(i % 2, 0, i / 2);
			if(level.getBlockState(blockPos).canOcclude())
				return false;
		}
		return true;
	}
}