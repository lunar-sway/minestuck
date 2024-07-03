package com.mraof.minestuck.skaianet;

import com.mraof.minestuck.network.DataCheckerPackets;
import com.mraof.minestuck.player.PlayerIdentifier;
import com.mraof.minestuck.player.Title;
import com.mraof.minestuck.world.lands.LandTypePair;
import com.mraof.minestuck.world.lands.LandTypes;
import com.mraof.minestuck.world.lands.terrain.TerrainLandType;
import com.mraof.minestuck.world.lands.title.TitleLandType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.network.PacketDistributor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Optional;

public class DataCheckerManager
{
	private static final Logger LOGGER = LogManager.getLogger();
	
	public static void onDataRequest(ServerPlayer player, int index)
	{
		if(DataCheckerPermission.hasPermission(player))
		{
			CompoundTag data = createDataTag(player.server, SessionHandler.get(player.server));
			PacketDistributor.PLAYER.with(player).send(new DataCheckerPackets.Data(index, data));
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
	
	private static CompoundTag createSessionDataTag(MinecraftServer mcServer, Session session)
	{
		ListTag connectionList = new ListTag();
		for(PlayerIdentifier player : session.getPlayers())
			connectionList.add(createPlayerDataTag(player, mcServer));
		
		CompoundTag sessionTag = new CompoundTag();
		sessionTag.put("connections", connectionList);
		return sessionTag;
	}
	
	private static CompoundTag createPlayerDataTag(PlayerIdentifier player, MinecraftServer mcServer)
	{
		CompoundTag tag = new CompoundTag();
		
		tag.putString("client", player.getUsername());
		tag.putString("clientId", player.getCommandString());
		
		writeConnectionData(tag, player, mcServer);
		writeExtraData(tag, player, mcServer);
		
		return tag;
	}
	
	private static void writeConnectionData(CompoundTag tag, PlayerIdentifier player, MinecraftServer mcServer)
	{
		var connections = SburbConnections.get(mcServer);
		
		boolean isMain = connections.hasPrimaryConnectionForClient(player);
		tag.putBoolean("isMain", isMain);
		
		if(isMain)
			connections.primaryPartnerForClient(player)
					.ifPresent(serverPlayer -> tag.putString("server", serverPlayer.getUsername()));
		else
			connections.getActiveConnection(player)
					.ifPresent(connection -> tag.putString("server", connection.server().getUsername()));
	}
	
	private static void writeExtraData(CompoundTag tag, PlayerIdentifier player, MinecraftServer mcServer)
	{
		SkaianetData skaianetData = SkaianetData.get(mcServer);
		SburbPlayerData playerData = skaianetData.getOrCreateData(player);
		ResourceKey<Level> landDimensionKey = playerData.getLandDimension();
		if(landDimensionKey != null)
		{
			writeEntryPreparedData(tag, player, landDimensionKey, mcServer);
			return;
		}
		
		skaianetData.getOrCreatePredefineData(player).ifPresent(predefineData -> putPredefinedData(tag, predefineData));
	}
	
	/**
	 * When entry is prepared, the land types and player title is generated or taken from predefined data.
	 * This function is meant to write that data.
	 */
	private static void writeEntryPreparedData(CompoundTag tag, PlayerIdentifier player,
											   ResourceKey<Level> landDimensionKey, MinecraftServer mcServer)
	{
		tag.putString("clientDim", landDimensionKey.location().toString());
		
		Optional<LandTypePair.Named> landTypes = LandTypePair.getNamed(mcServer, landDimensionKey);
		landTypes.flatMap(named -> LandTypePair.Named.CODEC.encodeStart(NbtOps.INSTANCE, named).resultOrPartial(LOGGER::error))
				.ifPresent(landPairTag -> tag.put("landTypes", landPairTag));
		
		Title.getTitle(player, mcServer).ifPresent(title ->
		{
			tag.putByte("class", (byte) title.heroClass().ordinal());
			tag.putByte("aspect", (byte) title.heroAspect().ordinal());
		});
	}
	
	private static void putPredefinedData(CompoundTag tag, PredefineData data)
	{
		Title title = data.getTitle();
		if(title != null)
		{
			tag.putByte("class", (byte) data.getTitle().heroClass().ordinal());
			tag.putByte("aspect", (byte) data.getTitle().heroAspect().ordinal());
		}
		
		TerrainLandType terrainType = data.getTerrainLandType();
		TitleLandType titleType = data.getTitleLandType();
		if(terrainType != null)
			tag.putString("terrainLandType", LandTypes.TERRAIN_REGISTRY.getKey(terrainType).toString());
		if(titleType != null)
			tag.putString("titleLandType", LandTypes.TITLE_REGISTRY.getKey(titleType).toString());
	}
}
