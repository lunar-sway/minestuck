package com.mraof.minestuck.world.gen.feature;

import com.mojang.datafixers.Dynamic;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.GenerationSettings;
import net.minecraft.world.gen.feature.BlockStateFeatureConfig;
import net.minecraft.world.gen.feature.Feature;

import java.util.Random;
import java.util.function.Function;

public class StoneMoundFeature extends Feature<BlockStateFeatureConfig>
{
	
	public StoneMoundFeature(Function<Dynamic<?>, ? extends BlockStateFeatureConfig> configFactoryIn)
	{
		super(configFactoryIn);
	}
	
	@Override
	public boolean place(IWorld worldIn, ChunkGenerator<? extends GenerationSettings> generator, Random rand, BlockPos pos, BlockStateFeatureConfig config)
	{
		float randFloat = rand.nextFloat();
		BlockState state;
		if(randFloat >= .95)
		{
			state = Blocks.GRANITE.getDefaultState();
		} else if(randFloat >= .9)
		{
			state = Blocks.ANDESITE.getDefaultState();
		} else if(randFloat >= .85)
		{
			state = Blocks.DIORITE.getDefaultState();
		} else
		{
			state = config.state;
		}
		int height = 2 + rand.nextInt(60);
		double width = 1 + height / 2;
		int radius = (int) width * 2;
		double maxJ = width + rand.nextInt(10);
		double maxK = width + rand.nextInt(10);
		
		if(!worldIn.getBlockState(pos.down(15)).getMaterial().isSolid())
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
						setBlockState(worldIn, pos.add(x, i - 10, z), state);
						setBlockState(worldIn, pos.add(x, -i - 10, z), state);
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