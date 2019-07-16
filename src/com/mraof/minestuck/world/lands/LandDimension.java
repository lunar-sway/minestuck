package com.mraof.minestuck.world.lands;

import com.mraof.minestuck.block.MinestuckBlocks;
import com.mraof.minestuck.client.renderer.LandSkyRender;
import com.mraof.minestuck.network.skaianet.SburbConnection;
import com.mraof.minestuck.network.skaianet.SkaianetHandler;
import com.mraof.minestuck.util.Debug;
import com.mraof.minestuck.util.IdentifierHandler;
import com.mraof.minestuck.world.MinestuckDimensionHandler;
import com.mraof.minestuck.world.biome.BiomeMinestuck;
import com.mraof.minestuck.world.gen.ModChunkGeneratorType;
import com.mraof.minestuck.world.gen.SkaiaGenSettings;
import com.mraof.minestuck.world.lands.gen.ChunkGeneratorLands;
import com.mraof.minestuck.world.lands.gen.ChunkProviderLands;

import com.mraof.minestuck.world.lands.gen.LandGenSettings;
import com.mraof.minestuck.world.lands.structure.blocks.StructureBlockRegistry;
import com.mraof.minestuck.world.lands.terrain.TerrainLandAspect;
import com.mraof.minestuck.world.lands.title.TitleLandAspect;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.GameType;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.provider.BiomeProviderType;
import net.minecraft.world.dimension.Dimension;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.ChunkGeneratorType;
import net.minecraft.world.gen.OverworldGenSettings;
import net.minecraftforge.common.ModDimension;

import javax.annotation.Nullable;
import java.util.function.BiFunction;

public class LandDimension extends Dimension
{
	private ChunkGeneratorLands chunkGen;
	
	public StructureBlockRegistry blockRegistry;
	public LandAspects landAspects;
	public float skylightBase;
	Vec3d skyColor;
	Vec3d fogColor;
	Vec3d cloudColor;
	
	public LandDimension(World worldIn, DimensionType typeIn)
	{
		super(worldIn, typeIn);
		typeIn.getData().resetReaderIndex();
		if(typeIn.getData().readBoolean())
		{
			TerrainLandAspect terrain = LandAspectRegistry.fromNameTerrain(typeIn.getData().readString(32767), false);
			TitleLandAspect title = LandAspectRegistry.fromNameTitle(typeIn.getData().readString(32767), false);
			landAspects = new LandAspects(terrain, title);
		} else landAspects = null;
		
		doesWaterVaporize = false;
		
		//this.biomeProvider = new BiomeProviderLands(world, chunkProvider.rainfall, chunkProvider.oceanChance, chunkProvider.roughChance);
		this.nether = false;
		
		if(world.isRemote)
			setSkyRenderer(new LandSkyRender(this));
		
		if(landAspects != null)
			initLandAspects();
	}
	
	public void initLandAspects()
	{
		blockRegistry = new StructureBlockRegistry();
		landAspects.aspectTerrain.registerBlocks(blockRegistry);
		skylightBase = landAspects.aspectTerrain.getSkylightBase();
		skyColor = landAspects.aspectTerrain.getSkyColor();
		fogColor = landAspects.aspectTerrain.getFogColor();
		cloudColor = landAspects.aspectTerrain.getCloudColor();
		landAspects.aspectTitle.prepareWorldProvider(this);
		if(chunkGen != null)
			initGenSettings(chunkGen.getSettings());
	}
	
	private void initGenSettings(LandGenSettings settings)
	{
		settings.setDefaultBlock(blockRegistry.getBlockState("ground"));
		settings.setDefaultFluid(blockRegistry.getBlockState("ocean"));
	}
	
	
	//@Override TODO
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
	public ChunkGenerator<?> createChunkGenerator()
	{
		LandGenSettings settings = ModChunkGeneratorType.LANDS.createSettings();
		if(landAspects != null)
			initGenSettings(settings);
		chunkGen = ModChunkGeneratorType.LANDS.create(this.world, BiomeProviderType.FIXED.create(BiomeProviderType.FIXED.createSettings().setBiome(BiomeMinestuck.skaia)), settings);
		return chunkGen;
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
	/*
	@Override
	public Biome getBiomeForCoords(BlockPos pos)
	{
		return chunkProvider.getBiomeGen();
	}^*/
	
	
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
		
		@Override
		public BiFunction<World, DimensionType, ? extends Dimension> getFactory()
		{
			return LandDimension::new;
		}
	}
}
