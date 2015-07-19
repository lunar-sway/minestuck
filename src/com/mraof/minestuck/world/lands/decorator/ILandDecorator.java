package com.mraof.minestuck.world.lands.decorator;

import java.util.Random;

import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

import com.mraof.minestuck.world.lands.gen.ChunkProviderLands;

public interface ILandDecorator
{
	
	/**
	 * This is called when on a chunk being generated. Adds the indicated structure on the map.
	 */
	public BlockPos generate(World world,Random random, int chunkX, int chunkZ, ChunkProviderLands provider);
	
	public float getPriority();
	
}
