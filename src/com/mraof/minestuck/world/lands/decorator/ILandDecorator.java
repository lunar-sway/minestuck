package com.mraof.minestuck.world.lands.decorator;

import java.util.Random;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import com.mraof.minestuck.world.lands.gen.ChunkProviderLands;

public interface ILandDecorator
{
	
	/**
	 * This is called when on a chunk being generated. Adds the indicated structure on the map.
	 */
	BlockPos generate(World world,Random random, int chunkX, int chunkZ, ChunkProviderLands provider);
	
	float getPriority();	//TODO Figure out some sort of guideline for determining what this should return.
	
}
