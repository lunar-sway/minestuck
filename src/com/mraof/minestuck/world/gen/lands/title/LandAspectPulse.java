package com.mraof.minestuck.world.gen.lands.title;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.world.gen.ChunkProviderLands;

public class LandAspectPulse extends TitleAspect
{

	@Override
	public String getPrimaryName()
	{
		return "Pulse";
	}

	@Override
	public String[] getNames()
	{
		return new String[]{"pulse", "blood"};
	}
	
	@Override
	protected void prepareChunkProvider(ChunkProviderLands chunkProvider)
	{
		if(chunkProvider.decorators != null)
		{
//			chunkProvider.oceanBlock = Minestuck.blockBlood;	Enable when mod fluids work
//			chunkProvider.riverBlock = Minestuck.blockBlood;
		}
	}

}
