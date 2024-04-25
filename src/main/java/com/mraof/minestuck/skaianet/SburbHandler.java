package com.mraof.minestuck.skaianet;

import com.mraof.minestuck.MinestuckConfig;
import com.mraof.minestuck.advancements.MSCriteriaTriggers;
import com.mraof.minestuck.api.alchemy.GristType;
import com.mraof.minestuck.api.alchemy.GristTypeSpawnCategory;
import com.mraof.minestuck.computer.editmode.EditmodeLocations;
import com.mraof.minestuck.event.OnEntryEvent;
import com.mraof.minestuck.player.*;
import com.mraof.minestuck.util.ColorHandler;
import com.mraof.minestuck.world.DynamicDimensions;
import com.mraof.minestuck.world.MSDimensions;
import com.mraof.minestuck.world.lands.LandTypePair;
import com.mraof.minestuck.world.lands.LandTypes;
import com.mraof.minestuck.world.lands.terrain.TerrainLandType;
import com.mraof.minestuck.world.lands.title.TitleLandType;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundSetSubtitleTextPacket;
import net.minecraft.network.protocol.game.ClientboundSetTitleTextPacket;
import net.minecraft.network.protocol.game.ClientboundSetTitlesAnimationPacket;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.common.NeoForge;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Random;

/**
 * A class for managing sburb-related stuff from outside this package that is dependent on connections and sessions.
 * For example: Titles, land aspects, entry items etc.
 * @author kirderf1
 */
public final class SburbHandler
{
	private static final Logger LOGGER = LogManager.getLogger();
	
	public static final String CHAT_LAND_ENTRY = "minestuck.chat_land_entry";
	
	private static Title produceTitle(PlayerIdentifier player, MinecraftServer mcServer)
	{
		SkaianetData skaianetData = SkaianetData.get(mcServer);
		
		Title title = null;
		Optional<PredefineData> data = skaianetData.getOrCreatePredefineData(player);
		if(data.isPresent())
			title = data.get().getTitle();
		
		if(title == null)
			title = Generator.generateTitle(player, EnumAspect.valuesSet(), skaianetData);
		
		return title;
	}
	
	static void generateAndSetTitle(PlayerIdentifier player, MinecraftServer mcServer)
	{
		PlayerData data = PlayerData.get(player, mcServer);
		if(Title.getTitle(data).isEmpty())
		{
			Title title = produceTitle(player, mcServer);
			if(title == null)
				return;
			Title.setTitle(data, title);
		} else if(!MinestuckConfig.SERVER.playerSelectedTitle.get())
			LOGGER.warn("Trying to generate a title for {} when a title is already assigned!", player.getUsername());
	}
	
	public static ItemStack getEntryItem(Level level, SburbPlayerData playerData)
	{
		int color =  ColorHandler.getColorForPlayer(playerData.playerId(), level);
		
		return ColorHandler.setColor(playerData.artifactType.createItemStack(), color);
	}
	
	/**
	 * Calculates the tier of deployables available to the given client player.
	 * Starts at 0 with the first client-server pair, and increases by one for each pair.
	 * If the session chain is sealed and everyone has entered, the tier will be Integer.MAX_VALUE.
	 */
	public static int availableTier(MinecraftServer mcServer, PlayerIdentifier client)
	{
		SessionHandler handler = SessionHandler.get(mcServer);
		Session s = handler.getOrCreateSession(client);
		
		if(handler.doesSessionHaveMaxTier(s))
			return Integer.MAX_VALUE;
		
		int count = -1;
		for(PlayerIdentifier player : s.getPlayers())
			if(SburbPlayerData.get(player, mcServer).hasEntered())
				count++;
		if(!SburbPlayerData.get(client, mcServer).hasEntered())
			count++;
		
		return count;
	}
	
