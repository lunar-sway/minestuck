package com.mraof.minestuck.world;

import com.mraof.minestuck.client.renderer.LandSkyRenderer;
import com.mraof.minestuck.network.skaianet.SburbConnection;
import com.mraof.minestuck.network.skaianet.SkaianetHandler;
import com.mraof.minestuck.util.Debug;
import com.mraof.minestuck.util.IdentifierHandler;
import com.mraof.minestuck.world.biome.LandBiomeHolder;
import com.mraof.minestuck.world.biome.LandWrapperBiome;
import com.mraof.minestuck.world.gen.MSWorldGenTypes;

import com.mraof.minestuck.world.lands.LandAspectRegistry;
import com.mraof.minestuck.world.lands.LandAspects;
import com.mraof.minestuck.world.gen.LandGenSettings;
import com.mraof.minestuck.world.lands.terrain.TerrainLandAspect;
import com.mraof.minestuck.world.lands.title.TitleLandAspect;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.ListNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.dimension.Dimension;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.ModDimension;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;

public class LandDimension extends Dimension
{
	private LandBiomeHolder biomeHolder;
	public final LandAspects landAspects;
	public float skylightBase;
	Vec3d skyColor;
	Vec3d fogColor;
	Vec3d cloudColor;
	
	public LandDimension(World worldIn, DimensionType typeIn, LandAspects aspects)
	{
		super(worldIn, typeIn);
		
		if(aspects != null)
			landAspects = aspects;
		else
		{
			Debug.warnf("Creating land dimension %s without land aspects", typeIn);
			landAspects = new LandAspects(LandAspectRegistry.TERRAIN_NULL, LandAspectRegistry.TITLE_NULL);
		}
		
		doesWaterVaporize = false;
		
		this.nether = false;
		
		if(!(world instanceof ServerWorld)) //world.isRemote can't be used just yet because it is set after the dimension is created
			setSkyRenderer(new LandSkyRenderer(this));
		
		initLandAspects();
	}
	
	private void initLandAspects()
	{
		skylightBase = landAspects.terrain.getSkylightBase();
		skyColor = landAspects.terrain.getSkyColor();
		fogColor = landAspects.terrain.getFogColor();
		cloudColor = landAspects.terrain.getCloudColor();
		
		landAspects.title.prepareWorldProvider(this);
		
		biomeHolder = new LandBiomeHolder(landAspects, false);
	}
	
	@Override
	public float getSunBrightness(float partialTicks)
	{
		float skylight = skylightBase;
		skylight = (float)((double)skylight * (1.0D - (double)(world.getRainStrength(partialTicks) * 5.0F) / 16.0D));
		skylight = (float)((double)skylight * (1.0D - (double)(world.getThunderStrength(partialTicks) * 5.0F) / 16.0D));
		return skylight*0.8F + 0.2F;
	}
	
	@Override
	public float getStarBrightness(float par1)
	{
		float f = 1 - skylightBase;
		return f * f * 0.5F;
	}
	
	@Override
	public ChunkGenerator<?> createChunkGenerator()
	{
		LandGenSettings settings = MSWorldGenTypes.LANDS.createSettings();
		settings.setLandAspects(landAspects);
		settings.setBiomeHolder(biomeHolder);
		return MSWorldGenTypes.LANDS.create(this.world, MSWorldGenTypes.LAND_BIOMES.create(MSWorldGenTypes.LAND_BIOMES.createSettings().setGenSettings(settings).setSeed(this.getSeed())), settings);
	}
	
	public LandWrapperBiome getWrapperBiome(Biome biome)
	{
		return biomeHolder.localBiomeFrom(biome);
	}
	
	@Override
	public Biome getBiome(BlockPos pos)
	{
		return biomeHolder.localBiomeFrom(super.getBiome(pos));
	}
	
	@Nullable
	@Override
	public BlockPos findSpawn(ChunkPos p_206920_1_, boolean checkValid)
	{
		return null;
	}
	
	@Nullable
	@Override
	public BlockPos findSpawn(int p_206921_1_, int p_206921_2_, boolean checkValid)
	{
		return null;
	}
	
