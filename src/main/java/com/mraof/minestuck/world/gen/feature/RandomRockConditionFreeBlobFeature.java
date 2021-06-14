package com.mraof.minestuck.world.gen.feature;

import com.mojang.serialization.Codec;
import com.mraof.minestuck.block.MSBlocks;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.Feature;

import java.util.Iterator;
import java.util.Random;

public class RandomRockConditionFreeBlobFeature extends Feature<RandomRockBlockBlobConfig>
{
	public RandomRockConditionFreeBlobFeature(Codec<RandomRockBlockBlobConfig> codec)
	{
		super(codec);
	}
	
	@Override
	public boolean place(ISeedReader worldIn, ChunkGenerator generator, Random rand, BlockPos pos, RandomRockBlockBlobConfig config)
	{
		float randFloat = rand.nextFloat();
		BlockState randomStone;
		if(randFloat >= .95)
		{
			randomStone = Blocks.GRANITE.defaultBlockState();
		} else if(randFloat >= .9)
		{
			randomStone = Blocks.ANDESITE.defaultBlockState();
		} else if(randFloat >= .85)
		{
			randomStone = Blocks.DIORITE.defaultBlockState();
		} else if(randFloat >= .8)
		{
			randomStone = Blocks.COBBLESTONE.defaultBlockState();
		} else if(randFloat >= .75)
		{
			randomStone = MSBlocks.CHALK.defaultBlockState();
		} else if(randFloat >= .7)
		{
			randomStone = MSBlocks.BLACK_STONE.defaultBlockState();
		} else if(randFloat >= .65)
		{
			randomStone = MSBlocks.SHADE_STONE.defaultBlockState();
		} else if(randFloat >= .6)
		{
			randomStone = MSBlocks.PINK_STONE.defaultBlockState();
		} else if(randFloat >= .55)
		{
			randomStone = MSBlocks.BROWN_STONE.defaultBlockState();
		} else
		{
			randomStone = Blocks.STONE.defaultBlockState();
		}
		
		for(int i1 = 0; i1 < 3; i1++)
		{
			int xSize = config.startRadius + rand.nextInt(2);
			int ySize = config.startRadius + rand.nextInt(2);
			int zSize = config.startRadius + rand.nextInt(2);
			float f = (float) (xSize + ySize + zSize) * 0.333F + 0.5F;
			Iterator iterator = BlockPos.betweenClosedStream(pos.offset(-xSize, -ySize, -zSize), pos.offset(xSize, ySize, zSize)).iterator();
			
			while(iterator.hasNext())
			{
				BlockPos blockpos1 = (BlockPos) iterator.next();
				
				if(blockpos1.distSqr(pos) <= (double) (f * f))
					setBlock(worldIn, blockpos1, randomStone);
			}
			
			pos = pos.offset(-(config.startRadius + 1) + rand.nextInt(2 + config.startRadius * 2), 0 - rand.nextInt(2), -(config.startRadius + 1) + rand.nextInt(2 + config.startRadius * 2));
		}
		
		return true;
	}
}
