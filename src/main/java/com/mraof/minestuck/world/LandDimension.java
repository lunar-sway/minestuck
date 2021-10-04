package com.mraof.minestuck.world;
/*
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
	private static final long GENERIC_BIG_PRIME = 661231563202688713L;
	
	@Override
	public long getSeed()
	{
		return super.getSeed() + getType().getId()*GENERIC_BIG_PRIME;
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
	public boolean canRespawnHere()
	{
		return false;
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
	
	}
	
	@Nullable
	@Override
	public MusicTicker.MusicType getMusicType()
	{
		//A hack to make the vanilla music ticker behave as if the music type changes when entering/exiting lands
		// (matters for some timing-related behavior, even if we stop any vanilla music from playing in lands)
		return MusicTicker.MusicType.MENU;
	}
}*/