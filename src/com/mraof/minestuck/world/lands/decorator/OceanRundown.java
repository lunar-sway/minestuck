package com.mraof.minestuck.world.lands.decorator;

import com.mraof.minestuck.world.biome.ModBiomes;
import com.mraof.minestuck.world.lands.gen.ChunkProviderLands;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.Heightmap;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class OceanRundown implements ILandDecorator
{
	public float probability;
	public int attempts;
	
	public OceanRundown(float probability, int attempts)
	{
		this.probability = probability;
		this.attempts = attempts;
	}
	
	@Override
	public BlockPos generate(World world, Random random, int chunkX, int chunkZ, ChunkProviderLands provider)
	{
		int x = (chunkX << 4), z = (chunkZ << 4);
		Biome[] biomes = null;//world.getBiomeProvider().getBiomes(null, x, z, 32, 32);
		
		if(random.nextFloat() < probability)
		{
			BlockPos pos1 = null, pos2 = null, pos3 = null;
			for(int i = 0; i < attempts; i++)
			{
				pos1 = new BlockPos(8 + random.nextInt(16), 0, 8 + random.nextInt(16));
				boolean condition = true;
				check: for(int posX = pos1.getX() - 3; posX <= pos1.getX() + 3; posX++)	//Check if the position generated is on land a distance away from ocean
				{
					for(int posZ = pos1.getZ() - 3; posZ <= pos1.getZ() + 3; posZ++)
					{
						if(!biomes[posX + 32 * posZ].equals(ModBiomes.mediumNormal)
								&& !biomes[posX + 32 * posZ].equals(ModBiomes.mediumRough))
						{
							condition = false;
							break check;
						}
					}
				}
				
				if(condition) //Look for ocean and pick pos2 and pos3
				{
					List<BlockPos> oceanPos = new ArrayList<>();
					for(int posX = pos1.getX() - 8; posX < pos1.getX() + 8; posX++)
					{
						for(int posZ = pos1.getZ() - 8; posZ < pos1.getZ() + 8; posZ++)
						{
							if(biomes[posX + posZ * 32].equals(ModBiomes.mediumOcean))
								oceanPos.add(new BlockPos(posX, 0, posZ));
						}
					}
					if(oceanPos.size() < 10)
						continue;
					pos2 = oceanPos.remove(random.nextInt(oceanPos.size()));
					pos3 = oceanPos.get(random.nextInt(oceanPos.size()));
					break;
				}
			}
			
			if(pos2 != null)
			{
				BlockState fluid = provider.blockRegistry.getBlockState("ocean");
				int minX = Math.min(pos1.getX(), Math.min(pos2.getX(), pos3.getX()));
				int maxX = Math.max(pos1.getX(), Math.max(pos2.getX(), pos3.getX()));
				for(int posX = minX; posX <= maxX; posX++)
				{
					int z1, z2;
					if(pos1.getX() == posX)
					{
						z1 = pos1.getZ();
						if(differentSign(pos2.getX() - posX, pos3.getX() - posX))
							z2 = lineposZ(pos2, pos3, posX);
						else z2 = z1;
					} else if(pos2.getX() == posX)
					{
						z1 = pos2.getZ();
						if(differentSign(pos1.getX() - posX, pos3.getX() - posX))
							z2 = lineposZ(pos1, pos3, posX);
						else z2 = z1;
					} else if(pos3.getX() == posX)
					{
						z1 = pos3.getZ();
						if(differentSign(pos2.getX() - posX, pos1.getX() - posX))
							z2 = lineposZ(pos2, pos1, posX);
						else z2 = z1;
					} else if(differentSign(pos1.getX() - posX, pos2.getX() - posX))
					{
						z1 = lineposZ(pos1, pos2, posX);
						if(differentSign(pos1.getX() - posX, pos3.getX() - posX))
							z2 = lineposZ(pos1, pos3, posX);
						else z2 = lineposZ(pos2, pos3, posX);
					} else
					{
						z1 = lineposZ(pos1, pos3, posX);
						z2 = lineposZ(pos2, pos3, posX);
					}
					if(z1 > z2)
					{
						int tempZ = z2;
						z2 = z1;
						z1 = tempZ;
					}
					for(int posZ = z1; posZ <= z2; posZ++)
					{
						BlockPos pos = world.getHeight(Heightmap.Type.WORLD_SURFACE_WG, new BlockPos(posX + x, 0, posZ + z));
						if(!world.getBlockState(pos).getMaterial().isLiquid())
							world.setBlockState(pos.down(), fluid);
					}
				}
			}
		}
		
		
		return null;
	}
	
	private static int lineposZ(BlockPos p1, BlockPos p2, int x)
	{
		return p1.getZ() + (int)((x - p1.getX())*((p2.getZ() - p1.getZ())/(double)(p2.getX() - p1.getX())));
	}
	
	private static boolean differentSign(int a, int b)
	{
		return a < 0 && b > 0 || a > 0 && b < 0;
	}
	
	@Override
	public float getPriority()
	{
		return 1.0F;
	}
}