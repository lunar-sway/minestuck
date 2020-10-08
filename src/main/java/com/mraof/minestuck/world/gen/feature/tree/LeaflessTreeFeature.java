package com.mraof.minestuck.world.gen.feature.tree;

import com.mojang.serialization.Codec;
import net.minecraft.block.BlockState;
import net.minecraft.block.RotatedPillarBlock;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.BlockStateFeatureConfig;
import net.minecraft.world.gen.feature.Feature;

import java.util.Random;

public class LeaflessTreeFeature extends Feature<BlockStateFeatureConfig>
{
	public LeaflessTreeFeature(Codec<BlockStateFeatureConfig> codec)
	{
		super(codec);
	}
	
	@Override
	public boolean func_241855_a(ISeedReader world, ChunkGenerator generator, Random rand, BlockPos pos, BlockStateFeatureConfig config)
	{
		//TODO Define which blocks that it is allowed to place on
		int size = rand.nextInt(3);
		int height = 4 + size;
		
		int min = size < 2 ? 1 : 2;
		
		int count = size + (rand.nextBoolean() ? 2 : 3);
		
		for(int i = 0; i < count; i++)
		{
			int h = min + rand.nextInt(height - min);
			float modifier = (h+3)*0.2F;
			int xOffset = Math.round((rand.nextFloat() - rand.nextFloat())*4*modifier);
			int yOffset = h + Math.round(rand.nextFloat()*2*modifier);
			int zOffset = Math.round((rand.nextFloat() - rand.nextFloat())*4*modifier);
			
			genBranch(pos.up(h), pos.add(xOffset, yOffset, zOffset), world, config.state);
		}
		
		genBranch(pos, pos.up(height), world, config.state);
		
		return true;
	}
	
	protected void genBranch(BlockPos pos0, BlockPos pos1, IWorld world, BlockState logState)
	{
		final int xDiff = pos1.getX() - pos0.getX();
		final int yDiff = pos1.getY() - pos0.getY();
		final int zDiff = pos1.getZ() - pos0.getZ();
		final int xLength = Math.abs(xDiff);
		final int yLength = Math.abs(yDiff);
		final int zLength = Math.abs(zDiff);
		
		int length;
		Direction.Axis axis;
		if(xLength >= yLength && xLength >= zLength)
		{
			length = xLength;
			axis = Direction.Axis.X;
		} else if(yLength >= xLength && yLength >= zLength)
		{
			length = yLength;
			axis = Direction.Axis.Y;
		} else
		{
			length = zLength;
			axis = Direction.Axis.Z;
		}
		
		BlockState state = logState.with(RotatedPillarBlock.AXIS, axis);
		
		for(int i = 0; i < length; i++)
		{
			float f = i/(float) (length);
			BlockPos pos = pos0.add(xDiff*f, yDiff*f, zDiff*f);
			if(world.hasBlockState(pos, (blockState) -> blockState.canBeReplacedByLogs(world, pos)))
				setBlockState(world, pos, state);
			else return;
		}
	}
}