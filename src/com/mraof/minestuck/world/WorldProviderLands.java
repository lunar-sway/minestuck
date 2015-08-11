package com.mraof.minestuck.world;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.BlockPos;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.WorldSettings.GameType;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.chunk.IChunkProvider;

import com.mraof.minestuck.util.Debug;
import com.mraof.minestuck.world.lands.LandAspectRegistry;
import com.mraof.minestuck.world.lands.gen.ChunkProviderLands;

public class WorldProviderLands extends WorldProvider 
{
	private ChunkProviderLands provider;
	public LandAspectRegistry.AspectCombination landAspects;
	
	@Override
	public float calculateCelestialAngle(long par1, float par3)
	{
		if (provider != null) 
		{
			switch(provider.dayCycle) 
			{
			case (0):
				return super.calculateCelestialAngle(par1,par3);
			case (1):
				return 1.0F;
			case (2):
				return 0.5F;
			}
			return 1.0F; //We should never reach this
		}
		else 
		{
			createChunkGenerator();
			return this.calculateCelestialAngle(par1,par3);
		}
	}

	public IChunkProvider createChunkGenerator()
	{
		if (provider == null)
		{
			landAspects = MinestuckDimensionHandler.getAspects(dimensionId);
			
			provider = landAspects.aspectTitle.createChunkProvider(this);
			
		}
		return provider;
	}

	@Override
	public String getDimensionName()
	{
		if (provider == null || provider.aspect1 == null || provider.aspect2 == null) {
			return "Land";
		} else {
			return "Land of " + provider.aspect1.getPrimaryName() + " and " + provider.aspect2.getPrimaryName();
		}
	}
	
	@Override
	public BlockPos getSpawnPoint() 
	{
		BlockPos spawn = MinestuckDimensionHandler.getSpawn(getDimensionId());
		if(spawn != null)
			return spawn;
		else
		{
			Debug.printf("Couldn't get special spawnpoint for dimension %d. This should not happen.", this.getDimensionId());
			return super.getSpawnPoint();
		}
	}
	
	@Override
	public BlockPos getRandomizedSpawnPoint()
	{
		createChunkGenerator();
		BlockPos coordinates = getSpawnPoint();
		
		boolean isAdventure = worldObj.getWorldInfo().getGameType() == GameType.ADVENTURE;
		int spawnFuzz = 12;
		int spawnFuzzHalf = spawnFuzz / 2;
		
		if (!isAdventure)
		{
			coordinates = coordinates.add(this.worldObj.rand.nextInt(spawnFuzz) - spawnFuzzHalf,
					0, this.worldObj.rand.nextInt(spawnFuzz) - spawnFuzzHalf);
			coordinates = this.worldObj.getTopSolidOrLiquidBlock(coordinates);
		}
		
		return coordinates;
	}
	
	@Override
	public int getRespawnDimension(EntityPlayerMP player)
	{
		int dim = player.getEntityData().getCompoundTag(EntityPlayer.PERSISTED_NBT_TAG).getInteger("LandId");
		return dim == 0 ? this.dimensionId : dim;
	}
	
	@Override
	public boolean canRespawnHere()
	{
		return true;
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
		if(provider == null)
			createChunkGenerator();
		this.worldChunkMgr = new WorldChunkManagerLands(worldObj, provider.rainfall);
		this.hasNoSky = false;
	}
	
	@Override
	public void calculateInitialWeather()
	{
		super.calculateInitialWeather();
		forceWeatherCheck();
	}
	
	@Override
	public void updateWeather()
	{
		super.updateWeather();
		forceWeatherCheck();
	}
	
	private void forceWeatherCheck()
	{
		if(provider.weatherType == -1)
			worldObj.rainingStrength = 0.0F;
		else if((provider.weatherType & 5) != 0)
			worldObj.rainingStrength = 1.0F;
		
		if(provider.weatherType == -1 || (provider.weatherType & 6) == 0)
			worldObj.thunderingStrength = 0.0F;
		else if((provider.weatherType & 4) != 0)
			worldObj.thunderingStrength = 1.0F;
	}
	
	@Override
	public Vec3 getFogColor(float par1, float par2)
	{
		if(provider != null)
		{
			return provider.getFogColor();
		}
		else
		{
			Debug.print("Getting superclass fog color");
			return super.getFogColor(par1, par2);
		}
	}
	
	@Override
	public String getInternalNameSuffix()
	{
		return "_minestuck";
	}
	
	public World getWorld()
	{
		return worldObj;
	}
	
	@Override
	public BiomeGenBase getBiomeGenForCoords(BlockPos pos)
	{
		return provider.getBiomeGen();
	}
	
}
