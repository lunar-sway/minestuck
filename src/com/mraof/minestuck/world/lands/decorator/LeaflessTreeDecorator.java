package com.mraof.minestuck.world.lands.decorator;

import java.util.Random;

import com.mraof.minestuck.world.lands.gen.ChunkProviderLands;

import net.minecraft.block.BlockLog;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.Heightmap;

public class LeaflessTreeDecorator extends BiomeSpecificDecorator
{
	public IBlockState logState;
	public float count;
	
	public LeaflessTreeDecorator(IBlockState logState, float count, Biome... biomes)
	{
		super(biomes);
		this.logState = logState;
		this.count = count;
	}
	
	@Override
	public float getPriority()
	{
		return 0.6F;
	}
	
	@Override
	public BlockPos generate(World world, Random random, BlockPos pos, ChunkProviderLands provider)
	{
		pos = world.getHeight(Heightmap.Type.WORLD_SURFACE, pos);
		if(!provider.blockRegistry.getBlockState("surface").equals(world.getBlockState(pos.down())))
			return null;
		
		int size = random.nextInt(3);
		int height = 4 + size;
		
		int min = size < 2 ? 1 : 2;
		
		int count = size + (random.nextBoolean() ? 2 : 3);
		
		for(int i = 0; i < count; i++)
		{
			int h = min + random.nextInt(height - min);
			float modifier = (h+3)*0.2F;
			int xOffset = Math.round((random.nextFloat() - random.nextFloat())*4*modifier);
			int yOffset = h + Math.round(random.nextFloat()*2*modifier);
			int zOffset = Math.round((random.nextFloat() - random.nextFloat())*4*modifier);
			
			genBranch(pos.up(h), pos.add(xOffset, yOffset, zOffset), world);
		}
		
		genBranch(pos, pos.up(height), world);
		
		return null;
	}
	
	protected void genBranch(BlockPos pos0, BlockPos pos1, World world)
	{
		final int xDiff = pos1.getX() - pos0.getX();
		final int yDiff = pos1.getY() - pos0.getY();
		final int zDiff = pos1.getZ() - pos0.getZ();
		final int xLength = Math.abs(xDiff);
		final int yLength = Math.abs(yDiff);
		final int zLength = Math.abs(zDiff);
		
		int length;
		/*EnumAxis axis;
		if(xLength >= yLength && xLength >= zLength)
		{
			length = xLength;
			axis = EnumAxis.X;
		} else if(yLength >= xLength && yLength >= zLength)
		{
			length = yLength;
			axis = EnumAxis.Y;
		} else
		{
			length = zLength;
			axis = EnumAxis.Z;
		}
		
		IBlockState state = logState.withProperty(BlockLog.LOG_AXIS, axis);
		
		for(int i = 0; i < length; i++)
		{
			float f = i/(float) (length);
			BlockPos pos = pos0.add(xDiff*f, yDiff*f, zDiff*f);
			world.setBlockState(pos, state, 2);
		}*/
	}
	
	@Override
	public int getCount(Random random)
	{
		int i = (int) count;
		return i + (random.nextFloat() < (count - i) ? 1 : 0);
	}
}