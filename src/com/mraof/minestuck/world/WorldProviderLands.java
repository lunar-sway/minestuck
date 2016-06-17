package com.mraof.minestuck.world;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.DimensionType;
import net.minecraft.world.World;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.WorldSettings.GameType;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.chunk.IChunkGenerator;

import com.mraof.minestuck.network.skaianet.SburbConnection;
import com.mraof.minestuck.network.skaianet.SkaianetHandler;
import com.mraof.minestuck.util.Debug;
import com.mraof.minestuck.util.IdentifierHandler;
import com.mraof.minestuck.world.lands.LandAspectRegistry;
import com.mraof.minestuck.world.lands.gen.ChunkProviderLands;

public class WorldProviderLands extends WorldProvider
{
	private ChunkProviderLands provider;
	public LandAspectRegistry.AspectCombination landAspects;
	
	@Override
	public DimensionType getDimensionType()
	{
		return MinestuckDimensionHandler.landDimensionType;
	}
	
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
	
	@Override
	public IChunkGenerator createChunkGenerator()
	{
		if (provider == null)
		{
			landAspects = MinestuckDimensionHandler.getAspects(getDimension());
			
			provider = landAspects.aspectTitle.createChunkProvider(this);
			
		}
		return provider;
	}
	
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
		BlockPos spawn = MinestuckDimensionHandler.getSpawn(getDimension());
		if(spawn != null)
			return spawn;
		else
		{
			Debug.errorf("Couldn't get special spawnpoint for dimension %d. This should not happen.", this.getDimension());
			return super.getSpawnPoint();
		}
	}
	
	@Override
	public BlockPos getRandomizedSpawnPoint()
	{
		createChunkGenerator();
		BlockPos coordinates = getSpawnPoint();
		
		boolean isAdventure = worldObj.getWorldInfo().getGameType() == GameType.ADVENTURE;
		int spawnFuzz = 12;	//TODO respect changed value of MinestuckConfig.artifactRange
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
	public int getRespawnDimension(EntityPlayerMP player)	//actually only called when the provider says that you can't respawn in that dimension, which also causes beds to explode
	{
		SburbConnection c = SkaianetHandler.getMainConnection(IdentifierHandler.encode(player), true);
		return c == null || !c.enteredGame() ? getDimension() : c.getClientDimension();
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
		this.worldChunkMgr = new WorldChunkManagerLands(worldObj, provider.rainfall, provider.oceanChance);
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
	public Vec3d getFogColor(float par1, float par2)
	{
		if(provider != null)
		{
			return provider.getFogColor();
		}
		else
		{
			Debug.debug("Getting superclass fog color");
			return super.getFogColor(par1, par2);
		}
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
	
	@Override
	public void onPlayerAdded(EntityPlayerMP player)
	{
		int centerX = ((int)player.posX) >> 4;
		int centerZ = ((int)player.posZ) >> 4;
		for(int x = centerX - 1; x <= centerX + 1; x++)
			for(int z = centerZ - 1; z <= centerZ + 1; z++)
				this.worldObj.getChunkProvider().provideChunk(x, z);
	}
}
