package com.mraof.minestuck.world;

import com.mraof.minestuck.client.renderer.LandSkyRender;
import com.mraof.minestuck.network.skaianet.SburbConnection;
import com.mraof.minestuck.network.skaianet.SkaianetHandler;
import com.mraof.minestuck.util.Debug;
import com.mraof.minestuck.util.IdentifierHandler;
import com.mraof.minestuck.world.lands.LandAspectRegistry;
import com.mraof.minestuck.world.lands.gen.ChunkProviderLands;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.DimensionType;
import net.minecraft.world.GameType;
import net.minecraft.world.World;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.IChunkGenerator;

public class WorldProviderLands extends WorldProvider
{
	public ChunkProviderLands chunkProvider;
	public LandAspectRegistry.AspectCombination landAspects;
	public float skylightBase;
	Vec3d skyColor;
	Vec3d fogColor;
	Vec3d cloudColor;
	
	@Override
	public DimensionType getDimensionType()
	{
		return MinestuckDimensionHandler.landDimensionType;
	}
	
	@Override
	public float getSunBrightnessFactor(float partialTicks)
	{
		float skylight = skylightBase;
		skylight = (float)((double)skylight * (1.0D - (double)(world.getRainStrength(partialTicks) * 5.0F) / 16.0D));
		skylight = (float)((double)skylight * (1.0D - (double)(world.getThunderStrength(partialTicks) * 5.0F) / 16.0D));
		return skylight;
	}
	
	@Override
	public float getStarBrightness(float par1)
	{
		float f = 1 - skylightBase;
		return f * f * 0.5F;
	}
	
	@Override
	public float getSunBrightness(float par1)
	{
		return this.getSunBrightnessFactor(par1) * 0.8F + 0.2F;
	}
	
	@Override
	public IChunkGenerator createChunkGenerator()
	{
		if (chunkProvider == null)
		{
			landAspects = MinestuckDimensionHandler.getAspects(getDimension());
			
			chunkProvider = landAspects.aspectTitle.createChunkProvider(this);
			
		}
		return chunkProvider;
	}
	
	public String getDimensionName()
	{
		if (chunkProvider == null || chunkProvider.aspect1 == null || chunkProvider.aspect2 == null) {
			return "Land";
		} else {
			return "Land of " + chunkProvider.aspect1.getPrimaryName() + " and " + chunkProvider.aspect2.getPrimaryName();
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
		
		boolean isAdventure = world.getWorldInfo().getGameType() == GameType.ADVENTURE;
		int spawnFuzz = 12;	//TODO respect changed value of MinestuckConfig.artifactRange
		int spawnFuzzHalf = spawnFuzz / 2;
		
		if (!isAdventure)
		{
			coordinates = coordinates.add(this.world.rand.nextInt(spawnFuzz) - spawnFuzzHalf,
					0, this.world.rand.nextInt(spawnFuzz) - spawnFuzzHalf);
			coordinates = this.world.getTopSolidOrLiquidBlock(coordinates);
		}
		
		return coordinates;
	}
	
	@Override
	public int getRespawnDimension(EntityPlayerMP player)
	{
		int dimOut = 0;
		SburbConnection c = SkaianetHandler.getMainConnection(IdentifierHandler.encode(player), true);
		if(c == null || !c.enteredGame())
			dimOut = player.getSpawnDimension();	//Method outputs 0 when no spawn dimension is set, sending players to the overworld.
		else
		{
			dimOut = c.getClientDimension();
		}
		
		return dimOut;
	}
	
	@Override
	public WorldSleepResult canSleepAt(EntityPlayer player, BlockPos pos)
	{
		return WorldSleepResult.ALLOW;
	}
	
	@Override
	public boolean canRespawnHere()
	{
		return false;
	}
	
	@Override
	public boolean isSurfaceWorld()
	{
		return true;
	}
	
	@Override
	protected void init()
	{
		hasSkyLight = true;
		doesWaterVaporize = false;
		if(chunkProvider == null)
			createChunkGenerator();
		this.biomeProvider = new BiomeProviderLands(world, chunkProvider.rainfall, chunkProvider.oceanChance, chunkProvider.roughChance);
		this.nether = false;
		
		if(world.isRemote)
			setSkyRenderer(new LandSkyRender(this));
		
		skylightBase = landAspects.aspectTerrain.getSkylightBase();
		skyColor = landAspects.aspectTerrain.getSkyColor();
		fogColor = landAspects.aspectTerrain.getFogColor();
		cloudColor = landAspects.aspectTerrain.getCloudColor();
		landAspects.aspectTitle.prepareWorldProvider(this);
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
		if(chunkProvider.weatherType == -1)
			world.rainingStrength = 0.0F;
		else if((chunkProvider.weatherType & 5) != 0)
			world.rainingStrength = 1.0F;
		
		if(chunkProvider.weatherType == -1 || (chunkProvider.weatherType & 6) == 0)
			world.thunderingStrength = 0.0F;
		else if((chunkProvider.weatherType & 4) != 0)
			world.thunderingStrength = 1.0F;
	}
	
	@Override
	public Vec3d getSkyColor(Entity cameraEntity, float partialTicks)
	{
		return skyColor;
	}
	
	@Override
	public Vec3d getFogColor(float par1, float par2)
	{
		return getFogColor();
	}
	
	@Override
	public Vec3d getCloudColor(float partialTicks)
	{
		return cloudColor;
	}
	
	public World getWorld()
	{
		return world;
	}
	
	@Override
	public Biome getBiomeForCoords(BlockPos pos)
	{
		return chunkProvider.getBiomeGen();
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
	
	public BlockPos findAndMarkNextStructure(EntityPlayerMP player, String type, NBTTagList tags)
	{
		if(type.equalsIgnoreCase("village"))
			return chunkProvider.villageHandler.findAndMarkNextVillage(player, type, tags);
		Debug.warnf("Couldn't identify %s", type);
		return null;
	}
	
	public void mergeFogColor(Vec3d fogColor, float strength)
	{
		double d1 = (this.fogColor.x + fogColor.x*strength)/(1 + strength);
		double d2 = (this.fogColor.y + fogColor.y*strength)/(1 + strength);
		double d3 = (this.fogColor.z + fogColor.z*strength)/(1 + strength);
		this.fogColor = new Vec3d(d1, d2, d3);
	}
	
	public Vec3d getFogColor()
	{
		return this.fogColor;
	}
}
