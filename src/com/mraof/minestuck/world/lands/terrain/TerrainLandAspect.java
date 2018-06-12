package com.mraof.minestuck.world.lands.terrain;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.mraof.minestuck.entity.consort.EnumConsort;
import com.mraof.minestuck.world.lands.ILandAspect;
import com.mraof.minestuck.world.lands.decorator.ILandDecorator;
import com.mraof.minestuck.world.lands.gen.ChunkProviderLands;
import com.mraof.minestuck.world.lands.gen.DefaultTerrainGen;
import com.mraof.minestuck.world.lands.gen.ILandTerrainGen;
import com.mraof.minestuck.world.lands.structure.GateStructurePillar;
import com.mraof.minestuck.world.lands.structure.IGateStructure;
import com.mraof.minestuck.world.lands.structure.MapGenLandStructure;
import com.mraof.minestuck.world.lands.structure.blocks.StructureBlockRegistry;

import net.minecraft.util.math.Vec3d;

public abstract class TerrainLandAspect implements ILandAspect<TerrainLandAspect>
{
	
	public abstract void registerBlocks(StructureBlockRegistry registry);
	
	public abstract List<ILandDecorator> getDecorators();	//TODO Add a Random as parameter
	
	/**
	 *  Returns a integer representing how they land's day cycle will proceed.
	 *  
	 *  0 = Normal day cycle;
	 *  1 = Always day;
	 *  2 = Always night.
	 */
	public abstract int getDayCycleMode();
	
	public abstract Vec3d getFogColor();
	
	public int getWeatherType()
	{
		return -1;
	}
	
	public float getRainfall()
	{
		return 0.5F;
	}
	
	public float getTemperature()
	{
		return 0.7F;
	}
	
	public float getOceanChance()
	{
		return 1/3F;
	}
	
	public float getRoughChance()
	{
		return 1/5F;//getOceanChance()/2 + 1/5F; For if generation by ocean
	}
	
	@Override
	public List<TerrainLandAspect> getVariations()
	{
		ArrayList<TerrainLandAspect> list = new ArrayList<TerrainLandAspect>();
		list.add(this);
		return list;
	}
	
	@Override
	public TerrainLandAspect getPrimaryVariant()
	{
		return this;
	}
	
	public ILandTerrainGen createTerrainGenerator(ChunkProviderLands chunkProvider, Random rand)
	{
		return new DefaultTerrainGen(chunkProvider, rand);
	}
	
	public void modifyStructureList(List<MapGenLandStructure.StructureEntry> list)
	{}
	
	@Override
	public IGateStructure getGateStructure()
	{
		return new GateStructurePillar();
	}
	
	public abstract EnumConsort getConsortType();
}
