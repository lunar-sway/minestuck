package com.mraof.minestuck.world.gen.feature;

import com.mojang.datafixers.Dynamic;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.GenerationSettings;
import net.minecraft.world.gen.feature.BlockStateFeatureConfig;
import net.minecraft.world.gen.feature.Feature;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Random;
import java.util.function.Function;

public class StoneMoundFeature extends Feature<BlockStateFeatureConfig>
{
	
	private static final Logger LOGGER = LogManager.getLogger();
	
	public StoneMoundFeature(Function<Dynamic<?>, ? extends BlockStateFeatureConfig> configFactoryIn)
	{
		super(configFactoryIn);
	}
	
	@Override
	public boolean place(IWorld worldIn, ChunkGenerator<? extends GenerationSettings> generator, Random rand, BlockPos pos, BlockStateFeatureConfig config)
	{
		BlockState state = config.state;
		int height = 2 + rand.nextInt(30);
		double width = 1 + height/2;
		int radius = (int) width * 2;
		double maxJ = width + rand.nextInt(5);
		double maxK = width + rand.nextInt(5);
		
		if(worldIn.getBlockState(pos.up(height - 5)).getMaterial().isLiquid())
			return false;
		
		for(int i = 0; i < height; i++)
		{
			for(int j = 0; j < maxJ; j++)
			{
				for(int k = 0; k < maxK; k++)
				{
					//double x = j - maxJ/2;
					//double z = k - maxK/2;
					double doubleX = j - maxJ/2;
					double doubleZ = k - maxK/2;
					int x = (int) doubleX;
					int z = (int) doubleZ;
					
					//LOGGER.debug("height = {}, width = {}, radius = {}, doubleX = {}, doubleZ = {}, x = {}, z = {}", radius, doubleX, doubleZ, x, z);
					
					if((doubleX*doubleX) + (doubleZ*doubleZ) <= radius/* && (x^2) + (z^2) >= -1*radius || (x^2) + (z^2) <= radius/2 || (x^2) + (z^2) <= radius || */){
						setBlockState(worldIn, pos.add(x,i -5, z), state);
					}
					//if(height/(2+i) >= 1)
					
					//setBlockState(worldIn, pos.add(j,i - 10, k), state);
					//setBlockState(worldIn, pos.add(j,i - 10, k + i/(1+j)), state);
					
					//setBlockState(worldIn, pos.add(j,i - 10, k + i/(1+j)), state);
					//int slope = height/(1+j);
					//setBlockState(worldIn, pos.add((j / 5) + rand.nextInt(10)-5,i - 5,(k / 5) + rand.nextInt(10)-5), state);
				}
			}
			--radius;
			if(rand.nextBoolean())
				--radius;
		}
		return true;
	}
}