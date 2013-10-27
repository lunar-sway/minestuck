package com.mraof.minestuck.world;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.world.EnumGameType;
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
		}
		else 
		{
			createChunkGenerator();
			return this.calculateCelestialAngle(par1,par3);
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
	public String getDimensionName()
	{
		if (provider == null || provider.aspect1 == null || provider.aspect2 == null) {
			return "Land";
		} else {
			return "Land of " + provider.helper.pickElement(provider.aspect1.getNames()) + " and " + provider.helper.pickElement(provider.aspect2.getNames());
		}
	}

	@Override
	public ChunkCoordinates getRandomizedSpawnPoint() 
	{
		ChunkCoordinates chunkcoordinates = new ChunkCoordinates(this.worldObj.getSpawnPoint());
//		Debug.printf("Respawn Coordinates: %d, %d, %d", chunkcoordinates.posX, chunkcoordinates.posY, chunkcoordinates.posZ );

		boolean isAdventure = worldObj.getWorldInfo().getGameType() == EnumGameType.ADVENTURE;
		int spawnFuzz = 12;
		int spawnFuzzHalf = spawnFuzz / 2;

		if (!hasNoSky && !isAdventure)
		{
			chunkcoordinates.posX += this.worldObj.rand.nextInt(spawnFuzz) - spawnFuzzHalf;
			chunkcoordinates.posZ += this.worldObj.rand.nextInt(spawnFuzz) - spawnFuzzHalf;
			chunkcoordinates.posY = this.worldObj.getTopSolidOrLiquidBlock(chunkcoordinates.posX, chunkcoordinates.posZ);
		}
//		Debug.printf("Respawning at: %d, %d, %d", chunkcoordinates.posX, chunkcoordinates.posY, chunkcoordinates.posZ );

		return chunkcoordinates;
	}

	@Override
	public int getRespawnDimension(EntityPlayerMP player)
	{
		return this.dimensionId;
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

	@Override
	public boolean isSurfaceWorld()
	{
		return true;
	}

	@Override
	public void registerWorldChunkManager()
	{
		super.registerWorldChunkManager();
		isHellWorld = false;
		this.worldChunkMgr = new WorldChunkManagerHell(BiomeGenBase.sky, 0.5F, 0.5F);
		this.hasNoSky = false;
	}
}
