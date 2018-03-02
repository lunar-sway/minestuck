package com.mraof.minestuck.world.lands.decorator.structure;

import com.mraof.minestuck.world.lands.decorator.BiomeSpecificDecorator;
import com.mraof.minestuck.world.lands.gen.ChunkProviderLands;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;

import java.util.Random;

public class OasisDecorator extends BiomeSpecificDecorator
{
	public OasisDecorator(Biome... biomes)
	{
		super(biomes);
	}
	
	@Override
	public BlockPos generate(World world, Random random, BlockPos pos, ChunkProviderLands provider)
	{
		boolean[] blocks = new boolean[16*16*4];
		
		int runthroughs = random.nextInt(3) + 4;
		
		for(int i = 0; i < runthroughs; i++)
		{
			double xSize = random.nextDouble() * 5D + 5D;
			double ySize = random.nextDouble() * 4D + 3D;
			double zSize = random.nextDouble() * 5D + 5D;
			double xPos = random.nextDouble() * (16D - xSize - 4D) + 2D + xSize / 2D;
			double yPos = random.nextDouble() * (5D - ySize / 2 - 2D) + 2D + ySize / 2D;
			double zPos = random.nextDouble() * (16D - zSize - 4D) + 2D + zSize / 2D;
			
			for (int x = 2; x < 14; x++)
			{
				for (int z = 2; z < 14; z++)
				{
					for (int y = 1; y < 4; y++)
					{
						double xDiff = (x - xPos) / (xSize / 2D);
						double yDiff = (y - yPos) / (ySize / 2D);
						double zDiff = (z - zPos) / (zSize / 2D);
						double distance = xDiff * xDiff + yDiff * yDiff + zDiff * zDiff;
						if (distance < 1)
							blocks[((x * 16) + z) * 4 + y] = true;
					}
				}
			}
		}
		
		int yMin = 256, yMax = 0;
		for (int x = 1; x < 15; x++)
		{
			for (int z = 1; z < 15; z++)
			{
				if (!blocks[((x * 16) + z) * 4 + 3] && hasBlock1(blocks, x, 3, z, true))
				{
					BlockPos topPos = world.getTopSolidOrLiquidBlock(pos.add(x - 8, 0, z - 8)).down();
					yMin = Math.min(yMin, topPos.getY());
					yMax = Math.max(yMax, topPos.getY());
				}
			}
		}
		
		if (yMax - yMin > 1)
			return null;
		
		pos = pos.up(yMin - pos.getY());
		
		for (int x = 0; x < 16; x++)
		{
			for (int z = 0; z < 16; z++)
			{
				for (int y = 0; y < 4; y++)
				{
					if (blocks[((x * 16) + z) * 4 + y])
						world.setBlockState(pos.add(x - 8, y - 3, z - 8), Blocks.WATER.getDefaultState(), 2);
					else if (!blocks[((x * 16) + z) * 4 + y] && hasBlock1(blocks, x, y, z, false))
						world.setBlockState(pos.add(x - 8, y - 3, z - 8), Blocks.DIRT.getDefaultState(), 2);
				}
				
				if (!blocks[((x * 16) + z) * 4 + 3] && hasBlock2(blocks, x, 3, z))
				{
					world.setBlockState(world.getTopSolidOrLiquidBlock(pos.add(x - 8, 0, z - 8)).down(), Blocks.GRASS.getDefaultState(), 2);
				} else if(blocks[((x * 16) + z) * 4 + 3])
					for(int y = 0; y < 4; y++)
						world.setBlockState(pos.add(x - 8, y + 1, z - 8), Blocks.AIR.getDefaultState(), 2);
			}
		}
		
		return null;
	}
	
	private boolean hasBlock1(boolean[] blocks, int x, int y, int z, boolean horizontal)
	{
		if (x > 0 && blocks[((x - 1) * 16 + z) * 4 + y] || x < 15 && blocks[((x + 1) * 16 + z) * 4 + y]
				|| z > 0 && blocks[(x * 16 + (z - 1)) * 4 + y] || z < 15 && blocks[(x * 16 + (z + 1)) * 4 + y])
			return true;
		else if (!horizontal && (y > 0 && blocks[(x * 16 + z) * 4 + y - 1] || y < 3 && blocks[(x * 16 + z) * 4 + y + 1]))
			return true;
		else return false;
	}
	
	private boolean hasBlock2(boolean[] blocks, int x, int y, int z)
	{
		if (hasBlock1(blocks, x, y, z, true) || x > 0 && hasBlock1(blocks, x - 1, y, z, true) || x < 15 && hasBlock1(blocks, x + 1, y, z, true)
				|| z > 1 && blocks[(x * 16 + (z - 2)) * 4 + y] || z < 14 && blocks[(x * 16 + (z + 2)) * 4 + y])
			return true;
		else return false;
	}
	
	@Override
	public float getPriority()
	{
		return 0.5F;
	}
	
	@Override
	public int getCount(Random random)
	{
		return random.nextFloat() < 0.002F ? 1 : 0;
	}
}