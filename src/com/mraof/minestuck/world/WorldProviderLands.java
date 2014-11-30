package com.mraof.minestuck.world;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.Vec3;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.WorldSettings.GameType;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.biome.WorldChunkManagerHell;
import net.minecraft.world.chunk.IChunkProvider;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.network.skaianet.SessionHandler;
import com.mraof.minestuck.util.Debug;
import com.mraof.minestuck.world.gen.ChunkProviderLands;
import com.mraof.minestuck.world.gen.lands.LandHelper;
import com.mraof.minestuck.world.gen.lands.PrimaryAspect;
import com.mraof.minestuck.world.gen.lands.SecondaryAspect;

public class WorldProviderLands extends WorldProvider 
{
	private ChunkProviderLands provider;
	public PrimaryAspect aspect1;
	public SecondaryAspect aspect2;
	public LandHelper landHelper;
	
	@Override
	public float calculateCelestialAngle(long par1, float par3)
	{
		if (provider != null) 
		{
			//Debug.print("Time mode is "+provider.dayCycle);
			switch(provider.dayCycle) 
			{
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
		if (provider == null)
		{
			landHelper = new LandHelper(Minestuck.worldSeed/dimensionId);
			NBTBase landDataTag = worldObj.getWorldInfo().getAdditionalProperty("LandData");
			if(landDataTag == null)
			{
				aspect1 = landHelper.getLandAspect();
				aspect2 = SessionHandler.getSecondaryAspect(landHelper, dimensionId);
			}
			else
			{
				aspect1 = LandHelper.fromName(((NBTTagCompound)landDataTag).getString("aspect1"));
				aspect2 = LandHelper.fromName2(((NBTTagCompound)landDataTag).getString("aspect2"));
			}
			
			long seed = this.worldObj.isRemote ? Minestuck.worldSeed : this.worldObj.getSeed();
			provider = aspect2.createChunkProvider(this);
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
	public ChunkCoordinates getRandomizedSpawnPoint() 
	{
		createChunkGenerator();
		ChunkCoordinates chunkcoordinates = new ChunkCoordinates(provider.spawnX, provider.spawnY, provider.spawnZ);

		boolean isAdventure = worldObj.getWorldInfo().getGameType() == GameType.ADVENTURE;
		int spawnFuzz = 12;
		int spawnFuzzHalf = spawnFuzz / 2;

		if (!hasNoSky && !isAdventure)
		{
			chunkcoordinates.posX += this.worldObj.rand.nextInt(spawnFuzz) - spawnFuzzHalf;
			chunkcoordinates.posZ += this.worldObj.rand.nextInt(spawnFuzz) - spawnFuzzHalf;
			chunkcoordinates.posY = this.worldObj.getTopSolidOrLiquidBlock(chunkcoordinates.posX, chunkcoordinates.posZ);
		}

		return chunkcoordinates;
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
}
