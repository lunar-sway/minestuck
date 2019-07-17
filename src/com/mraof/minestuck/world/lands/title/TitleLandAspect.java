package com.mraof.minestuck.world.lands.title;

import java.util.ArrayList;
import java.util.List;

import com.mraof.minestuck.world.WorldProviderLands;
import com.mraof.minestuck.world.lands.ILandAspect;
import com.mraof.minestuck.world.lands.gen.ChunkProviderLands;
import com.mraof.minestuck.world.lands.structure.IGateStructure;
import com.mraof.minestuck.world.lands.terrain.TerrainLandAspect;

public abstract class TitleLandAspect implements ILandAspect<TitleLandAspect>
{
	public boolean isAspectCompatible(TerrainLandAspect aspect)
	{
		return true;
	}
	
	public ChunkProviderLands createChunkProvider(WorldProviderLands land)
	{
		ChunkProviderLands chunkProvider = new ChunkProviderLands(land.getWorld(), land, land.getWorld().isRemote);
		TerrainLandAspect terrain = land.landAspects.aspectTerrain;
		
		prepareChunkProvider(chunkProvider);
		terrain.prepareChunkProvider(chunkProvider);
		if(!land.getWorld().isRemote)
		{
			prepareChunkProviderServer(chunkProvider);
			terrain.prepareChunkProviderServer(chunkProvider);
		}
		chunkProvider.createBiomeGen();
		return chunkProvider;
	}
	
	@Override
	public List<TitleLandAspect> getVariations()
	{
		ArrayList<TitleLandAspect> list = new ArrayList<TitleLandAspect>();
		list.add(this);
		return list;
	}
	
	@Override
	public TitleLandAspect getPrimaryVariant()
	{
		return this;
	}
	
	@Override
	public IGateStructure getGateStructure()
	{
		return null;
	}
	
	public void prepareWorldProvider(WorldProviderLands worldProvider)
	{}
	
	@Override
	public void prepareChunkProvider(ChunkProviderLands chunkProvider){}
	@Override
	public void prepareChunkProviderServer(ChunkProviderLands chunkProvider){}
	
}
