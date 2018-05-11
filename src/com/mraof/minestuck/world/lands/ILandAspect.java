package com.mraof.minestuck.world.lands;

import java.util.List;

import com.mraof.minestuck.world.lands.gen.ChunkProviderLands;
import com.mraof.minestuck.world.lands.structure.IGateStructure;

public interface ILandAspect<A extends ILandAspect>
{
	
	/**
	 * Returns a string that represents a unique name for a land, Used in saving and loading land data.
	 * @return
	 */
	public String getPrimaryName();
	
	/**
	 * Returns a list of strings used in giving a land a random name.
	 */
	public String[] getNames();
	
	public IGateStructure getGateStructure();
	
	abstract void prepareChunkProvider(ChunkProviderLands chunkProvider);
	abstract void prepareChunkProviderServer(ChunkProviderLands chunkProvider);
	
	List<A> getVariations();
	
	A getPrimaryVariant();
}
