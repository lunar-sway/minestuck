package com.mraof.minestuck.world;

import java.util.Arrays;

import com.mraof.minestuck.world.biome.GenLayerLands;

import net.minecraft.world.World;
import net.minecraft.world.WorldType;
import net.minecraft.world.biome.WorldChunkManager;
import net.minecraft.world.gen.layer.GenLayer;

public class WorldChunkManagerLands extends WorldChunkManager
{
	private float rainfall;
	
	public WorldChunkManagerLands(World world, float rainfall)
	{
		super(world);
		this.rainfall = rainfall;
	}
	
	@Override
	public float[] getRainfall(float[] listToReuse, int x, int z, int width, int length)
	{
		if (listToReuse == null || listToReuse.length < width * length)
		{
			listToReuse = new float[width * length];
		}
		
		Arrays.fill(listToReuse, 0, width * length, this.rainfall);
		return listToReuse;
	}
	
	@Override
	public GenLayer[] getModdedBiomeGenerators(WorldType worldType, long seed, GenLayer[] original)
	{
		return GenLayerLands.generateBiomeGenLayers(seed);
	}
}