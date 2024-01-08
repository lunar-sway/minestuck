package com.mraof.minestuck.skaianet;

import com.mraof.minestuck.network.DataCheckerPacket;
import com.mraof.minestuck.network.MSPacketHandler;
import com.mraof.minestuck.player.PlayerIdentifier;
import com.mraof.minestuck.player.Title;
import com.mraof.minestuck.util.DataCheckerPermission;
import com.mraof.minestuck.world.lands.LandTypePair;
import com.mraof.minestuck.world.lands.LandTypes;
import com.mraof.minestuck.world.lands.terrain.TerrainLandType;
import com.mraof.minestuck.world.lands.title.TitleLandType;
import com.mraof.minestuck.player.PlayerSavedData;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public class DataCheckerManager
{
	private static final Logger LOGGER = LogManager.getLogger();
	
	public static void onDataRequest(ServerPlayer player, int index)
	{
		if(DataCheckerPermission.hasPermission(player))
		{
			CompoundTag data = createDataTag(player.server, SessionHandler.get(player.level()));
			MSPacketHandler.sendToPlayer(DataCheckerPacket.data(index, data), player);
		}
	}
	/**
	 * Creates data to be used for the data checker
	 */
	private static CompoundTag createDataTag(MinecraftServer server, SessionHandler handler)
	{
		CompoundTag nbt = new CompoundTag();
		ListTag sessionList = new ListTag();
		nbt.put("sessions", sessionList);
		for(Session session : handler.getSessions())
		{
			sessionList.add(createSessionDataTag(server, session));
		}
		return nbt;
	}
	
	private static CompoundTag createSessionDataTag(MinecraftServer server, Session session)
	{
		ListTag connectionList = new ListTag();
		Set<PlayerIdentifier> playerSet = new HashSet<>();
		for(SburbConnection c : session.connections)
		{
			connectionList.add(createConnectionDataTag(server, c, playerSet, session.predefinedPlayers));
		}
		
		for(Map.Entry<PlayerIdentifier, PredefineData> entry : session.predefinedPlayers.entrySet())
		{
			if(playerSet.contains(entry.getKey()))
				continue;
			
			connectionList.add(createPredefineDataTag(entry.getKey(), entry.getValue()));
		}
		
		CompoundTag sessionTag = new CompoundTag();
		if(session.name != null)
			sessionTag.putString("name", session.name);
		sessionTag.put("connections", connectionList);
		return sessionTag;
	}
	
	/**
	 * Creates data for this connection to be sent to the data checker screen
	 */
	private static CompoundTag createConnectionDataTag(MinecraftServer server, SburbConnection connection, Set<PlayerIdentifier> playerSet, Map<PlayerIdentifier, PredefineData> predefinedPlayers)
	{
		SburbPlayerData playerData = SburbPlayerData.get(connection.getClientIdentifier(), server);
		if(connection.isMain())
			playerSet.add(connection.getClientIdentifier());
		CompoundTag connectionTag = new CompoundTag();
		connectionTag.putString("client", connection.getClientIdentifier().getUsername());
		connectionTag.putString("clientId", connection.getClientIdentifier().getCommandString());
		if(connection.hasServerPlayer())
			connectionTag.putString("server", connection.getServerIdentifier().getUsername());
		connectionTag.putBoolean("isMain", connection.isMain());
		connectionTag.putBoolean("isActive", connection.isActive());
		if(connection.isMain())
		{
			ResourceKey<Level> landDimensionKey = playerData.getLandDimension();
			if(landDimensionKey != null)
			{
				connectionTag.putString("clientDim", landDimensionKey.location().toString());
				
				Optional<LandTypePair.Named> landTypes = LandTypePair.getNamed(server, landDimensionKey);
				landTypes.flatMap(named -> LandTypePair.Named.CODEC.encodeStart(NbtOps.INSTANCE, named).resultOrPartial(LOGGER::error))
						.ifPresent(tag -> connectionTag.put("landTypes", tag));
				
				Title title = PlayerSavedData.getData(connection.getClientIdentifier(), connection.skaianet.mcServer).getTitle();
				if(title != null)
				{
					connectionTag.putByte("class", (byte) title.getHeroClass().ordinal());
					connectionTag.putByte("aspect", (byte) title.getHeroAspect().ordinal());
				}
			} else if(predefinedPlayers.containsKey(connection.getClientIdentifier()))
			{
				PredefineData data = predefinedPlayers.get(connection.getClientIdentifier());
				putPredefinedDataToTag(connectionTag, data);
			}
		}
		return connectionTag;
	}
	
	/**
	 * Creates data to be sent to the data checker screen for players with predefined data but without a connection
	 */
	private static CompoundTag createPredefineDataTag(PlayerIdentifier identifier, PredefineData data)
	{
		CompoundTag connectionTag = new CompoundTag();
		
		connectionTag.putString("client", identifier.getUsername());
		connectionTag.putString("clientId", identifier.getCommandString());
		connectionTag.putBoolean("isMain", true);
		connectionTag.putBoolean("isActive", false);
		connectionTag.putInt("clientDim", 0);
		
		putPredefinedDataToTag(connectionTag, data);
		
		return connectionTag;
	}
	
	private static void putPredefinedDataToTag(CompoundTag nbt, PredefineData data)
	{
		Title title = data.getTitle();
		if(title != null)
		{
			nbt.putByte("class", (byte) data.getTitle().getHeroClass().ordinal());
			nbt.putByte("aspect", (byte) data.getTitle().getHeroAspect().ordinal());
		}
		
		TerrainLandType terrainType = data.getTerrainLandType();
		TitleLandType titleType = data.getTitleLandType();
		if(terrainType != null)
			nbt.putString("terrainLandType", LandTypes.TERRAIN_REGISTRY.get().getKey(terrainType).toString());
		if(titleType != null)
			nbt.putString("titleLandType", LandTypes.TITLE_REGISTRY.get().getKey(titleType).toString());
	}
}
