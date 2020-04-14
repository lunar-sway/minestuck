package com.mraof.minestuck.skaianet;

import com.mraof.minestuck.MinestuckConfig;
import com.mraof.minestuck.advancements.MSCriteriaTriggers;
import com.mraof.minestuck.item.MSItems;
import com.mraof.minestuck.item.crafting.alchemy.GristType;
import com.mraof.minestuck.item.crafting.alchemy.GristTypes;
import com.mraof.minestuck.network.MSPacketHandler;
import com.mraof.minestuck.network.TitleSelectPacket;
import com.mraof.minestuck.player.EnumAspect;
import com.mraof.minestuck.player.IdentifierHandler;
import com.mraof.minestuck.player.PlayerIdentifier;
import com.mraof.minestuck.player.Title;
import com.mraof.minestuck.util.ColorHandler;
import com.mraof.minestuck.util.Debug;
import com.mraof.minestuck.util.EntryProcess;
import com.mraof.minestuck.util.MinestuckRandom;
import com.mraof.minestuck.world.lands.LandTypePair;
import com.mraof.minestuck.world.lands.LandTypes;
import com.mraof.minestuck.world.lands.terrain.TerrainLandType;
import com.mraof.minestuck.world.lands.title.TitleLandType;
import com.mraof.minestuck.world.storage.PlayerData;
import com.mraof.minestuck.world.storage.PlayerSavedData;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.server.ServerWorld;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * A class for managing sburb-related stuff from outside this package that is dependent on connections and sessions.
 * For example: Titles, land aspects, entry items etc.
 * @author kirderf1
 */
public final class SburbHandler
{
	static Map<PlayerEntity, Vec3d> titleSelectionMap = new HashMap<>();	//TODO Consider making this non-static
	
