package com.mraof.minestuck.world;

import java.util.Arrays;

import com.mraof.minestuck.world.biome.GenLayerLands;

import net.minecraft.world.World;
import net.minecraft.world.WorldType;
import net.minecraft.world.biome.BiomeProvider;
import net.minecraft.world.gen.layer.GenLayer;

public class WorldChunkManagerLands extends BiomeProvider
{
	private float rainfall;
	private GenLayerLands layerLands;
	
	public WorldChunkManagerLands(World world, float rainfall, float oceanChance)
	{
		super(world.getWorldInfo());
		this.rainfall = rainfall;
		layerLands.setOceanChance(oceanChance);
	}
	
	/*@Override
	public float[] getRainfall(float[] listToReuse, int x, int z, int width, int length)
	{
		if (listToReuse == null || listToReuse.length < width * length)
		{
			listToReuse = new float[width * length];
		}
		
		Arrays.fill(listToReuse, 0, width * length, this.rainfall);
		return listToReuse;
	}*/
	
	@Override
	public GenLayer[] getModdedBiomeGenerators(WorldType worldType, long seed, GenLayer[] original)
	{
		GenLayer[] layers = GenLayerLands.generateBiomeGenLayers(seed);
		layerLands = (GenLayerLands) layers[0];
		return new GenLayer[] {layers[1], layers[2]};
	}
}