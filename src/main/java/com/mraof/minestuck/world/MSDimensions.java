package com.mraof.minestuck.world;

import com.google.common.collect.ImmutableMap;
import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.network.MSPacketHandler;
import com.mraof.minestuck.network.data.LandTypesDataPacket;
import com.mraof.minestuck.skaianet.SburbConnection;
import com.mraof.minestuck.world.lands.LandInfo;
import com.mraof.minestuck.world.lands.LandTypePair;
import com.mraof.minestuck.world.lands.LandTypes;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Mod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Mod.EventBusSubscriber(modid = Minestuck.MOD_ID, bus=Mod.EventBusSubscriber.Bus.FORGE)
public class MSDimensions
{
	private static final Logger LOGGER = LogManager.getLogger();
	private static final Map<RegistryKey<World>, LandInfo> typeToInfoContainer = new HashMap<>();
	
	public static RegistryKey<World> SKAIA = RegistryKey.create(Registry.DIMENSION_REGISTRY, new ResourceLocation(Minestuck.MOD_ID, "skaia"));
	
	public static LandTypePair getAspects(MinecraftServer server, RegistryKey<World> dimension)
	{
		LandInfo info = getLandInfo(server, dimension);
		if(info != null)
			return info.getLandAspects();
		else if(isLandDimension(server, dimension))
		{
			LOGGER.warn("Tried to get land aspects for {}, but did not find a container reference! Using defaults instead.", dimension.getRegistryName());
			return new LandTypePair(LandTypes.TERRAIN_NULL, LandTypes.TITLE_NULL);
		} else return null;
	}
	
	public static LandInfo getLandInfo(World world)
	{
		return getLandInfo(world.getServer(), world.dimension());
	}
	
	public static LandInfo getLandInfo(MinecraftServer server, RegistryKey<World> dimension)
	{
		Objects.requireNonNull(server);
		return typeToInfoContainer.get(dimension);
	}
	
	public static boolean isLandDimension(MinecraftServer server, RegistryKey<World> world)
	{
		Objects.requireNonNull(server);
		return typeToInfoContainer.containsKey(world);
	}
	
	public static boolean isSkaia(RegistryKey<World> dimension)
	{
		return dimension == SKAIA;
	}
	
	public static void updateLandMaps(SburbConnection connection, boolean shouldSendUpdate)
	{
		typeToInfoContainer.put(connection.getLandInfo().getDimensionType(), connection.getLandInfo());
		
		if (shouldSendUpdate)
		{
			MSPacketHandler.sendToAll(createLandTypesPacket());
		}
	}
	
	public static void clear()
	{
		typeToInfoContainer.clear();
	}
	
	public static void sendDimensionData(ServerPlayerEntity player)
	{
		MSPacketHandler.sendToPlayer(createLandTypesPacket(), player);
	}
	
	private static LandTypesDataPacket createLandTypesPacket()
	{
		ImmutableMap.Builder<RegistryKey<World>, LandTypePair> builder = new ImmutableMap.Builder<>();
		
		for (Map.Entry<RegistryKey<World>, LandInfo> entry : typeToInfoContainer.entrySet())
			builder.put(entry.getKey(), entry.getValue().getLandAspects());
		
		return new LandTypesDataPacket(builder.build());
	}
}