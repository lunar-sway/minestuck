package com.mraof.minestuck.world.lands.terrain;

import java.util.ArrayList;
import java.util.List;

import com.mraof.minestuck.entity.consort.ConsortEntity;
import com.mraof.minestuck.world.lands.ILandAspect;
import com.mraof.minestuck.world.lands.decorator.ILandDecorator;
import com.mraof.minestuck.world.lands.gen.ChunkProviderLands;
import com.mraof.minestuck.world.lands.structure.GateStructurePillar;
import com.mraof.minestuck.world.lands.structure.IGateStructure;
import com.mraof.minestuck.world.lands.structure.blocks.StructureBlockRegistry;

import net.minecraft.entity.EntityType;
import net.minecraft.util.math.Vec3d;

public abstract class TerrainLandAspect implements ILandAspect<TerrainLandAspect>
{
	public abstract void registerBlocks(StructureBlockRegistry registry);
	
	@Deprecated
	public List<ILandDecorator> getDecorators(){return null;}
	
	/**
	 * @return
	 */
	public float getSkylightBase()
	{
		return 1F;
	}
	
	public abstract Vec3d getFogColor();
	
	public Vec3d getCloudColor()
	{
		return getFogColor();
	}
	
	public Vec3d getSkyColor()
	{
		return new Vec3d(0, 0, 0);
	}
	
	@Deprecated
	public int getWeatherType()
	{
		return -1;
	}
	
	@Deprecated
	public float getTemperature()
	{
		return 0.7F;
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
	
	//public void modifyStructureList(List<MapGenLandStructure.StructureEntry> list)
	//{}
	
	@Override
	public IGateStructure getGateStructure()
	{
		return new GateStructurePillar();
	}
	
	@Override
	public void prepareChunkProvider(ChunkProviderLands chunkProvider){}
	@Override
	public void prepareChunkProviderServer(ChunkProviderLands chunkProvider){}
	
	public abstract EntityType<? extends ConsortEntity> getConsortType();
	
}