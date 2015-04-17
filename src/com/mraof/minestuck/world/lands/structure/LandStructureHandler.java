package com.mraof.minestuck.world.lands.structure;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.mraof.minestuck.util.Debug;
import com.mraof.minestuck.world.lands.gen.ChunkProviderLands;

import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.MapGenStructure;
import net.minecraft.world.gen.structure.StructureStart;

public class LandStructureHandler extends MapGenStructure
{
	
	public final static List<StructureEntry> genericStructures = new ArrayList<StructureEntry>();
	public final List<StructureEntry> structures = new ArrayList<StructureEntry>();
	private float totalRarity;
	
	private final ChunkProviderLands chunkProvider;
	
	public static void registerStructures()
	{
		
	}
	
	public LandStructureHandler(ChunkProviderLands chunkProvider)
	{
		structures.addAll(genericStructures);
		this.chunkProvider = chunkProvider;
		
		chunkProvider.aspect1.modifyStructureList(structures);
	}
	
	private static final int MAX_STRUCTURE_DISTANCE = 7;
	private static final int MIN_STRUCTURE_DISTANCE = 2;
	
	@Override
	protected boolean canSpawnStructureAtCoords(int chunkX, int chunkZ)	//This works very much like the scattered features in the overworld
	{
		int x = chunkX;
		int z = chunkZ;
		
		if (x < 0)
			x -= this.MAX_STRUCTURE_DISTANCE - 1;
		if (z < 0)
			z -= this.MAX_STRUCTURE_DISTANCE - 1;
		
		x /= this.MAX_STRUCTURE_DISTANCE;
		z /= this.MAX_STRUCTURE_DISTANCE;
		Random random = this.worldObj.setRandomSeed(x, z, 59273643^worldObj.provider.getDimensionId());
		x *= this.MAX_STRUCTURE_DISTANCE;
		z *= this.MAX_STRUCTURE_DISTANCE;
		x += random.nextInt(this.MAX_STRUCTURE_DISTANCE - this.MIN_STRUCTURE_DISTANCE);
		z += random.nextInt(this.MAX_STRUCTURE_DISTANCE - this.MIN_STRUCTURE_DISTANCE);
		
		if (chunkX == x && chunkZ == z)
		{
			int[] terrainMap = chunkProvider.terrainGenerator.getHeightMap(chunkX, chunkZ);
			
		}
		
		return false;
	}
	
	@Override
	public String getStructureName()
	{
		return "Basic Land Feature";
	}
	
	@Override
	protected StructureStart getStructureStart(int chunkX, int chunkZ)
	{
		int x = chunkX;
		int z = chunkZ;
		
		if (x < 0)
			x -= this.MAX_STRUCTURE_DISTANCE - 1;
		if (z < 0)
			z -= this.MAX_STRUCTURE_DISTANCE - 1;
		
		x /= this.MAX_STRUCTURE_DISTANCE;
		z /= this.MAX_STRUCTURE_DISTANCE;
		Random rand = worldObj.setRandomSeed(x , z, 34527185^worldObj.provider.getDimensionId());
		
		if(totalRarity == 0)
			for(StructureEntry entry : structures)
				totalRarity += entry.rarity;
		
		float index = rand.nextFloat()*totalRarity;
		
		for(StructureEntry entry : structures)
		{
			if(index < entry.rarity)
				return entry.createInstance(chunkProvider, worldObj, rand, chunkX, chunkZ);
			index -= entry.rarity;
		}
		
		return null;
	}
	
	public static class StructureEntry
	{
		public final Class<StructureStart> structureStart;
		public final float rarity;
		
		public StructureEntry(Class<StructureStart> structure, int rarity)
		{
			this.structureStart = structure;
			this.rarity = rarity;
		}
		
		private StructureStart createInstance(ChunkProviderLands chunkProvider, World world, Random rand, int chunkX, int chunkZ)
		{
			try
			{
				return structureStart.getConstructor(ChunkProviderLands.class, World.class, Random.class, int.class, int.class).newInstance(chunkProvider, world, rand, chunkX, chunkZ);
			}
			catch(Exception e)
			{
				e.printStackTrace();
				Debug.print("Failed to create structure for "+structureStart.getName());
				return null;
			}
		}
	}
	
}
