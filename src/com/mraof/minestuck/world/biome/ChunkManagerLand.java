package com.mraof.minestuck.world.biome;

import net.minecraft.util.BlockPos;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.biome.WorldChunkManagerHell;

public class ChunkManagerLand extends WorldChunkManagerHell
{
	
	public ChunkManagerLand(BiomeGenBase biome, float rain)
	{
		super(biome, rain);
	}
	
	@Override
	public BiomeGenBase func_180300_a(BlockPos p_180300_1_, BiomeGenBase p_180300_2_)
	{
		BiomeGenBase.getBiomeGenArray()[255] = getBiomeGenerator(null);
		return getBiomeGenerator(null);	//I hope this will work...
	}
	
}
