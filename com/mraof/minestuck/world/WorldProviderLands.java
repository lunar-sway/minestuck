package com.mraof.minestuck.world;

import net.minecraft.world.WorldProvider;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.biome.WorldChunkManagerHell;
import net.minecraft.world.chunk.IChunkProvider;

import com.mraof.minestuck.util.Debug;
import com.mraof.minestuck.world.gen.ChunkProviderLands;

public class WorldProviderLands extends WorldProvider 
{
	private ChunkProviderLands provider;

	@Override
	public String getDimensionName()
	{
		if (provider == null || provider.aspect1 == null || provider.aspect2 == null) {
			return "Land";
		} else {
		return "Land of " + provider.helper.pickElement(provider.aspect1.getNames()) + " and " + provider.helper.pickElement(provider.aspect2.getNames());
		}
	}

	public IChunkProvider createChunkGenerator()
	{
		if (provider == null) {
			provider = new ChunkProviderLands(this.worldObj, this.worldObj.getSeed()*dimensionId, true);
		}
		return provider;
	}
	@Override
	public boolean isDaytime() {
	   	if (provider != null) {
    		switch (provider.dayCycle) {
    		case (0):
    			return super.isDaytime();
    		case (1):
    			return true;
    		case (2):
    			return false;
    		}
    		return true; //We should never reach this
    	} else {
      		createChunkGenerator();
    		return this.isDaytime();
    	}
	}
	public void registerWorldChunkManager()
    {
		super.registerWorldChunkManager();
		isHellWorld = false;
        this.worldChunkMgr = new WorldChunkManagerHell(BiomeGenBase.plains, 0.5F, 0.5F);
    }
    public float calculateCelestialAngle(long par1, float par3)
    {
    	if (provider != null) {
    		//Debug.print("Time mode is "+provider.dayCycle);
    		switch (provider.dayCycle) {
    		case (0):
    			return super.calculateCelestialAngle(par1,par3);
    		case (1):
    			return 12000.0F;
    		case (2):
    			return 24000.0F;
    		}
    		return 12000.0F; //We should never reach this
    	} else {
    		createChunkGenerator();
    		return this.calculateCelestialAngle(par1,par3);
    	}
    }
    public boolean isSurfaceWorld()
    {
        return true;
    }

}
