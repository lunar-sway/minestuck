package com.mraof.minestuck.world.lands.decorator;

import java.util.Iterator;
import java.util.Random;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import com.mraof.minestuck.world.biome.BiomeMinestuck;
import com.mraof.minestuck.world.lands.gen.ChunkProviderLands;

public class BlockBlobDecorator implements ILandDecorator	//A version of WorldGenBlockBlob slightly adapted to this usage area
{
	
	private IBlockState block;
	private int size;
	
	public BlockBlobDecorator(IBlockState block, int size)
	{
		this.block = block;
		this.size = size;
	}
	
	@Override
	public BlockPos generate(World world, Random random, int chunkX, int chunkZ, ChunkProviderLands provider)
	{
		for(int i = 0; i < random.nextInt(4); i++)
		{
			BlockPos pos = world.getHeight(new BlockPos((chunkX << 4) + 8 + random.nextInt(16), 0, (chunkZ << 4) + 8 + random.nextInt(16)));
			
			if(world.getBiomeForCoordsBody(pos) == BiomeMinestuck.mediumOcean)
				continue;
			
			for (int i1 = 0; i1 < 3; ++i1)
			{
				int xSize = size + random.nextInt(2);
				int ySize = size + random.nextInt(2);
				int zSize = size + random.nextInt(2);
				float f = (float)(xSize + ySize + zSize) * 0.333F + 0.5F;
				Iterator iterator = BlockPos.getAllInBox(pos.add(-xSize, -ySize, -zSize), pos.add(xSize, ySize, zSize)).iterator();
				
				while (iterator.hasNext())
				{
					BlockPos blockpos1 = (BlockPos)iterator.next();
					
					if (blockpos1.distanceSq(pos) <= (double)(f * f))
						world.setBlockState(blockpos1, block, 2);
				}
				
				pos = pos.add(-(size + 1) + random.nextInt(2 + size * 2), 0 - random.nextInt(2), -(size + 1) + random.nextInt(2 + size * 2));
			}
		}
		
		return null;
	}
	
	@Override
	public float getPriority()
	{
		return 0.7F;
	}
}