package com.mraof.minestuck.world;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.BlockPos;
import net.minecraft.util.Vec3;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.WorldSettings.GameType;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.biome.WorldChunkManagerHell;
import net.minecraft.world.chunk.IChunkProvider;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.util.Debug;
import com.mraof.minestuck.world.gen.ChunkProviderLands;
import com.mraof.minestuck.world.storage.MinestuckSaveHandler;

public class WorldProviderLands extends WorldProvider 
{
	private ChunkProviderLands provider;

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
		if (provider == null) {
			long seed = this.worldObj.isRemote ? Minestuck.worldSeed : this.worldObj.getSeed();
			provider = new ChunkProviderLands(this.worldObj, seed, true);
		}
		return provider;
	}

	@Override
	public String getDimensionName()
	{
		if (provider == null || provider.aspect1 == null || provider.aspect2 == null) {
			return "Land";
		} else {
			return "Land of " + provider.aspect1.getNames()[provider.nameIndex1] + " and " + provider.aspect2.getNames()[provider.nameIndex2];
		}
	}
	
	@Override
	public BlockPos getSpawnPoint() 
	{
		BlockPos spawn = MinestuckSaveHandler.spawnpoints.get((byte) this.getDimensionId());
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
		this.worldChunkMgr = new WorldChunkManagerHell(BiomeGenBase.sky, 0.5F);
		this.hasNoSky = false;
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
}