	private static LandTypePair genLandAspects(MinecraftServer mcServer, PlayerIdentifier player)
	{
		SkaianetData skaianetData = SkaianetData.get(mcServer);
		List<PlayerIdentifier> otherPlayers = skaianetData.sessionHandler.playersToCheckForDataSelection(player).toList();
		Title title = Title.getTitle(player, mcServer).orElseThrow();
		TitleLandType titleLandType = null;
		TerrainLandType terrainLandType = null;
		
		Optional<PredefineData> data = skaianetData.getOrCreatePredefineData(player);
		if(data.isPresent())
		{
			titleLandType = data.get().getTitleLandType();
			terrainLandType = data.get().getTerrainLandType();
		}
		
		if(titleLandType == null)
		{
			if(title.heroAspect() == EnumAspect.SPACE && !Generator.titleLandTypesUsedBy(otherPlayers, skaianetData).contains(LandTypes.FROGS.get()) &&
					(terrainLandType == null || LandTypes.FROGS.get().isAspectCompatible(terrainLandType)))
				titleLandType = LandTypes.FROGS.get();
			else
			{
				titleLandType = Generator.generateWeightedTitleLandType(otherPlayers, title.heroAspect(), terrainLandType, skaianetData);
				if(terrainLandType != null && titleLandType == LandTypes.TITLE_NULL.get())
				{
					LOGGER.warn("Failed to find a title land aspect compatible with land aspect \"{}\". Forced to use a poorly compatible land aspect instead.", LandTypes.TERRAIN_REGISTRY.getKey(terrainLandType));
					titleLandType = Generator.generateWeightedTitleLandType(otherPlayers, title.heroAspect(), null, skaianetData);
				}
			}
		}
		if(terrainLandType == null)
			terrainLandType = Generator.generateWeightedTerrainLandType(otherPlayers, titleLandType, skaianetData);
		
		return new LandTypePair(terrainLandType, titleLandType);
	}
	
	public static void onEntryItemsDeployed(MinecraftServer mcServer, PlayerIdentifier player)
	{
		var connections = SburbConnections.get(mcServer);
		Optional<ActiveConnection> connection = connections.getActiveConnection(player);
		if(connection.isPresent() && !connections.hasPrimaryConnectionForClient(player))
			connections.setPrimaryConnection(connection.get());
	}
	
	public static ResourceKey<Level> prepareEntry(PlayerIdentifier player, MinecraftServer mcServer)
	{
		SkaianetData skaianetData = SkaianetData.get(mcServer);
		SburbPlayerData playerData = skaianetData.getOrCreateData(player);
		if(playerData.getLandDimension() != null)
			return playerData.getLandDimension();
		
		PlayerIdentifier identifier = playerData.playerId();
		
		generateAndSetTitle(identifier, mcServer);
		
		LandTypePair landTypes = genLandAspects(mcServer, identifier);
		
		skaianetData.connections.setPrimaryConnectionForEntry(identifier);
		ResourceKey<Level> landDimension = DynamicDimensions.createLand(mcServer, DynamicDimensions.landIdBaseForPLayer(identifier), landTypes);
		playerData.setLand(landDimension);
		MSDimensions.sendLandTypesToAll(mcServer);
		
		skaianetData.removePredefineData(identifier);
		
		return landDimension;
	}
	
	public static void onEntry(MinecraftServer server, ServerPlayer player)
	{
		PlayerIdentifier playerId = Objects.requireNonNull(IdentifierHandler.encode(player));
		SkaianetData skaianetData = SkaianetData.get(server);
		SburbPlayerData playerData = skaianetData.getOrCreateData(playerId);
		
		playerData.setHasEntered();
		skaianetData.infoTracker.markLandChainDirty();
		
		SessionHandler.get(server).getOrCreateSession(playerData.playerId()).checkIfCompleted();
		
		MSCriteriaTriggers.CRUXITE_ARTIFACT.get().trigger(player);
		
		EditmodeLocations.onEntry(server, playerData.playerId());
		
		LandTypePair.Named landTypes = LandTypePair.getNamed(player.serverLevel()).orElseThrow();
		
		//chat message
		player.sendSystemMessage(Component.translatable(CHAT_LAND_ENTRY, landTypes.asComponent()));
		
		//Title style message
		player.connection.send(new ClientboundSetTitlesAnimationPacket(90, 150, 40)); //large fade in time and total length to offset lag
		player.connection.send(new ClientboundSetTitleTextPacket(Component.empty())); //clears preexisting titles
		player.connection.send(new ClientboundSetSubtitleTextPacket(landTypes.asComponentWithLandFont()));
		
		NeoForge.EVENT_BUS.post(new OnEntryEvent(server, playerId));
	}
	
	public static boolean canSelectColor(PlayerIdentifier player, MinecraftServer mcServer)
	{
		var connections = SburbConnections.get(mcServer);
		return connections.getActiveConnection(player).isEmpty() && !connections.hasPrimaryConnectionForClient(player);
	}
	
	static void initNewData(SburbPlayerData playerData)
	{
		Random rand = new Random();	//TODO seed?
		playerData.artifactType = SburbPlayerData.ArtifactType.values()[rand.nextInt(SburbPlayerData.ArtifactType.values().length)];
		playerData.setBaseGrist(generateGristType(rand));
	}
	
	static GristType generateGristType(Random rand)
	{
		List<GristType> types = GristTypeSpawnCategory.COMMON.gristTypes().toList();
		return types.get(rand.nextInt(types.size()));
	}
}