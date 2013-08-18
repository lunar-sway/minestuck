package com.mraof.minestuck.world.gen.lands;

import java.util.Random;

import com.mraof.minestuck.world.gen.ChunkProviderLands;

import net.minecraft.world.World;

public interface ILandDecorator {
	/**
	 * This is called when on a chunk being generated. Adds the indicated structure on the map.
	 */
	public void generate(World world,Random random, int chunkX, int chunkZ, ChunkProviderLands provider);
}
