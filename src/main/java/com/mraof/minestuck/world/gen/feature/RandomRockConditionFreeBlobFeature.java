package com.mraof.minestuck.world.gen.feature;

import com.mojang.datafixers.Dynamic;
import com.mraof.minestuck.block.MSBlocks;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.GenerationSettings;
import net.minecraft.world.gen.feature.Feature;

import java.util.Iterator;
import java.util.Random;
import java.util.function.Function;

public class RandomRockConditionFreeBlobFeature extends Feature<RandomRockBlockBlobConfig>
{
	public RandomRockConditionFreeBlobFeature(Function<Dynamic<?>, ? extends RandomRockBlockBlobConfig> configFactoryIn)
	{
		super(configFactoryIn);
	}
	
	@Override
	public boolean place(IWorld worldIn, ChunkGenerator<? extends GenerationSettings> generator, Random rand, BlockPos pos, RandomRockBlockBlobConfig config)
	{
		float randFloat = rand.nextFloat();
		BlockState randomStone = Blocks.STONE.getDefaultState();
		if(randFloat >= .95)
		{
			randomStone = Blocks.GRANITE.getDefaultState();
		} else if(randFloat <= .05)
		{
			randomStone = Blocks.ANDESITE.getDefaultState();
		} else if(randFloat <= .55 && randFloat >= .50)
		{
			randomStone = Blocks.DIORITE.getDefaultState();
		} else if(randFloat <= .61 && randFloat >= .56)
		{
			randomStone = Blocks.COBBLESTONE.getDefaultState();
		} else if(randFloat <= .67 && randFloat >= .62)
		{
			randomStone = MSBlocks.CHALK.getDefaultState();
		} else if(randFloat <= .73 && randFloat >= .68)
		{
			randomStone = MSBlocks.BLACK_STONE.getDefaultState();
		} else if(randFloat <= .79 && randFloat >= .74)
		{
			randomStone = MSBlocks.PINK_STONE.getDefaultState();
		}
		
		for(int i1 = 0; i1 < 3; i1++)
		{
			int xSize = config.startRadius + rand.nextInt(2);
			int ySize = config.startRadius + rand.nextInt(2);
			int zSize = config.startRadius + rand.nextInt(2);
			float f = (float) (xSize + ySize + zSize) * 0.333F + 0.5F;
			Iterator iterator = BlockPos.getAllInBox(pos.add(-xSize, -ySize, -zSize), pos.add(xSize, ySize, zSize)).iterator();
			
			while(iterator.hasNext())
			{
				BlockPos blockpos1 = (BlockPos) iterator.next();
				
				if(blockpos1.distanceSq(pos) <= (double) (f * f))
					setBlockState(worldIn, blockpos1, randomStone);
			}
			
			pos = pos.add(-(config.startRadius + 1) + rand.nextInt(2 + config.startRadius * 2), 0 - rand.nextInt(2), -(config.startRadius + 1) + rand.nextInt(2 + config.startRadius * 2));
		}
		
		return true;
	}
}
