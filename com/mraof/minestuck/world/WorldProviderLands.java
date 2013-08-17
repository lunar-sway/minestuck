package com.mraof.minestuck.world;

import net.minecraft.world.WorldProvider;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.biome.WorldChunkManagerHell;
import net.minecraft.world.chunk.IChunkProvider;

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
		return "Land of " + provider.aspect1.getPrimaryName() + " and " +  provider.aspect2.getPrimaryName();
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
		return true;
	}
	public void registerWorldChunkManager()
    {
		super.registerWorldChunkManager();
		isHellWorld = false;
        this.worldChunkMgr = new WorldChunkManagerHell(BiomeGenBase.plains, 0.5F, 0.5F);
    }
    public float calculateCelestialAngle(long par1, float par3)
    {
        return 12000.0F;
    }
    public boolean isSurfaceWorld()
    {
        return false;
    }

}
