package com.mraof.minestuck.world.lands.structure;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import com.mraof.minestuck.world.biome.BiomeMinestuck;
import com.mraof.minestuck.world.lands.gen.ChunkProviderLands;

import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.structure.MapGenStructure;
import net.minecraft.world.gen.structure.StructureComponent;
import net.minecraft.world.gen.structure.StructureStart;

public class MapGenConsortVillage extends MapGenStructure
{
	private static final List<Biome> BIOMES = Arrays.asList(BiomeMinestuck.mediumNormal);
	
	private static final int VILLAGE_DISTANCE = 24;
	private static final int MIN_VILLAGE_DISTANCE = 5;
	
	private final ChunkProviderLands chunkProvider;
	
	public MapGenConsortVillage(ChunkProviderLands chunkProvider)
	{
		this.chunkProvider = chunkProvider;
	}
	
	@Override
	public String getStructureName()
	{
		return "ConsortVillage";
	}
	
	@Override
	protected boolean canSpawnStructureAtCoords(int chunkX, int chunkZ)
	{
		int i = chunkX;
		int j = chunkZ;
		
		if(chunkX < 0)
		{
			chunkX -= VILLAGE_DISTANCE - 1;
		}
		
		if(chunkZ < 0)
		{
			chunkZ -= VILLAGE_DISTANCE - 1;
		}
		
		int k = chunkX / VILLAGE_DISTANCE;
		int l = chunkZ / VILLAGE_DISTANCE;
		Random random = this.world.setRandomSeed(k, l, 10387312);
		k = k * VILLAGE_DISTANCE;
		l = l * VILLAGE_DISTANCE;
		k = k + random.nextInt(VILLAGE_DISTANCE - MIN_VILLAGE_DISTANCE);
		l = l + random.nextInt(VILLAGE_DISTANCE - MIN_VILLAGE_DISTANCE);
		
		if(i == k && j == l)
		{
			boolean flag = this.world.getBiomeProvider().areBiomesViable(i * 16 + 8, j * 16 + 8, 0, BIOMES);
			if(flag)
			{
				return true;
			}
		}
		return false;
	}
	
	@Override
	protected StructureStart getStructureStart(int chunkX, int chunkZ)
	{
		return new Start(chunkProvider, world, this.rand, chunkX, chunkZ);
	}
	
	
	public static class Start extends StructureStart
	{
		public Start(ChunkProviderLands provider, World world, Random rand, int chunkX, int chunkZ)
		{
			super(chunkX, chunkZ);
			components.add(getVillageStart(provider));
		}
	}
	
	private static StructureComponent getVillageStart(ChunkProviderLands provider)
	{
		return null;
	}
}