	private static Title produceTitle(World world, PlayerIdentifier player)
	{
		Session session = SessionHandler.get(world).getPlayerSession(player);
		if(session == null)
			if(MinestuckConfig.playerSelectedTitle.get())
				session = new Session();
			else
			{
				Debug.logger.warn(String.format("Trying to generate a title for %s before creating a session!", player.getUsername()), new Throwable().fillInStackTrace());
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
	
	private static void generateTitle(World world, PlayerIdentifier player)
	{
		PlayerData data = PlayerSavedData.getData(player, world);
		if(data.getTitle() == null)
		{
			Title title = produceTitle(world, player);
			if(title == null)
				return;
			PlayerSavedData.getData(player, world).setTitle(title);
		} else if(!MinestuckConfig.playerSelectedTitle.get())
			Debug.warnf("Trying to generate a title for %s when a title is already assigned!", player.getUsername());
	}
	
	public static void handlePredefineData(ServerPlayerEntity player, SkaianetException.SkaianetConsumer<PredefineData> consumer) throws SkaianetException
	{
		PlayerIdentifier identifier = IdentifierHandler.encode(player);
		Session session = SessionHandler.get(player.server).getPlayerSession(identifier);
		if(session != null)
			session.predefineCall(identifier, consumer);
		else
		{
			session = new Session();
			session.predefineCall(identifier, consumer);
			SessionHandler.get(player.server).addNewSession(session);
		}
	}
	
	/*public static void managePredefinedSession(MinecraftServer server, ICommandSender sender, ICommand command, String sessionName, String[] playerNames, boolean finish) throws CommandException
	{
		Session session = sessionsByName.get(sessionName);
		if(session == null)
		{
			if(finish && playerNames.length == 0)
				throw new CommandException("Couldn't find session with that name. Aborting the finalizing process.", sessionName);
			if(singleSession)
				throw new CommandException("Not allowed to create new sessions when global session is active. Use \"%s\" as session name for global session access.", GLOBAL_SESSION_NAME);
			
			if(sender.sendCommandFeedback())
				sender.sendMessage(new TextComponentString("Couldn't find session with that name, creating a new session..."));
			session = new Session();
			session.name = sessionName;
			sessions.add(session);
			sessionsByName.put(session.name, session);
		}
		
		if(session.locked)
			throw new CommandException("That session may no longer be modified.");
		
		int handled = 0;
		boolean skipFinishing = false;
		for(String playerName : playerNames)
		{
			if(playerName.startsWith("!"))
			{
				playerName = playerName.substring(1);
				
				PlayerIdentifier identifier;
				try
				{
					identifier = IdentifierHandler.getForCommand(server, sender, playerName);
				} catch(CommandException c)
				{
					if(sender.sendCommandFeedback())
						sender.sendMessage(new TextComponentString(String.format(c.getMessage(), c.getErrorObjects())));
					continue;
				}
				
				if(!session.containsPlayer(identifier))
				{
					if(sender.sendCommandFeedback())
						sender.sendMessage(new TextComponentString("Failed to remove player \""+playerName+"\": Player isn't in session.").setStyle(new Style().setColor(TextFormatting.RED)));
					continue;
				}
				
				if(session.predefinedPlayers.remove(identifier) == null)
				{
					if(sender.sendCommandFeedback())
						sender.sendMessage(new TextComponentString("Failed to remove player \""+playerName+"\": Player isn't registered with the session.").setStyle(new Style().setColor(TextFormatting.RED)));
					continue;
				}
				
				handled++;
				
				if(session.containsPlayer(identifier))
				{
					split(session);
					session = sessionsByName.get(sessionName);
					if(session.containsPlayer(identifier))
					{
						if(sender.sendCommandFeedback())
							sender.sendMessage(new TextComponentString("Removed player \""+playerName+"\", but they are still part of a connection in the session and will therefore be part of the session unless the connection is discarded.").setStyle(new Style().setColor(TextFormatting.YELLOW)));
						skipFinishing = true;
						continue;
					}
				}
			} else
			{
				PlayerIdentifier identifier;
				try
				{
					identifier = IdentifierHandler.getForCommand(server, sender, playerName);
				} catch(CommandException c)
				{
					if(sender.sendCommandFeedback())
						sender.sendMessage(new TextComponentString(String.format(c.getMessage(), c.getErrorObjects())));
					continue;
				}
				
				if(session.predefinedPlayers.containsKey(identifier))
				{
					if(sender.sendCommandFeedback())
						sender.sendMessage(new TextComponentString("Failed to add player \""+playerName+"\": Player is already registered with session.").setStyle(new Style().setColor(TextFormatting.RED)));
					continue;
				}
				
				Session playerSession = getPlayerSession(identifier);
				
				if(playerSession != null)
				{
					if(merge(session, playerSession, null) != null)
					{
						if(sender.sendCommandFeedback())
							sender.sendMessage(new TextComponentString("Failed to add player \""+playerName+"\": Can't merge with the session that the player is already in.").setStyle(new Style().setColor(TextFormatting.RED)));
						continue;
					}
				} else if(MinestuckConfig.forceMaxSize && session.getPlayerList().size() + 1 > maxSize)
				{
					if(sender.sendCommandFeedback())
						sender.sendMessage(new TextComponentString("Failed to add player \""+playerName+"\": The session can't accept more players with the current configurations.").setStyle(new Style().setColor(TextFormatting.RED)));
					continue;
				}
				
				session.predefinedPlayers.put(identifier, new PredefineData());
				handled++;
			}
		}
		
		if(playerNames.length > 0)
			CommandBase.notifyCommandListener(sender, command, "commands.sburbSession.addSuccess", handled, playerNames.length);
		
		/*if(finish)
			if(!skipFinishing && handled == playerNames.length)
			{
				SburbHandler.finishSession(sender, command, session);
				
			} else throw new CommandException("Skipping to finalize the session due to one or more issues while adding players.");*/
	/*}
	
	public static void sessionName(MinecraftServer server, ICommandSender sender, ICommand command, String playerName, String sessionName) throws CommandException
	{
		PlayerIdentifier identifier = IdentifierHandler.getForCommand(server, sender, playerName);
		
		Session playerSession = getPlayerSession(identifier), session = sessionsByName.get(sessionName);
		if(singleSession)
			throw new CommandException("Not allowed to change session name when global session is active. Use \"%s\" as session name for global session access.", GLOBAL_SESSION_NAME);
		if(playerSession == null)
			throw new CommandException("Couldn't find session for player \"%s\"", playerName);
		if(session != null)
			throw new CommandException("That session name is already taken.");
		
		if(playerSession.name != null)
			sessionsByName.remove(playerSession.name);
		String prevName = playerSession.name;
		playerSession.name = sessionName;
		sessionsByName.put(playerSession.name, playerSession);
		
		if(prevName != null)
			CommandBase.notifyCommandListener(sender, command, "commands.sburbSession.rename", prevName, sessionName, playerName);
		else CommandBase.notifyCommandListener(sender, command, "commands.sburbSession.name", sessionName, playerName);
	}
	
	public static void predefineTitle(MinecraftServer server, ICommandSender sender, ICommand command, String playerName, String sessionName, Title title) throws CommandException
	{
		PlayerIdentifier identifier = predefineCheck(server, sender, playerName, sessionName);
		
		Title playerTitle = MinestuckPlayerData.getTitle(identifier);
		if(playerTitle != null)
			throw new CommandException("You can't change your title after having entered the medium.");
		
		Session session = sessionsByName.get(sessionName);
		for(SburbConnection c : session.connections)
			if(c.isMain && c.enteredGame && title.equals(MinestuckPlayerData.getTitle(c.getClientIdentifier())))
				throw new CommandException("This title is already used by %s.", c.getClientIdentifier().getUsername());
		for(Map.Entry<PlayerIdentifier, PredefineData> entry : session.predefinedPlayers.entrySet())
			if(entry.getValue().title != null && title.equals(entry.getValue().title))
				throw new CommandException("This title is already assigned to %s.", entry.getKey().getUsername());
		
		PredefineData data = session.predefinedPlayers.get(identifier);
		data.title = title;
		TitleLandAspect landAspect = LandAspectRegistry.getSingleLandAspect(title.getHeroAspect());
		if(landAspect != null)
			data.landTitle = landAspect;	//This part could be made more robust for when landTerrain is already defined
		CommandBase.notifyCommandListener(sender, command, "commands.sburbSession.titleSuccess", playerName, title.asTextComponent());
	}
	
	public static void predefineTerrainLandAspect(MinecraftServer server, ICommandSender sender, ICommand command, String playerName, String sessionName, TerrainLandAspect aspect) throws CommandException
	{
		PlayerIdentifier identifier = predefineCheck(server, sender, playerName, sessionName);
		
		Session session = sessionsByName.get(sessionName);
		SburbConnection clientConnection = SkaianetHandler.getClientConnection(identifier);
		PredefineData data = session.predefinedPlayers.get(identifier);
		
		if(clientConnection != null && clientConnection.enteredGame())
			throw new CommandException("You can't change your land aspects after having entered the medium.");
		if(data.landTitle == null)
			throw new CommandException("You should define the other land aspect before this one.");
		if(!data.landTitle.isAspectCompatible(aspect))
			throw new CommandException("That terrain land aspect isn't compatible with the other land aspect.");
		
		data.landTerrain = aspect;
		CommandBase.notifyCommandListener(sender, command, "commands.sburbSession.landTerrainSuccess", playerName, aspect.getPrimaryName());
	}
	
	public static void predefineTitleLandAspect(MinecraftServer server, ICommandSender sender, ICommand command, String playerName, String sessionName, TitleLandAspect aspect) throws CommandException
	{
		PlayerIdentifier identifier = predefineCheck(server, sender, playerName, sessionName);
		
		Session session = sessionsByName.get(sessionName);
		SburbConnection clientConnection = SkaianetHandler.getClientConnection(identifier);
		PredefineData data = session.predefinedPlayers.get(identifier);
		
		if(clientConnection != null && clientConnection.enteredGame())
			throw new CommandException("You can't change your land aspects after having entered the medium.");
		if(sender.sendCommandFeedback())
			if(data.title == null)
				sender.sendMessage(new TextComponentString("Beware that the title generated might not be suited for this land aspect.").setStyle(new Style().setColor(TextFormatting.YELLOW)));
			else if(!LandAspectRegistry.containsTitleLandAspect(data.title.getHeroAspect(), aspect))
				sender.sendMessage(new TextComponentString("Beware that the title predefined isn't suited for this land aspect.").setStyle(new Style().setColor(TextFormatting.YELLOW)));
		
		if(data.landTerrain != null && !aspect.isAspectCompatible(data.landTerrain))
		{
			data.landTerrain = null;
			if(sender.sendCommandFeedback())
				sender.sendMessage(new TextComponentString("The terrain aspect previously chosen isn't compatible with this land aspect, and has therefore been removed.").setStyle(new Style().setColor(TextFormatting.YELLOW)));
		}
		
		data.landTitle = aspect;
		CommandBase.notifyCommandListener(sender, command, "commands.sburbSession.landTitleSuccess", playerName, aspect.getPrimaryName());
	}
	
	private static PlayerIdentifier predefineCheck(MinecraftServer server, ICommandSender sender, String playerName, String sessionName) throws CommandException
	{
		PlayerIdentifier identifier = IdentifierHandler.getForCommand(server, sender, playerName);
		
		Session session = sessionsByName.get(sessionName), playerSession = getPlayerSession(identifier);
		if(session == null)
		{
			if(singleSession)
				throw new CommandException("Not allowed to create new sessions when global session is active. Use \"%s\" as session name for global session access.", GLOBAL_SESSION_NAME);
			
			if(sender.sendCommandFeedback())
				sender.sendMessage(new TextComponentString("Couldn't find session with that name, creating a new session..."));
			session = new Session();
			session.name = sessionName;
			sessions.add(session);
			sessionsByName.put(session.name, session);
		}
		/*if(session == null)
			throw new CommandException("Couldn't find session with the name %s", sessionName);*/
		/*if(playerSession != null && session != playerSession)
			throw new CommandException("The player is already in another session!");
		if(playerSession == null || !session.predefinedPlayers.containsKey(identifier))
		{
			if(sender.sendCommandFeedback())
				sender.sendMessage(new TextComponentString("Couldn't find session for player or player isn't registered with this session yet. Adding player to session "+sessionName));
			session.predefinedPlayers.put(identifier, new PredefineData());
		}
		return identifier;
	}
	
	//Decided to skip completing this because I couldn't figure out the algorithms,
	//and I didn't want it to slow down session predefine more than it already have
	//Continue if you think you can do it.
	static void finishSession(ICommandSender sender, ICommand command, Session session) throws CommandException
	{
		Random rand = MinestuckRandom.getRandom();
		Set<PlayerIdentifier> unregisteredPlayers = session.getPlayerList();
		unregisteredPlayers.removeAll(session.predefinedPlayers.keySet());
		if(!unregisteredPlayers.isEmpty())
		{
			StringBuilder str = new StringBuilder();
			Iterator<PlayerIdentifier> iter = unregisteredPlayers.iterator();
			str.append(iter.next());
			while(iter.hasNext())
			{
				str.append(", ");
				str.append(iter.next());
			}
			throw new CommandException("Found players in session that isn't registered. Add them or disconnect them from the session to proceed: %s", str.toString());
		}
		
		for(SburbConnection c : session.connections)	//Add data to predefined registry
			if(c.isMain && c.enteredGame)
			{
				PredefineData data = session.predefinedPlayers.get(c.getClientIdentifier());
				Title title = MinestuckPlayerData.getTitle(c.getClientIdentifier());
				AspectCombination landAspects = MinestuckDimensionHandler.getAspects(c.getClientDimension());
				data.title = title;
				data.landTitle = landAspects.aspectTitle;
				data.landTerrain = landAspects.aspectTerrain;
			}
		
		{	//Titles
			int specialClasses = 0;
			int[] classFrequencies = new int[12], aspectFrequencies = new int[12];
			for(PredefineData data : session.predefinedPlayers.values())
				if(data.title != null)
				{
					if(data.title.getHeroClass().ordinal() < 12)
						classFrequencies[data.title.getHeroClass().ordinal()]++;
					else specialClasses++;
					aspectFrequencies[data.title.getHeroAspect().ordinal()]++;
				}
			
			int minAspects = session.predefinedPlayers.size()/12;	//If evenly placed, the minimum amounts of each aspect used
			int additionalAspects = session.predefinedPlayers.size()%12;	//How many additional aspects that need to be used over the minAspects*12.
			int minClasses = (session.predefinedPlayers.size() - specialClasses)/12;
			int additionalClasses = (session.predefinedPlayers.size() - specialClasses)%12;
			int classOffset = 0, aspectOffset = 0;	//How many titles that are already assigned above the minimum.
			for(int i = 0; i < 12; i++)
			{
				if(classFrequencies[i] > minClasses)
					classOffset += classFrequencies[i] - minClasses;
				if(aspectFrequencies[i] > minAspects)
					aspectOffset += aspectFrequencies[i] - minAspects;
			}
			
			if(classOffset > additionalClasses)	//if this is true, then it can't assign at least "minClassses" of each class.
			{
				int levelOffset = (classOffset - additionalClasses + 11)/12;
				minClasses -= levelOffset;
				additionalClasses += levelOffset*12;
				for(int i = 0; i < 12; i++)
					if(classFrequencies[i] > minClasses)
						additionalClasses -= Math.min(levelOffset, classFrequencies[i] - minClasses);
			}
		}
		//generate titles, land aspects etc. here
		//order of generation: title -> land aspect title -> land aspect terrain
		
		session.locked = true;
	}*/
	
	/**
	 * @param c The connection.
	 * @return Damage value for the entry item
	 */
	public static ItemStack getEntryItem(World world, SburbConnection c)
	{
		int color =  ColorHandler.getColorForPlayer(c.getClientIdentifier(), world);
		Item artifact;
		if(c == null)
			artifact = MSItems.CRUXITE_APPLE;
		
		else switch(c.artifactType)
		{
		case 1: artifact = MSItems.CRUXITE_POTION; break;
		default: artifact = MSItems.CRUXITE_APPLE;
		}
		
		return ColorHandler.setColor(new ItemStack(artifact), color);
	}
	
	public static GristType getPrimaryGristType(PlayerIdentifier player)
	{
		
		return GristTypes.SHALE;
	}
	
	public static SburbConnection getConnectionForDimension(ServerWorld world)
	{
		return getConnectionForDimension(world.getServer(), world.getDimension().getType());
	}
	public static SburbConnection getConnectionForDimension(MinecraftServer mcServer, DimensionType dim)
	{
		if(dim == null)
			return null;
		for(SburbConnection c : SkaianetHandler.get(mcServer).connections)
			if(c.getClientDimension() == dim)
				return c;
		return null;
	}
	
	/**
	 * 
	 * @param client The username of the player, encoded.
	 */
	public static int availableTier(MinecraftServer mcServer, PlayerIdentifier client)
	{
		Session s = SessionHandler.get(mcServer).getPlayerSession(client);
		if(s == null)
			return -1;
		if(s.completed)
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
			titleLandType = Generator.generateWeightedTitleLandType(session, title.getHeroAspect(), terrainLandType, connection.getClientIdentifier());
			if(terrainLandType != null && titleLandType == LandTypes.TITLE_NULL)
			{
				Debug.warnf("Failed to find a title land aspect compatible with land aspect \"%s\". Forced to use a poorly compatible land aspect instead.", terrainLandType.getRegistryName());
				titleLandType = Generator.generateWeightedTitleLandType(session, title.getHeroAspect(), null, connection.getClientIdentifier());
			}
		}
		if(terrainLandType == null)
			terrainLandType = Generator.generateWeightedTerrainLandType(session, titleLandType, connection.getClientIdentifier());
		
		return new LandTypePair(terrainLandType, titleLandType);
	}
	
	static void onFirstItemGiven(SburbConnection connection)
	{
		
	}
	
	static void prepareEntry(MinecraftServer mcServer, SburbConnection c)
	{
		PlayerIdentifier identifier = c.getClientIdentifier();
		
		generateTitle(mcServer.getWorld(DimensionType.OVERWORLD), c.getClientIdentifier());
		LandTypePair landTypes = genLandAspects(mcServer, c);		//This is where the Land dimension is actually registered, but it also needs the player's Title to be determined.
		DimensionType dimType = LandTypes.createLandType(mcServer, identifier, landTypes);
		c.setLand(landTypes, dimType);
	}
	
	static void onEntry(MinecraftServer server, SburbConnection c)
	{
		c.setHasEntered();
		
		SessionHandler.get(server).getPlayerSession(c.getClientIdentifier()).checkIfCompleted(SessionHandler.get(server).singleSession);
		
		ServerPlayerEntity player = c.getClientIdentifier().getPlayer(server);
		if(player != null)
		{
			MSCriteriaTriggers.CRUXITE_ARTIFACT.trigger(player);
			c.getLandInfo().sendLandEntryMessage(player);
		}
	}
	
	public static boolean canSelectColor(PlayerIdentifier player, MinecraftServer mcServer)
	{
		for(SburbConnection c : SkaianetHandler.get(mcServer).connections)
			if(c.getClientIdentifier().equals(player))
				return false;
		return true;
	}
	
	public static boolean hasEntered(ServerPlayerEntity player)
	{
		PlayerIdentifier identifier = IdentifierHandler.encode(player);
		SburbConnection c = SkaianetHandler.get(player.server).getMainConnection(identifier, true);
		return c != null && c.hasEntered();
	}

	static void onConnectionCreated(SburbConnection c)
	{
		Random rand = MinestuckRandom.getPlayerSpecificRandom(c.getClientIdentifier(), 0);
		c.artifactType = rand.nextInt(2);
		Debug.infof("Randomized artifact type to be: %d for player %s.", c.artifactType, c.getClientIdentifier().getUsername());
	}
	
	public static boolean shouldEnterNow(ServerPlayerEntity player)
	{
		if(!MinestuckConfig.playerSelectedTitle.get())
			return true;
		
		PlayerIdentifier identifier = IdentifierHandler.encode(player);
		Session s = SessionHandler.get(player.world).getPlayerSession(identifier);
		
		if(s != null && s.predefinedPlayers.containsKey(identifier) && s.predefinedPlayers.get(identifier).getTitle() != null
				|| PlayerSavedData.getData(identifier, player.server).getTitle() != null)
			return true;
		
		titleSelectionMap.put(player, new Vec3d(player.posX, player.posY, player.posZ));
		TitleSelectPacket packet = new TitleSelectPacket();
		MSPacketHandler.sendToPlayer(packet, player);
		return false;
	}
	
	public static void stopEntry(ServerPlayerEntity player)
	{
		titleSelectionMap.remove(player);
	}
	
	public static void titleSelected(ServerPlayerEntity player, Title title)
	{
		if(MinestuckConfig.playerSelectedTitle.get() && titleSelectionMap.containsKey(player))
		{
			PlayerIdentifier identifier = IdentifierHandler.encode(player);
			
			if(title == null)
				generateTitle(player.world, identifier);
			else
			{
				Session s = SessionHandler.get(player.server).getPlayerSession(identifier);
				if(s != null)
				{
					for(SburbConnection c : s.connections)
						if(title.equals(PlayerSavedData.getData(c.getClientIdentifier(), player.server).getTitle()))
						{    //Title is already used
							MSPacketHandler.sendToPlayer(new TitleSelectPacket(title), player);
							return;
						}
					for(PredefineData data : s.predefinedPlayers.values())
						if(title.equals(data.getTitle()))
						{
							MSPacketHandler.sendToPlayer(new TitleSelectPacket(title), player);
							return;
						}
				} else Debug.warnf("%s picked a title without being part of a session.", player.getDisplayName());
				
				PlayerSavedData.getData(identifier, player.server).setTitle(title);
			}
			
			Vec3d pos = titleSelectionMap.remove(player);
			
			player.setPosition(pos.x, pos.y, pos.z);
			
			EntryProcess process = new EntryProcess();
			process.onArtifactActivated(player);
			
		} else Debug.warnf("%s tried to select a title without entering.", player.getName());
	}
}