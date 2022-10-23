package com.mraof.minestuck.skaianet;

import com.mraof.minestuck.MinestuckConfig;
import com.mraof.minestuck.advancements.MSCriteriaTriggers;
import com.mraof.minestuck.computer.editmode.DeployList;
import com.mraof.minestuck.item.MSItems;
import com.mraof.minestuck.alchemy.GristType;
import com.mraof.minestuck.player.*;
import com.mraof.minestuck.util.ColorHandler;
import com.mraof.minestuck.world.MSDimensions;
import com.mraof.minestuck.world.lands.LandTypeGenerator;
import com.mraof.minestuck.world.lands.LandTypePair;
import com.mraof.minestuck.world.lands.LandTypes;
import com.mraof.minestuck.world.lands.terrain.TerrainLandType;
import com.mraof.minestuck.world.lands.title.TitleLandType;
import net.minecraft.Util;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.Item;
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
	
	public static final String LAND_ENTRY = "minestuck.land_entry";
	
	private static Title produceTitle(Level level, PlayerIdentifier player)
	{
		Session session = SessionHandler.get(level).getPlayerSession(player);
		if(session == null)
			if(MinestuckConfig.SERVER.playerSelectedTitle.get())
				session = new Session();
			else
			{
				LOGGER.warn("Trying to generate a title for {} before creating a session!", player.getUsername(), new Throwable().fillInStackTrace());
				return null;
			}
		
		Title title = null;
		if(session.predefinedPlayers.containsKey(player))
		{
			PredefineData data = session.predefinedPlayers.get(player);
			title = data.getTitle();
		}
		
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
		SessionHandler.get(player.server).findOrCreateAndCall(identifier, session -> session.predefineCall(identifier, consumer));
	}
	
	/**
	 * @param c The connection.
	 * @return Damage value for the entry item
	 */
	public static ItemStack getEntryItem(Level level, SburbConnection c)
	{
		int color =  ColorHandler.getColorForPlayer(c.getClientIdentifier(), level);
		
		Item artifact = c.artifactType == 1 ? MSItems.CRUXITE_POTION.get() : MSItems.CRUXITE_APPLE.get();
		
		return ColorHandler.setColor(new ItemStack(artifact), color);
	}
	
	public static SburbConnection getConnectionForDimension(ServerLevel level)
	{
		return getConnectionForDimension(level.getServer(), level.dimension());
	}
	public static SburbConnection getConnectionForDimension(MinecraftServer mcServer, ResourceKey<Level> dim)
	{
		if(dim == null)
			return null;
		
		return SessionHandler.get(mcServer).getConnectionStream().filter(c -> c.getClientDimension() == dim)
				.findAny().orElse(null);
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
		SburbConnection c = SkaianetHandler.get(mcServer).getActiveConnection(client);
		if(c == null)
			return -1;
		int count = -1;
		for(SburbConnection conn : s.connections)
			if(conn.hasEntered())
				count++;
		if(!c.hasEntered())
			count++;
		return count;
	}
	
	private static LandTypePair genLandAspects(MinecraftServer mcServer, SburbConnection connection)
	{
		Session session = SessionHandler.get(mcServer).getPlayerSession(connection.getClientIdentifier());
		Title title = PlayerSavedData.getData(connection.getClientIdentifier(), mcServer).getTitle();
		TitleLandType titleLandType = null;
		TerrainLandType terrainLandType = null;
		
		if(session.predefinedPlayers.containsKey(connection.getClientIdentifier()))
		{
			PredefineData data = session.predefinedPlayers.get(connection.getClientIdentifier());
			titleLandType = data.getTitleLandType();
			terrainLandType = data.getTerrainLandType();
		}
		
		if(titleLandType == null)
		{
			if(title.getHeroAspect() == EnumAspect.SPACE && !session.getUsedTitleLandTypes(mcServer).contains(LandTypes.FROGS.get()) &&
					(terrainLandType == null || LandTypes.FROGS.get().isAspectCompatible(terrainLandType)))
				titleLandType = LandTypes.FROGS.get();
			else
			{
				titleLandType = Generator.generateWeightedTitleLandType(mcServer, session, title.getHeroAspect(), terrainLandType, connection.getClientIdentifier());
				if(terrainLandType != null && titleLandType == LandTypes.TITLE_NULL.get())
				{
					LOGGER.warn("Failed to find a title land aspect compatible with land aspect \"{}\". Forced to use a poorly compatible land aspect instead.", terrainLandType.getRegistryName());
					titleLandType = Generator.generateWeightedTitleLandType(mcServer, session, title.getHeroAspect(), null, connection.getClientIdentifier());
				}
			}
		}
		if(terrainLandType == null)
			terrainLandType = Generator.generateWeightedTerrainLandType(mcServer, session, titleLandType, connection.getClientIdentifier());
		
		return new LandTypePair(terrainLandType, titleLandType);
	}
	
	public static boolean giveItems(MinecraftServer mcServer, PlayerIdentifier player)
	{
		SkaianetHandler handler = SkaianetHandler.get(mcServer);
		SburbConnection c = handler.getPrimaryConnection(player, true).orElse(null);
		if(c != null && !c.isMain())
		{
			onFirstItemGiven(c);
			return true;
		}
		return false;
	}
	
	static void onFirstItemGiven(SburbConnection connection)
	{
		connection.setIsMain();
	}
	
	static void prepareEntry(MinecraftServer mcServer, SburbConnection c)
	{
		PlayerIdentifier identifier = c.getClientIdentifier();
		
		generateAndSetTitle(mcServer.getLevel(Level.OVERWORLD), c.getClientIdentifier());
		LandTypePair landTypes = genLandAspects(mcServer, c);		//This is where the Land dimension is actually registered, but it also needs the player's Title to be determined.
		
		ResourceKey<Level> dimType = LandTypeGenerator.createLandDimension(mcServer, identifier, landTypes);
		MSDimensions.sendLandTypesToAll(mcServer);
		
		c.setLand(dimType);
	}
	
	static void onEntry(MinecraftServer server, SburbConnection c)
	{
		c.setHasEntered();
		
		SessionHandler.get(server).getPlayerSession(c.getClientIdentifier()).checkIfCompleted();
		
		ServerPlayer player = c.getClientIdentifier().getPlayer(server);
		if(player != null)
		{
			MSCriteriaTriggers.CRUXITE_ARTIFACT.trigger(player);
			
			LandTypePair.Named landTypes = LandTypePair.getNamed(player.getLevel()).orElseThrow();
			player.sendMessage(new TranslatableComponent(LAND_ENTRY, landTypes.asComponent()), Util.NIL_UUID);
		}
	}
	
	public static boolean canSelectColor(PlayerIdentifier player, MinecraftServer mcServer)
	{
		return SessionHandler.get(mcServer).getConnectionStream().noneMatch(c -> c.getClientIdentifier().equals(player));
	}
	
	public static boolean hasEntered(ServerPlayer player)
	{
		PlayerIdentifier identifier = IdentifierHandler.encode(player);
		Optional<SburbConnection> c = SkaianetHandler.get(player.server).getPrimaryConnection(identifier, true);
		return c.isPresent() && c.get().hasEntered();
	}
	
	/**
	 * Extra behavior on the creation of a connection, such as generating the artifact type.
	 */
	static void onConnectionCreated(SburbConnection c)
	{
		Random rand = new Random();	//TODO seed?
		c.artifactType = rand.nextInt(2);
		LOGGER.info("Randomized artifact type to be: {} for player {}.", c.artifactType, c.getClientIdentifier().getUsername());
		c.setBaseGrist(generateGristType(rand));
	}
	
	static GristType generateGristType(Random rand)
	{
		List<GristType> types = GristType.SpawnCategory.COMMON.gristTypes().toList();
		return types.get(rand.nextInt(types.size()));
	}
	
	public static void resetGivenItems(MinecraftServer mcServer)
	{
		SessionHandler.get(mcServer).getConnectionStream().forEach(SburbConnection::resetGivenItems);
		
		DeployList.onConditionsUpdated(mcServer);
	}
}