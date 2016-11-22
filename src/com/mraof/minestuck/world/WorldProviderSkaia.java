package com.mraof.minestuck.world;

import net.minecraft.entity.player.EntityPlayerMP;
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
		return new ChunkProviderSkaia(this.world, this.world.getSeed(), true);
	}
	
	@Override
	public boolean isDaytime()
	{
		return true;
	}
	
	@Override
	protected void createBiomeProvider()
	{
		this.biomeProvider = new BiomeProviderSingle(Biomes.PLAINS);
	}
	
	@Override
	public float calculateCelestialAngle(long par1, float par3)
	{
		return 12000.0F;
	}

	@Override
	public boolean isSurfaceWorld()
	{
		return false;
	}
	
	@Override
	public DimensionType getDimensionType()
	{
		return MinestuckDimensionHandler.skaiaDimensionType;
	}
	
	@Override
	public void onPlayerAdded(EntityPlayerMP player)
	{
		int centerX = ((int)player.posX) >> 4;
		int centerZ = ((int)player.posZ) >> 4;
		for(int x = centerX - 1; x <= centerX + 1; x++)
			for(int z = centerZ - 1; z <= centerZ + 1; z++)
				this.world.getChunkProvider().provideChunk(x, z);
	}
}