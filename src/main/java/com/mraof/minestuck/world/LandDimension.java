package com.mraof.minestuck.world;
/*
import com.mraof.minestuck.client.renderer.LandSkyRenderer;
import com.mraof.minestuck.player.IdentifierHandler;
import com.mraof.minestuck.skaianet.SburbConnection;
import com.mraof.minestuck.skaianet.SkaianetHandler;
import com.mraof.minestuck.util.Debug;
import com.mraof.minestuck.world.biome.LandBiomeHolder;
import com.mraof.minestuck.world.biome.LandWrapperBiome;
import com.mraof.minestuck.world.gen.LandGenSettings;
import com.mraof.minestuck.world.gen.MSWorldGenTypes;
import com.mraof.minestuck.world.gen.feature.structure.blocks.StructureBlockRegistry;
import com.mraof.minestuck.world.lands.LandInfo;
import com.mraof.minestuck.world.lands.LandProperties;
import com.mraof.minestuck.world.lands.LandTypePair;
import com.mraof.minestuck.world.lands.LandTypes;
import net.minecraft.client.audio.MusicTicker;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.GameType;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.dimension.Dimension;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.ModDimension;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiFunction;

public class LandDimension extends Dimension
{
	private LandBiomeHolder biomeHolder;
	private LandProperties properties;
	private StructureBlockRegistry blocks;
	public final LandTypePair landTypes;
	
	private LandDimension(World worldIn, DimensionType typeIn, LandTypePair aspects)
	{
		super(worldIn, typeIn, 0.0F);
		
		if(aspects != null)
			landTypes = aspects;
		else
		{
			Debug.warnf("Creating land dimension %s without land aspects", typeIn);
			landTypes = new LandTypePair(LandTypes.TERRAIN_NULL, LandTypes.TITLE_NULL);
		}
		
		doesWaterVaporize = false;
		
		this.nether = false;
		
		if(!(world instanceof ServerWorld)) //world.isRemote can't be used just yet because it is set after the dimension is created
			setSkyRenderer(new LandSkyRenderer(this));
		
		initLandAspects();
	}
	
	private void initLandAspects()
	{
		properties = new LandProperties(landTypes.terrain);
		properties.load(landTypes);
		
		blocks = new StructureBlockRegistry();
		landTypes.terrain.registerBlocks(blocks);
		landTypes.title.registerBlocks(blocks);
		
		biomeHolder = new LandBiomeHolder(properties, landTypes);
		biomeHolder.initBiomesWith(blocks);
	}
	
	private static final long GENERIC_BIG_PRIME = 661231563202688713L;
	
	@Override
	public long getSeed()
	{
		return super.getSeed() + getType().getId()*GENERIC_BIG_PRIME;
	}
	
	@Override
	public ChunkGenerator<?> createChunkGenerator()
	{
		LandGenSettings settings = MSWorldGenTypes.LANDS.createSettings();
		settings.setLandTypes(landTypes);
		settings.setBiomeHolder(biomeHolder);
		settings.setStructureBlocks(blocks);
		return MSWorldGenTypes.LANDS.create(this.world, MSWorldGenTypes.LAND_BIOMES.create(MSWorldGenTypes.LAND_BIOMES.createSettings(this.world.getWorldInfo()).setGenSettings(settings).setSeed(this.getSeed())), settings);
	}
	
	public LandWrapperBiome getWrapperBiome(Biome biome)
	{
		return biomeHolder.localBiomeFrom(biome);
	}

	@Nullable
	@Override
	public BlockPos findSpawn(ChunkPos chunkPos, boolean checkValid)
	{
		return findSpawn(chunkPos.getXStart(), chunkPos.getZStart(), checkValid);
	}
	
	@Nullable
	@Override
	public BlockPos findSpawn(int posX, int posZ, boolean checkValid)
	{
		BlockPos pos = getSpawnPoint();
		
		boolean isAdventure = world.getWorldInfo().getGameType() == GameType.ADVENTURE;
		int spawnFuzz = 12;	//TODO spawn explicitly within the entry area
		int spawnFuzzHalf = spawnFuzz / 2;
		
		if (!isAdventure)
		{
			pos = pos.add(this.world.rand.nextInt(spawnFuzz) - spawnFuzzHalf,
					0, this.world.rand.nextInt(spawnFuzz) - spawnFuzzHalf);
			
			int y = world.getChunk(pos).getTopBlockY(Heightmap.Type.MOTION_BLOCKING, pos.getX() & 15, pos.getZ() & 15);
			if(y < 0)
				return null;
			
			pos = pos.up(y - pos.getY());
		}
		
		return pos;
	}
	
	@Override
	public float calculateCelestialAngle(long worldTime, float partialTicks)
	{
		//Reverses the algorithm used to calculate the skylight float. Needed as the skylight is currently hardcoded to use celestial angle
		return (float) (Math.acos((properties.skylightBase - 0.5F) / 2) / (Math.PI * 2F));
	}
	
	public float calculateVeilAngle()
	{
		double d0 = MathHelper.frac((double)world.getDayTime() / 24000.0D - 0.25D);
		double d1 = 0.5D - Math.cos(d0 * Math.PI) / 2.0D;
		return (float)(d0 * 2.0D + d1) / 3.0F;
	}
	
	@Override
	public boolean doesXZShowFog(int x, int z)
	{
		return false;
	}
	
	@Override
	public BlockPos getSpawnPoint() 
	{
		if(world.isRemote)
			return super.getSpawnPoint();
		else
		{
			LandInfo info = MSDimensions.getLandInfo(world);
			if(info != null)
				return info.getSpawn();
			else
			{
				return new BlockPos(0, 127, 0);
			}
		}
	}
	
	@Override
	public DimensionType getRespawnDimension(ServerPlayerEntity player)
	{
		DimensionType dimOut;
		Optional<SburbConnection> c = SkaianetHandler.get(player.server).getPrimaryConnection(IdentifierHandler.encode(player), true);
		if(c.isPresent() && c.get().hasEntered())
			dimOut = c.get().getClientDimension();
		else
		{
			dimOut = player.getSpawnDimension();	//Method outputs 0 when no spawn dimension is set, sending players to the overworld.
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
	
	@Override
	public void updateWeather(Runnable defaultLogic)
	{
		super.updateWeather(defaultLogic);
		forceWeatherCheck();
	}
	
	@Override
	public void calculateInitialWeather()
	{
		super.calculateInitialWeather();
		forceWeatherCheck();
	}
	
	private void forceWeatherCheck()
	{
		if(properties.forceRain == LandProperties.ForceType.OFF)
			world.rainingStrength = 0.0F;
		else if(properties.forceRain == LandProperties.ForceType.ON)
			world.rainingStrength = 1.0F;
		
		if(properties.forceThunder == LandProperties.ForceType.OFF)
			world.thunderingStrength = 0.0F;
		else if(properties.forceThunder == LandProperties.ForceType.ON)
			world.thunderingStrength = 1.0F;
	}
	
	@Override
	@OnlyIn(Dist.CLIENT)
	public Vec3d getFogColor(float celestialAngle, float partialTicks)
	{
		return properties.getFogColor();
	}
	
	public Vec3d getSkyColor()
	{
		return properties.getSkyColor();
	}
	
	public StructureBlockRegistry getBlocks()
	{
		return blocks;
	}
	
	@Nullable
	@Override
	public MusicTicker.MusicType getMusicType()
	{
		//A hack to make the vanilla music ticker behave as if the music type changes when entering/exiting lands
		// (matters for some timing-related behavior, even if we stop any vanilla music from playing in lands)
		return MusicTicker.MusicType.MENU;
	}
	
	public static class Type extends ModDimension
	{
		public boolean useServerData;
		public Map<ResourceLocation, LandTypePair.LazyInstance> dimToLandTypes = new HashMap<>();
		//TODO Dimension might actually not be unloaded when switching to/creating a new world
		@Override
		public void write(PacketBuffer buffer, boolean network)
		{
			if(network)
			{
				buffer.writeInt(dimToLandTypes.size());
				for(Map.Entry<ResourceLocation, LandTypePair.LazyInstance> entry : dimToLandTypes.entrySet())
				{
					buffer.writeResourceLocation(entry.getKey());
					entry.getValue().write(buffer);
				}
			}
		}
		
		@Override
		public void read(PacketBuffer buffer, boolean network)
		{
			if(network)
			{
				dimToLandTypes.clear();
				int size = buffer.readInt();
				for(int i = 0; i < size; i++)
				{
					ResourceLocation dimId = buffer.readResourceLocation();
					LandTypePair.LazyInstance landAspects = LandTypePair.LazyInstance.read(buffer);
					dimToLandTypes.put(dimId, landAspects);
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
			LandTypePair.LazyInstance aspects = dimToLandTypes.get(DimensionType.getKey(type));
			if(aspects == null)
				Debug.warn("Trying to create a land world but haven't gotten its land aspects!");
			return new LandDimension(world, type, aspects == null ? null : aspects.create());
		}
	}
}*/