package com.mraof.minestuck.world;

import net.minecraft.init.Biomes;
import net.minecraft.world.DimensionType;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.biome.BiomeProviderSingle;
import net.minecraft.world.chunk.IChunkGenerator;

import com.mraof.minestuck.world.gen.ChunkProviderSkaia;

public class WorldProviderSkaia extends WorldProvider 
{
	
	@Override
	public IChunkGenerator createChunkGenerator()
	{
		return new ChunkProviderSkaia(this.worldObj, this.worldObj.getSeed(), true);
	}
	
	@Override
	public boolean isDaytime()
	{
		return true;
	}
	
	public void registerWorldChunkManager()
	{
		super.registerWorldChunkManager();
		this.worldChunkMgr = new BiomeProviderSingle(Biomes.plains);
	}
	
	public float calculateCelestialAngle(long par1, float par3)
	{
		return 12000.0F;
	}
	
	public boolean isSurfaceWorld()
	{
		return false;
	}
	
	@Override
	public DimensionType getDimensionType()
	{
		return MinestuckDimensionHandler.skaiaDimensionType;
	}
	
}