package com.mraof.minestuck.world;

import net.minecraft.world.WorldProvider;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.biome.WorldChunkManagerHell;
import net.minecraft.world.chunk.IChunkProvider;

import com.mraof.minestuck.world.gen.ChunkProviderSkaia;

public class WorldProviderSkaia extends WorldProvider 
{
//	public int dimensionId = 2;

	@Override
	public String getDimensionName() 
	{
		return "Skaia";	
	}
	
	public IChunkProvider createChunkGenerator()
    {
        return new ChunkProviderSkaia(this.worldObj, this.worldObj.getSeed(), true);
    }
	@Override
	public boolean isDaytime() {
		return true;
	}
	public void registerWorldChunkManager()
    {
		super.registerWorldChunkManager();
        this.worldChunkMgr = new WorldChunkManagerHell(BiomeGenBase.plains, 0.5F);
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
