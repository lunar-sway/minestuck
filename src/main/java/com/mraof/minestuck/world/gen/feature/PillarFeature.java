package com.mraof.minestuck.world.gen.feature;

import com.mojang.datafixers.Dynamic;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.GenerationSettings;
import net.minecraft.world.gen.feature.BlockStateFeatureConfig;
import net.minecraft.world.gen.feature.Feature;

import java.util.Random;
import java.util.function.Function;

public class PillarFeature extends Feature<BlockStateFeatureConfig>
{
	private final boolean large;
	
	public PillarFeature(Function<Dynamic<?>, ? extends BlockStateFeatureConfig> configFactoryIn, boolean large)
	{
		super(configFactoryIn);
		this.large = large;
	}
	
	@Override
	public boolean place(IWorld worldIn, ChunkGenerator<? extends GenerationSettings> generator, Random rand, BlockPos pos, BlockStateFeatureConfig config)
	{
		BlockState state = config.state;
		int height = 4 + rand.nextInt(4);
		
		if(worldIn.getBlockState(pos.up(height - 1)).getMaterial().isLiquid())
			return false;
		
		boolean size = large && rand.nextFloat() < 0.4;
		
		if(size)
		{
			for(int i = 0; i < height + 3; i++)
			{
				setBlockState(worldIn, pos.add(0, i, 0), state);
				setBlockState(worldIn, pos.add(1, i, 0), state);
				setBlockState(worldIn, pos.add(1, i, 1), state);
				setBlockState(worldIn, pos.add(0, i, 1), state);
			}
		} else
		{
			for(int i = 0; i < height; i++)
				setBlockState(worldIn, pos.up(i), state);
		}
		return true;
	}
}