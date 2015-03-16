package com.mraof.minestuck.world.gen.lands.title;

import java.util.ArrayList;
import java.util.List;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.world.WorldProviderLands;
import com.mraof.minestuck.world.gen.ChunkProviderLands;
import com.mraof.minestuck.world.gen.lands.ILandAspect;
import com.mraof.minestuck.world.gen.lands.terrain.TerrainAspect;

public abstract class TitleAspect implements ILandAspect
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
		return chunkProvider;
	}
	
	@Override
	public List<ILandAspect> getVariations()
	{
		ArrayList<ILandAspect> list = new ArrayList<ILandAspect>();
		list.add(this);
		return list;
	}
	
	@Override
	public ILandAspect getPrimaryVariant()
	{
		return this;
	}
	
}
