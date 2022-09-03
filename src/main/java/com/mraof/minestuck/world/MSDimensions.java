package com.mraof.minestuck.world;

import com.google.common.collect.ImmutableMap;
import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.network.MSPacketHandler;
import com.mraof.minestuck.network.data.LandTypesDataPacket;
import com.mraof.minestuck.skaianet.SburbConnection;
import com.mraof.minestuck.world.lands.LandInfo;
import com.mraof.minestuck.world.lands.LandTypePair;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
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
	private static final Map<ResourceKey<Level>, LandInfo> typeToInfoContainer = new HashMap<>();
	
	public static ResourceKey<Level> SKAIA = ResourceKey.create(Registry.DIMENSION_REGISTRY, new ResourceLocation(Minestuck.MOD_ID, "skaia"));
	public static final ResourceLocation LAND_EFFECTS = new ResourceLocation(Minestuck.MOD_ID, "land");
	
	public static LandInfo getLandInfo(Level level)
	{
		return getLandInfo(level.getServer(), level.dimension());
	}
	
	public static LandInfo getLandInfo(MinecraftServer server, ResourceKey<Level> dimension)
	{
		Objects.requireNonNull(server);
		return typeToInfoContainer.get(dimension);
	}
	
	public static boolean isLandDimension(MinecraftServer server, ResourceKey<Level> level)
	{
		Objects.requireNonNull(server);
		return typeToInfoContainer.containsKey(level);
	}
	
	public static boolean isSkaia(ResourceKey<Level> dimension)
	{
		return dimension == SKAIA;
	}
	
	public static void updateLandMaps(MinecraftServer server, SburbConnection connection, boolean shouldSendUpdate)
	{
		typeToInfoContainer.put(connection.getLandInfo().getDimensionType(), connection.getLandInfo());
		
		if (shouldSendUpdate)
		{
			MSPacketHandler.sendToAll(createLandTypesPacket(server));
		}
	}
	
	public static void clear()
	{
		typeToInfoContainer.clear();
	}
	
	public static void sendDimensionData(ServerPlayer player)
	{
		MSPacketHandler.sendToPlayer(createLandTypesPacket(player.getServer()), player);
	}
	
	private static LandTypesDataPacket createLandTypesPacket(MinecraftServer server)
	{
		ImmutableMap.Builder<ResourceKey<Level>, LandTypePair> builder = new ImmutableMap.Builder<>();
		
		for(ResourceKey<Level> levelKey : server.levelKeys())
		{
			LandTypePair.getTypes(server, levelKey)
					.ifPresent(landTypes -> builder.put(levelKey, landTypes));
		}
		
		return new LandTypesDataPacket(builder.build());
	}
}