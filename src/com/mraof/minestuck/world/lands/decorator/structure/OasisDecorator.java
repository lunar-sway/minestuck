package com.mraof.minestuck.world.lands.decorator.structure;

import com.mraof.minestuck.world.lands.decorator.BiomeSpecificDecorator;
import com.mraof.minestuck.world.lands.gen.ChunkProviderLands;
import com.mraof.minestuck.world.lands.structure.blocks.StructureBlockUtil;
import com.mraof.minestuck.world.storage.loot.MinestuckLoot;
import net.minecraft.block.*;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.Heightmap;

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
		
		//Generate water placement
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
		
		//Figure out water level and if the ground is flat enough
		int yMin = 256, yMax = 0;
		for (int x = 1; x < 15; x++)
		{
			for (int z = 1; z < 15; z++)
			{
				if (!blocks[((x * 16) + z) * 4 + 3] && hasBlock1(blocks, x, 3, z, true))
				{
					BlockPos topPos = world.getHeight(Heightmap.Type.WORLD_SURFACE, pos.add(x - 8, 0, z - 8)).down();
					yMin = Math.min(yMin, topPos.getY());
					yMax = Math.max(yMax, topPos.getY());
				}
			}
		}
		
		if (yMax - yMin > 1)
			return null;
		
		pos = pos.up(yMin - pos.getY());
		
		BlockPos treePos = null;
		int blockCount = 0;	//Cool way of 100% picking a position while iterating through and checking alternatives; same as used by dispensers
		//Place blocks that make up the base of the oasis
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
					BlockPos surfacePos = world.getHeight(Heightmap.Type.WORLD_SURFACE, pos.add(x - 8, 0, z - 8));
					world.setBlockState(surfacePos.down(), Blocks.GRASS.getDefaultState(), 2);
					if (random.nextInt(5) == 0)
						world.setBlockState(surfacePos, Blocks.GRASS.getDefaultState(), 2);
					if (hasBlock1(blocks, x, 3, z, true))
					{
						blockCount++;
						if (random.nextInt(blockCount) == 0)
							treePos = surfacePos;
					}
				} else if (blocks[((x * 16) + z) * 4 + 3])
					for (int y = 0; y < 4; y++)
						world.setBlockState(pos.add(x - 8, y + 1, z - 8), Blocks.AIR.getDefaultState(), 2);
			}
		}
		
		if(treePos != null)	//This should never not be the case
		{
			int posX = treePos.getX() + 8 - pos.getX();
			int posZ = treePos.getZ() + 8 - pos.getZ();
			int topX = Math.max(4, Math.min(11, posX - 2 + random.nextInt(3)));
			int topZ = Math.max(4, Math.min(11, posZ - 2 + random.nextInt(3)));
			BlockPos topPos = pos.add(topX - 8, treePos.getY() - pos.getY() + 5 + random.nextInt(2), topZ - 8);
			IBlockState log = Blocks.JUNGLE_LOG.getDefaultState();
			IBlockState leaves = Blocks.JUNGLE_LEAVES.getDefaultState();
			
			BlockPos diff = topPos.subtract(treePos);
			int logChecks = 12;
			for (int i = 0; i <= logChecks; i++)
			{
				BlockPos currentPos = treePos.add(diff.getX() * i / logChecks, diff.getY() * i / logChecks, diff.getZ() * i / logChecks);
				world.setBlockState(currentPos, log, 2);
			}
			
			for (int x = -4; x <= 4; x++)
			{
				for (int z = -4; z <= 4; z++)
				{
					int lowerY = 1;
					if (random.nextDouble() < BlockPos.ORIGIN.getDistance(x, 0, z) / 4)
						lowerY = 0;
					int upperY = Math.min(4, 4 - Math.max(Math.abs(x), Math.abs(z)) + random.nextInt(2));
					if (Math.abs(x) == 4 || Math.abs(z) == 4)
						lowerY -= Math.abs(random.nextInt(4) - random.nextInt(4));
					for (int y = lowerY; y <= upperY; y++)
						world.setBlockState(topPos.add(x, y, z), leaves, 2);
				}
			}
			world.setBlockState(topPos.up(), log, 2);
		}
		
		if(random.nextInt(25) == 0)
		{
			BlockPos chestPos = null;
			for (int y = 1; y < 4 && chestPos == null; y++)
			{
				int count = 0;
				for (int x = 1; x < 15; x++)
				{
					for (int z = 1; z < 15; z++)
						if (blocks[((x * 16) + z) * 4 + y])
						{
							count++;
							if(random.nextInt(count) == 0)
								chestPos = pos.add(x - 8, y - 3, z - 8);
						}
				}
			}
			
			if(chestPos != null)
			{
				//StructureBlockUtil.placeLootChest(chestPos, world, null, EnumFacing.getHorizontal(random.nextInt(4)), MinestuckLoot.BASIC_MEDIUM_CHEST, random);
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