	@Override
	public float calculateCelestialAngle(long worldTime, float partialTicks)
	{
		return 0;
	}
	
	@Override
	public boolean doesXZShowFog(int x, int z)
	{
		return false;
	}
	
	/*public String getDimensionName()
	{
		if (chunkProvider == null || chunkProvider.aspect1 == null || chunkProvider.aspect2 == null) {
			return "Land";
		} else {
			return "Land of " + chunkProvider.aspect1.getPrimaryName() + " and " + chunkProvider.aspect2.getPrimaryName();
		}
	}*/
	
	@Override
	public BlockPos getSpawnPoint() 
	{
		BlockPos spawn = new BlockPos(0, 0,0 );
		if(spawn != null)
			return spawn;
		else
		{
			Debug.errorf("Couldn't get special spawnpoint for dimension %d. This should not happen.", this.getDimension());
			return super.getSpawnPoint();
		}
	}
	/*
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
	}*/
	
	@Override
	public DimensionType getRespawnDimension(ServerPlayerEntity player)
	{
		DimensionType dimOut;
		SburbConnection c = SkaianetHandler.get(player.server).getMainConnection(IdentifierHandler.encode(player), true);
		if(c == null || !c.hasEntered())
			dimOut = player.getSpawnDimension();	//Method outputs 0 when no spawn dimension is set, sending players to the overworld.
		else
		{
			dimOut = c.getClientDimension();
		}
		
		return dimOut;
	}
	
	@Override
	public SleepResult canSleepAt(PlayerEntity player, BlockPos pos)
	{
		return SleepResult.ALLOW;
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
	
	/*
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
	}*/
	/*
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
	}*/
	
	@Override
	public Vec3d getSkyColor(BlockPos cameraPos, float partialTicks)
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
	
	//@Override TODO Is this actually needed?
	public void onPlayerAdded(ServerPlayerEntity player)
	{
		int centerX = ((int)player.posX) >> 4;
		int centerZ = ((int)player.posZ) >> 4;
		for(int x = centerX - 1; x <= centerX + 1; x++)
			for(int z = centerZ - 1; z <= centerZ + 1; z++)
				this.world.getChunkProvider().getChunk(x, z, true);
	}
	
	public BlockPos findAndMarkNextStructure(ServerPlayerEntity player, String type, ListNBT tags)
	{
		if(type.equalsIgnoreCase("village"))
			//return chunkProvider.villageHandler.findAndMarkNextVillage(player, type, tags);
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
	
	public static class Type extends ModDimension
	{
		public Map<Integer, LandAspects> dimToLandAspects = new HashMap<>();
		
		@Override
		public void write(PacketBuffer buffer, boolean network)
		{
			if(network)
			{
				buffer.writeInt(dimToLandAspects.size());
				for(Map.Entry<Integer, LandAspects> entry : dimToLandAspects.entrySet())
				{
					buffer.writeVarInt(entry.getKey());
					buffer.writeRegistryId(entry.getValue().terrain);
					buffer.writeRegistryId(entry.getValue().title);
				}
			}
		}
		
		@Override
		public void read(PacketBuffer buffer, boolean network)
		{
			if(network)
			{
				dimToLandAspects.clear();
				int size = buffer.readInt();
				for(int i = 0; i < size; i++)
				{
					int id = buffer.readVarInt();
					TerrainLandAspect terrain = buffer.readRegistryIdSafe(TerrainLandAspect.class);
					TitleLandAspect title = buffer.readRegistryIdSafe(TitleLandAspect.class);
					dimToLandAspects.put(id, new LandAspects(terrain, title));
				}
			}
		}
		
		@Override
		public BiFunction<World, DimensionType, ? extends Dimension> getFactory()
		{
			return this::createDimension;
		}
		
		private LandDimension createDimension(World world, DimensionType type)
		{
			LandAspects aspects = dimToLandAspects.get(type.getId());
			if(aspects == null)
				Debug.warn("Trying to create a land world but haven't gotten its land aspects!");
			return new LandDimension(world, type, aspects);
		}
	}
}