package com.mraof.minestuck.skaianet;

import com.mraof.minestuck.network.DataCheckerPacket;
import com.mraof.minestuck.network.MSPacketHandler;
import com.mraof.minestuck.player.PlayerIdentifier;
import com.mraof.minestuck.player.Title;
import com.mraof.minestuck.util.DataCheckerPermission;
import com.mraof.minestuck.world.lands.terrain.TerrainLandType;
import com.mraof.minestuck.world.lands.title.TitleLandType;
import com.mraof.minestuck.world.storage.PlayerSavedData;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.server.level.ServerPlayer;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class DataCheckerManager
{
	public static void onDataRequest(ServerPlayer player, int index)
	{
		if(DataCheckerPermission.hasPermission(player))
		{
			CompoundTag data = createDataTag(SessionHandler.get(player.level));
			MSPacketHandler.sendToPlayer(DataCheckerPacket.data(index, data), player);
		}
	}
	/**
	 * Creates data to be used for the data checker
	 */
	private static CompoundTag createDataTag(SessionHandler handler)
	{
		CompoundTag nbt = new CompoundTag();
		ListTag sessionList = new ListTag();
		nbt.put("sessions", sessionList);
		for(Session session : handler.getSessions())
		{
			sessionList.add(createSessionDataTag(session));
		}
		return nbt;
	}
	
	private static CompoundTag createSessionDataTag(Session session)
	{
		ListTag connectionList = new ListTag();
		Set<PlayerIdentifier> playerSet = new HashSet<>();
		for(SburbConnection c : session.connections)
		{
			connectionList.add(createConnectionDataTag(c, playerSet, session.predefinedPlayers));
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
	private static CompoundTag createConnectionDataTag(SburbConnection connection, Set<PlayerIdentifier> playerSet, Map<PlayerIdentifier, PredefineData> predefinedPlayers)
	{
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
			if(connection.getLandInfo() != null)
			{
				connectionTag.putString("clientDim", connection.getClientDimension().getRegistryName().toString());
				connectionTag.putString("landType1", connection.getLandInfo().landName1());
				connectionTag.putString("landType2", connection.getLandInfo().landName2());
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
			nbt.putString("terrainLandType", terrainType.getRegistryName().toString());
		if(titleType != null)
			nbt.putString("titleLandType", titleType.getRegistryName().toString());
	}
}
