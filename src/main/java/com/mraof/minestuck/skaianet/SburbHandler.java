package com.mraof.minestuck.skaianet;

import com.mraof.minestuck.MinestuckConfig;
import com.mraof.minestuck.advancements.MSCriteriaTriggers;
import com.mraof.minestuck.api.alchemy.GristType;
import com.mraof.minestuck.api.alchemy.GristTypeSpawnCategory;
import com.mraof.minestuck.computer.editmode.DeployList;
import com.mraof.minestuck.computer.editmode.EditmodeLocations;
import com.mraof.minestuck.player.*;
import com.mraof.minestuck.util.ColorHandler;
import com.mraof.minestuck.world.MSDimensions;
import com.mraof.minestuck.world.lands.LandTypePair;
import com.mraof.minestuck.world.lands.LandTypes;
import com.mraof.minestuck.world.lands.gen.LandTypeGenerator;
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
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
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
	
	private static Title produceTitle(Level level, PlayerIdentifier player)
	{
		SkaianetHandler skaianetHandler = SkaianetHandler.get(level);
		Session session = skaianetHandler.sessionHandler.getPlayerSession(player);
		if(session == null)
		{
			if(MinestuckConfig.SERVER.playerSelectedTitle.get())
				session = new Session(skaianetHandler);
			else
			{
				LOGGER.warn("Trying to generate a title for {} before creating a session!", player.getUsername(), new Throwable().fillInStackTrace());
				return null;
			}
		}
		
		Title title = null;
		Optional<PredefineData> data = skaianetHandler.predefineData(player);
		if(data.isPresent())
			title = data.get().getTitle();
		
		if(title == null)
		{
			try
			{
				title = Generator.generateTitle(session, EnumAspect.valuesSet(), player);
			} catch(SkaianetException e)
			{
				return null;	//TODO handle exception further down the line
			}
		}
		return title;
	}
	
	static void generateAndSetTitle(Level level, PlayerIdentifier player)
	{
		PlayerData data = PlayerSavedData.getData(player, level);
		if(data.getTitle() == null)
		{
			Title title = produceTitle(level, player);
			if(title == null)
				return;
			PlayerSavedData.getData(player, level).setTitle(title);
		} else if(!MinestuckConfig.SERVER.playerSelectedTitle.get())
			LOGGER.warn("Trying to generate a title for {} when a title is already assigned!", player.getUsername());
	}
	
	public static void handlePredefineData(ServerPlayer player, SkaianetException.SkaianetConsumer<PredefineData> consumer) throws SkaianetException
	{
		PlayerIdentifier identifier = IdentifierHandler.encode(player);
		SkaianetHandler.get(player.server).predefineCall(identifier, consumer);
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
		Session s = handler.getPlayerSession(client);
		if(s == null)
			return -1;
		if(handler.doesSessionHaveMaxTier(s))
			return Integer.MAX_VALUE;
		
		int count = -1;
		for(PlayerIdentifier player : s.getPlayerList())
			if(SburbPlayerData.get(player, mcServer).hasEntered())
				count++;
		if(!SburbPlayerData.get(client, mcServer).hasEntered())
			count++;
		
		return count;
	}
	
	private static LandTypePair genLandAspects(MinecraftServer mcServer, PlayerIdentifier player)
	{
		SkaianetHandler skaianetHandler = SkaianetHandler.get(mcServer);
		Session session = skaianetHandler.sessionHandler.getPlayerSession(player);
		Title title = PlayerSavedData.getData(player, mcServer).getTitle();
		TitleLandType titleLandType = null;
		TerrainLandType terrainLandType = null;
		
		Optional<PredefineData> data = skaianetHandler.predefineData(player);
		if(data.isPresent())
		{
			titleLandType = data.get().getTitleLandType();
			terrainLandType = data.get().getTerrainLandType();
		}
		
		if(titleLandType == null)
		{
			if(title.getHeroAspect() == EnumAspect.SPACE && !session.getUsedTitleLandTypes().contains(LandTypes.FROGS.get()) &&
					(terrainLandType == null || LandTypes.FROGS.get().isAspectCompatible(terrainLandType)))
				titleLandType = LandTypes.FROGS.get();
			else
			{
				titleLandType = Generator.generateWeightedTitleLandType(session, title.getHeroAspect(), terrainLandType, player);
				if(terrainLandType != null && titleLandType == LandTypes.TITLE_NULL.get())
				{
					LOGGER.warn("Failed to find a title land aspect compatible with land aspect \"{}\". Forced to use a poorly compatible land aspect instead.", LandTypes.TERRAIN_REGISTRY.get().getKey(terrainLandType));
					titleLandType = Generator.generateWeightedTitleLandType(session, title.getHeroAspect(), null, player);
				}
			}
		}
		if(terrainLandType == null)
			terrainLandType = Generator.generateWeightedTerrainLandType(session, titleLandType, player);
		
		return new LandTypePair(terrainLandType, titleLandType);
	}
	
	public static void onEntryItemsDeployed(MinecraftServer mcServer, PlayerIdentifier player)
	{
		SkaianetHandler handler = SkaianetHandler.get(mcServer);
		Optional<ActiveConnection> connection = handler.getActiveConnection(player);
		if(connection.isPresent() && !handler.hasPrimaryConnectionForClient(player))
			handler.trySetPrimaryConnection(connection.get());
	}
	
	static void prepareEntry(MinecraftServer mcServer, SburbPlayerData playerData)
	{
		PlayerIdentifier identifier = playerData.playerId();
		
		generateAndSetTitle(mcServer.getLevel(Level.OVERWORLD), identifier);
		LandTypePair landTypes = genLandAspects(mcServer, identifier);		//This is where the Land dimension is actually registered, but it also needs the player's Title to be determined.
		
		ResourceKey<Level> dimType = LandTypeGenerator.createLandDimension(mcServer, identifier, landTypes);
		MSDimensions.sendLandTypesToAll(mcServer);
		
		playerData.setLand(dimType);
	}
	
	static void onEntry(MinecraftServer server, SburbPlayerData playerData)
	{
		playerData.setHasEntered();
		
		SessionHandler.get(server).getPlayerSession(playerData.playerId()).checkIfCompleted();
		
		ServerPlayer player = playerData.playerId().getPlayer(server);
		if(player != null)
		{
			MSCriteriaTriggers.CRUXITE_ARTIFACT.trigger(player);
			
			EditmodeLocations.onEntry(server, playerData.playerId());
			
			LandTypePair.Named landTypes = LandTypePair.getNamed(player.serverLevel()).orElseThrow();
			
			//chat message
			player.sendSystemMessage(Component.translatable(CHAT_LAND_ENTRY, landTypes.asComponent()));
			
			//Title style message
			player.connection.send(new ClientboundSetTitlesAnimationPacket(90, 150, 40)); //large fade in time and total length to offset lag
			player.connection.send(new ClientboundSetTitleTextPacket(Component.empty())); //clears preexisting titles
			player.connection.send(new ClientboundSetSubtitleTextPacket(landTypes.asComponentWithLandFont()));
		}
	}
	
	public static boolean canSelectColor(PlayerIdentifier player, MinecraftServer mcServer)
	{
		SkaianetHandler skaianetHandler = SkaianetHandler.get(mcServer);
		return skaianetHandler.getActiveConnection(player).isEmpty() && !skaianetHandler.hasPrimaryConnectionForClient(player);
	}
	
	static void initNewData(SburbPlayerData playerData)
	{
		Random rand = new Random();	//TODO seed?
		playerData.artifactType = SburbPlayerData.ArtifactType.values()[rand.nextInt(SburbPlayerData.ArtifactType.values().length)];
		LOGGER.info("Randomized artifact type to be: {} for player {}.", playerData.artifactType.name(), playerData.playerId().getUsername());
		playerData.setBaseGrist(generateGristType(rand));
	}
	
	static GristType generateGristType(Random rand)
	{
		List<GristType> types = GristTypeSpawnCategory.COMMON.gristTypes().toList();
		return types.get(rand.nextInt(types.size()));
	}
	
	public static void resetGivenItems(MinecraftServer mcServer)
	{
		SkaianetHandler.get(mcServer).allPlayerData().forEach(SburbPlayerData::resetGivenItems);
		
		DeployList.onConditionsUpdated(mcServer);
	}
}