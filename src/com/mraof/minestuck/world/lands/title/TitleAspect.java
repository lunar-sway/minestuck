package com.mraof.minestuck.world.lands.title;

import java.util.ArrayList;
import java.util.List;

import com.mraof.minestuck.world.WorldProviderLands;
import com.mraof.minestuck.world.lands.ILandAspect;
import com.mraof.minestuck.world.lands.gen.ChunkProviderLands;
import com.mraof.minestuck.world.lands.structure.IGateStructure;
import com.mraof.minestuck.world.lands.terrain.TerrainAspect;

public abstract class TitleAspect implements ILandAspect<TitleAspect>
{
	
	protected abstract void prepareChunkProvider(ChunkProviderLands chunkProvider);
	
	public boolean isAspectCompatible(TerrainAspect aspect)
	{
		return true;
	}
	
	public ChunkProviderLands createChunkProvider(WorldProviderLands land)
	{
		ChunkProviderLands chunkProvider = new ChunkProviderLands(land.getWorld(), land, land.getWorld().isRemote);
		prepareChunkProvider(chunkProvider);
		chunkProvider.createBiomeGen();
		return chunkProvider;
	}
	
	@Override
	public List<TitleAspect> getVariations()
	{
		ArrayList<TitleAspect> list = new ArrayList<TitleAspect>();
		list.add(this);
		return list;
	}
	
	@Override
	public TitleAspect getPrimaryVariant()
	{
		return this;
	}
	
	@Override
	public IGateStructure getGateStructure()
	{
		return null;
	}
	
}
