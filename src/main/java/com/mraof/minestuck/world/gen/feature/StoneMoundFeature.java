package com.mraof.minestuck.world.gen.feature;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.BlockStateConfiguration;

public class StoneMoundFeature extends Feature<BlockStateConfiguration>
{
	
	public StoneMoundFeature(Codec<BlockStateConfiguration> codec)
	{
		super(codec);
	}
	
	@Override
	public boolean place(FeaturePlaceContext<BlockStateConfiguration> context)
	{
		WorldGenLevel level = context.level();
		BlockPos pos = context.origin();
		RandomSource rand = context.random();
		
		float randFloat = rand.nextFloat();
		BlockState state;
		if(randFloat >= .95)
		{
			state = Blocks.GRANITE.defaultBlockState();
		} else if(randFloat >= .9)
		{
			state = Blocks.ANDESITE.defaultBlockState();
		} else if(randFloat >= .85)
		{
			state = Blocks.DIORITE.defaultBlockState();
		} else
		{
			state = context.config().state;
		}
		int height = 2 + rand.nextInt(60);
		double width = 1 + height / 2;
		int radius = (int) width * 2;
		double maxJ = width + rand.nextInt(10);
		double maxK = width + rand.nextInt(10);
		
		if(!level.getBlockState(pos.below(15)).isSolid())
			return false;
		
		for(int i = 0; i < height; i++)
		{
			for(int j = 0; j < maxJ; j++)
			{
				for(int k = 0; k < maxK; k++)
				{
					double doubleX = j - maxJ / 2;
					double doubleZ = k - maxK / 2;
					int x = (int) doubleX;
					int z = (int) doubleZ;
					
					if((doubleX * doubleX) + (doubleZ * doubleZ) <= radius)
					{
						setBlock(level, pos.offset(x, i - 10, z), state);
						setBlock(level, pos.offset(x, -i - 10, z), state);
					}
				}
			}
			--radius;
			if(rand.nextBoolean())
				--radius;
		}
		return true;
	}
}