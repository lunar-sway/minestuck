package com.mraof.minestuck.world.lands.decorator;

import java.util.Random;

import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenCactus;

import com.mraof.minestuck.world.lands.gen.ChunkProviderLands;

public class CactusDecorator implements ILandDecorator
{
	
	private WorldGenCactus cactusGen;
	
	public CactusDecorator()
	{
		cactusGen = new WorldGenCactus();
	}
	
	@Override
	public BlockPos generate(World world, Random random, int chunkX, int chunkZ, ChunkProviderLands provider)
	{
		for(int i = 0; i < 10; i++)
		{
			BlockPos pos = new BlockPos((chunkX << 4) + 8 + random.nextInt(16), random.nextInt(256), (chunkZ << 4) + 8 + random.nextInt(16));
			cactusGen.generate(world, random, pos);
		}
		
		return null;
	}
	
	@Override
	public float getPriority()
	{
		return 0.4F;
	}
}