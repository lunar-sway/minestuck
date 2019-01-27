package com.mraof.minestuck.network.skaianet;

import static com.mraof.minestuck.network.skaianet.SessionHandler.GLOBAL_SESSION_NAME;
import static com.mraof.minestuck.network.skaianet.SessionHandler.getPlayerSession;
import static com.mraof.minestuck.network.skaianet.SessionHandler.maxSize;
import static com.mraof.minestuck.network.skaianet.SessionHandler.merge;
import static com.mraof.minestuck.network.skaianet.SessionHandler.sessions;
import static com.mraof.minestuck.network.skaianet.SessionHandler.sessionsByName;
import static com.mraof.minestuck.network.skaianet.SessionHandler.singleSession;
import static com.mraof.minestuck.network.skaianet.SessionHandler.split;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.MinestuckConfig;
import com.mraof.minestuck.entity.underling.EntityBasilisk;
import com.mraof.minestuck.entity.underling.EntityGiclops;
import com.mraof.minestuck.entity.underling.EntityImp;
import com.mraof.minestuck.entity.underling.EntityLich;
import com.mraof.minestuck.entity.underling.EntityOgre;
import com.mraof.minestuck.entity.underling.EntityUnderling;
import com.mraof.minestuck.item.ItemCruxiteArtifact;
import com.mraof.minestuck.item.MinestuckItems;
import com.mraof.minestuck.network.MinestuckChannelHandler;
import com.mraof.minestuck.network.MinestuckPacket;
import com.mraof.minestuck.network.PlayerDataPacket;
import com.mraof.minestuck.tracker.MinestuckPlayerTracker;
import com.mraof.minestuck.util.Debug;
import com.mraof.minestuck.util.EnumAspect;
import com.mraof.minestuck.util.EnumClass;
import com.mraof.minestuck.alchemy.GristHelper;
import com.mraof.minestuck.alchemy.GristType;
import com.mraof.minestuck.util.IdentifierHandler;
import com.mraof.minestuck.util.IdentifierHandler.PlayerIdentifier;
import com.mraof.minestuck.util.MinestuckPlayerData;
import com.mraof.minestuck.util.MinestuckRandom;
import com.mraof.minestuck.util.Title;
import com.mraof.minestuck.world.MinestuckDimensionHandler;
import com.mraof.minestuck.world.lands.LandAspectRegistry;
import com.mraof.minestuck.world.lands.LandAspectRegistry.AspectCombination;
import com.mraof.minestuck.world.lands.terrain.TerrainLandAspect;
import com.mraof.minestuck.world.lands.title.TitleLandAspect;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome.SpawnListEntry;

/**
 * A class for managing sbrub-related stuff from outside this package that is dependent on connections and sessions.
 * For example: Titles, land aspects, underling grist types, entry items etc.
 * @author kirderf1
 */
public class SburbHandler
{
	static Map<EntityPlayer, Vec3d> titleSelectionMap = new HashMap<EntityPlayer, Vec3d>();
	
	private static Title produceTitle(PlayerIdentifier player)
	{
		if(MinestuckPlayerData.getTitle(player) != null)
			if(MinestuckConfig.playerSelectedTitle)
				return null;	//Should be generated using the title-selection gui
			else
			{
				Debug.warnf("Trying to generate a title for %s when a title is already assigned!", player.getUsername());
				return null;
			}
		
		Session session = getPlayerSession(player);
		if(session == null)
			if(MinestuckConfig.playerSelectedTitle)
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
			title = data.title;
		}
		
		if(title == null)
		{
			Random rand = MinestuckRandom.getPlayerSpecificRandom(player);
			rand.nextInt();	//Avoid using same data as the artifact generation
			
			ArrayList<Title> usedTitles = new ArrayList<Title>();
			Set<PlayerIdentifier> playersEntered = new HashSet<PlayerIdentifier>();	//Used to avoid duplicates from both connections and predefined data
			playersEntered.add(player);
			for(SburbConnection c : session.connections)
				if(!c.getClientIdentifier().equals(player) && c.enteredGame)
				{
					Title playerTitle = MinestuckPlayerData.getTitle(c.getClientIdentifier());
					if(playerTitle != null)
					{
						usedTitles.add(playerTitle);
						playersEntered.add(c.getClientIdentifier());
					} else Debug.warnf("Player %s has entered, but lacks a title. This is not good!", c.getClientIdentifier().getUsername());
				}
			
			for(Map.Entry<PlayerIdentifier, PredefineData> entry : session.predefinedPlayers.entrySet())
				if(!playersEntered.contains(entry.getKey()) && entry.getValue().title != null)
					usedTitles.add(entry.getValue().title);
			
			if(usedTitles.size() < 12)	//Focus on getting an unused aspect and an unused class
			{
				EnumSet<EnumClass> usedClasses = EnumSet.noneOf(EnumClass.class);
				EnumSet<EnumAspect> usedAspects = EnumSet.noneOf(EnumAspect.class);
				for(Title usedTitle : usedTitles)
				{
					usedClasses.add(usedTitle.getHeroClass());
					usedAspects.add(usedTitle.getHeroAspect());
				}
				
				title = new Title(EnumClass.getRandomClass(usedClasses, rand), EnumAspect.getRandomAspect(usedAspects, rand));
			}
			else if(usedTitles.size() < 144)	//Focus only on getting an unused title
			{
				
				int[] classFrequency = new int[12];
				int specialClasses = 0;
				for(Title usedTitle : usedTitles)
				{
					if(usedTitle.getHeroClass().ordinal() < 12)
						classFrequency[usedTitle.getHeroClass().ordinal()]++;
					else specialClasses++;
				}
				
				EnumClass titleClass = null;
				int titleIndex = rand.nextInt(144 - (usedTitles.size() - specialClasses));
				for(int classIndex = 0; classIndex < 12; classIndex++)
				{
					int classChance = 12 - classFrequency[classIndex];
					if(titleIndex < classChance)
					{
						titleClass = EnumClass.getClassFromInt(classIndex);
						break;
					}
					titleIndex -= classChance;
				}
				if(titleClass == null)
					throw new IllegalStateException("Finished for loop without generating a title class. This should not happen and is likely a bug.");
				
				EnumSet<EnumAspect> usedAspects = EnumSet.noneOf(EnumAspect.class);
				for(Title usedTitle : usedTitles)
					if(usedTitle.getHeroClass() == titleClass)
						usedAspects.add(usedTitle.getHeroAspect());
				EnumAspect titleAspect = null;
				for(EnumAspect aspect : EnumAspect.values())
					if(!usedAspects.contains(aspect))
					{
						if(titleIndex == 0)
						{
							titleAspect = aspect;
							break;
						}
						titleIndex--;
					}
				if(titleAspect == null)
					throw new IllegalStateException("Finished for loop without generating a title aspect. This should not happen and is likely a bug.");
				
				title = new Title(titleClass, titleAspect);
			}
		}
		return title;
	}
	
	private static void generateTitle(PlayerIdentifier player)
	{
		Title title = produceTitle(player);
		if(title==null)
			return;
		MinestuckPlayerData.setTitle(player, title);
		MinestuckPlayerTracker.updateTitle(player.getPlayer());
	}
	
	public static void managePredefinedSession(MinecraftServer server, ICommandSender sender, ICommand command, String sessionName, String[] playerNames, boolean finish) throws CommandException
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
	}
	
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
		if(playerSession != null && session != playerSession)
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
	}
	
	/**
	 * @param player The username of the player, encoded.
	 * @return Damage value for the entry item
	 */
	public static ItemStack getEntryItem(PlayerIdentifier player)
	{
		SburbConnection c = SkaianetHandler.getClientConnection(player); 
		int colorIndex = MinestuckPlayerData.getData(player).color;
		Item artifact;
		if(c == null)
			artifact = MinestuckItems.cruxiteApple;
		
		else switch(c.artifactType)
		{
		case 1: artifact = MinestuckItems.cruxitePotion; break;
		default: artifact = MinestuckItems.cruxiteApple;
		}
		
		return new ItemStack(artifact, 1, colorIndex + 1);
	}
	
	public static GristType getPrimaryGristType(PlayerIdentifier player)
	{
		
		return GristType.Shale;
	}
	
	public static int getColorForDimension(int dim)
	{
		SburbConnection c = getConnectionForDimension(dim);
		return c == null ? -1 : MinestuckPlayerData.getData(c.getClientIdentifier()).color;
	}
	
	public static SburbConnection getConnectionForDimension(int dim)
	{
		for(SburbConnection c : SkaianetHandler.connections)
			if(c.enteredGame && c.clientHomeLand == dim)
				return c;
		return null;
	}
	
	/**
	 * 
	 * @param client The username of the player, encoded.
	 */
	public static int availableTier(PlayerIdentifier client)
	{
		Session s = getPlayerSession(client);
		if(s == null)
			return -1;
		if(s.completed)
			return Integer.MAX_VALUE;
		SburbConnection c = SkaianetHandler.getClientConnection(client);
		if(c == null)
			return -1;
		int count = -1;
		for(SburbConnection conn : s.connections)
			if(conn.enteredGame)
				count++;
		if(!c.enteredGame)
			count++;
		return count;
	}
	
	private static void genLandAspects(SburbConnection connection)
	{
		LandAspectRegistry aspectGen = new LandAspectRegistry((Minestuck.worldSeed^connection.clientHomeLand)^(connection.clientHomeLand << 8));
		Session session = getPlayerSession(connection.getClientIdentifier());
		Title title = MinestuckPlayerData.getTitle(connection.getClientIdentifier());
		TitleLandAspect titleAspect = null;
		TerrainLandAspect terrainAspect = null;
		
		if(session.predefinedPlayers.containsKey(connection.getClientIdentifier()))
		{
			PredefineData data = session.predefinedPlayers.get(connection.getClientIdentifier());
			if(data.landTitle != null)
				titleAspect = data.landTitle;
			if(data.landTerrain != null)
				terrainAspect = data.landTerrain;
		}
		
		boolean frogs = false;
		ArrayList<TerrainLandAspect> usedTerrainAspects = new ArrayList<TerrainLandAspect>();
		ArrayList<TitleLandAspect> usedTitleAspects = new ArrayList<TitleLandAspect>();
		for(SburbConnection c : session.connections)
			if(c.enteredGame && c != connection)
			{
				LandAspectRegistry.AspectCombination aspects = MinestuckDimensionHandler.getAspects(c.clientHomeLand);
				if(aspects.aspectTitle == LandAspectRegistry.frogAspect)
					frogs = true;
				usedTitleAspects.add(aspects.aspectTitle);
				usedTerrainAspects.add(aspects.aspectTerrain);
			}
		for(PredefineData data : session.predefinedPlayers.values())
		{
			if(data.landTerrain != null)
				usedTerrainAspects.add(data.landTerrain);
			if(data.landTitle != null)
			{
				usedTitleAspects.add(data.landTitle);
				if(data.landTitle == LandAspectRegistry.frogAspect)
					frogs = true;
			}
		}
		
		if(titleAspect == null)
			titleAspect = aspectGen.getTitleAspect(terrainAspect, title.getHeroAspect(), usedTitleAspects);
		if(terrainAspect == null)
			terrainAspect = aspectGen.getTerrainAspect(titleAspect, usedTerrainAspects);
		MinestuckDimensionHandler.registerLandDimension(connection.clientHomeLand, new AspectCombination(terrainAspect, titleAspect));
	}
	
	public static GristType getUnderlingType(EntityUnderling entity)
	{
		return GristHelper.getPrimaryGrist();
	}
	
	private static List<SpawnListEntry>[] difficultyList = new ArrayList[31];
	
	public static List<SpawnListEntry> getUnderlingList(BlockPos pos, World world)
	{
		
		BlockPos spawn = world.getSpawnPoint();
		
		int difficulty = (int) Math.round(Math.sqrt(new Vec3i(pos.getX() >> 4, 0, pos.getZ() >> 4).distanceSq(new Vec3i(spawn.getX() >> 4, 0, spawn.getZ() >> 4))));
		
		difficulty = Math.min(30, difficulty/3);
		
		if(difficultyList[difficulty] != null)
			return difficultyList[difficulty];
		
		ArrayList<SpawnListEntry> list = new ArrayList<SpawnListEntry>();
		
		int impWeight, ogreWeight = 0, basiliskWeight = 0, lichWeight = 0, giclopsWeight = 0;
		
		if(difficulty < 8)
			impWeight = difficulty + 1;
		else
		{
			impWeight = 8 - (difficulty - 8)/3;
			if(difficulty < 20)
				ogreWeight = (difficulty - 5)/3;
			else ogreWeight = 5 - (difficulty - 20)/3;
			
			if(difficulty >= 16)
			{
				if(difficulty < 26)
					basiliskWeight = (difficulty - 14)/2;
				else basiliskWeight = 6;
				if(difficulty < 28)
					lichWeight = (difficulty - 12)/3;
				else lichWeight = 6;
				if(difficulty >= 20)
					if(difficulty < 30)
						giclopsWeight = (difficulty - 17)/3;
					else giclopsWeight = 5;
			}
		}
		
		if(impWeight > 0)
			list.add(new SpawnListEntry(EntityImp.class, impWeight, Math.max(1, (int)(impWeight/2.5)), Math.max(3, impWeight)));
		if(ogreWeight > 0)
			list.add(new SpawnListEntry(EntityOgre.class, ogreWeight, ogreWeight >= 5 ? 2 : 1, Math.max(1, ogreWeight/2)));
		if(basiliskWeight > 0)
			list.add(new SpawnListEntry(EntityBasilisk.class, basiliskWeight, 1, Math.max(1, basiliskWeight/2)));
		if(lichWeight > 0)
			list.add(new SpawnListEntry(EntityLich.class, lichWeight, 1, Math.max(1, lichWeight/2)));
		if(giclopsWeight > 0 && !MinestuckConfig.disableGiclops)
			list.add(new SpawnListEntry(EntityGiclops.class, giclopsWeight, 1, Math.max(1, giclopsWeight/2)));
		
		difficultyList[difficulty] = list;
		
		return list;
	}
	
	static void onFirstItemGiven(SburbConnection connection)
	{
		
	}
	
	static void onLandCreated(SburbConnection c)
	{
//		Session session = getPlayerSession(c.clientIdentifier);
//		PlayerIdentifier identifier = c.clientIdentifier;
//		Title title = produceTitle(identifier);
//		
//		Title playerTitle = MinestuckPlayerData.getTitle(identifier);
//		if(playerTitle == null)
//		{
//			PredefineData data = session.predefinedPlayers.get(identifier);
//			data.title = title;
//		}
		
		generateTitle(c.getClientIdentifier());
		genLandAspects(c);		//This is where the Land dimension is actually registered, but it also needs the player's Title to be determined.
	}
	
	static void onGameEntered(SburbConnection c)
	{
		getPlayerSession(c.getClientIdentifier()).checkIfCompleted();
	}
	
	public static boolean canSelectColor(EntityPlayerMP player)
	{
		PlayerIdentifier identifier = IdentifierHandler.encode(player);
		for(SburbConnection c : SkaianetHandler.connections)
			if(c.getClientIdentifier().equals(identifier))
				return false;
		return true;
	}
	
	public static boolean hasEntered(EntityPlayerMP player)
	{
		PlayerIdentifier identifier = IdentifierHandler.encode(player);
		SburbConnection c = SkaianetHandler.getMainConnection(identifier, true);
		return c != null && c.enteredGame();
	}

	static void onConnectionCreated(SburbConnection c)
	{
		Random rand = MinestuckRandom.getPlayerSpecificRandom(c.getClientIdentifier());
		c.artifactType = rand.nextInt(2);
		Debug.infof("Randomized artifact type to be: %d for player %s.", c.artifactType, c.getClientIdentifier().getUsername());
	}
	
	public static boolean shouldEnterNow(EntityPlayer player)
	{
		if(!MinestuckConfig.playerSelectedTitle)
			return true;
		
		PlayerIdentifier identifier = IdentifierHandler.encode(player);
		Session s = getPlayerSession(identifier);
		
		if(s != null && s.predefinedPlayers.containsKey(identifier) && s.predefinedPlayers.get(identifier).title != null
				|| MinestuckPlayerData.getTitle(identifier) != null)
			return true;
		
		titleSelectionMap.put(player, new Vec3d(player.posX, player.posY, player.posZ));
		MinestuckChannelHandler.sendToPlayer(MinestuckPacket.makePacket(MinestuckPacket.Type.PLAYER_DATA, PlayerDataPacket.TITLE_SELECT), player);
		return false;
	}
	
	public static void stopEntry(EntityPlayer player)
	{
		titleSelectionMap.remove(player);
	}
	
	public static void titleSelected(EntityPlayer player, Title title)
	{
		if(MinestuckConfig.playerSelectedTitle && titleSelectionMap.containsKey(player))
		{
			PlayerIdentifier identifier = IdentifierHandler.encode(player);
			Session s = getPlayerSession(identifier);
			if(s == null)
				if(singleSession)
					s = sessions.get(0);
				else s = new Session();
			
			if(title == null)
				generateTitle(identifier);
			else
			{
				for(SburbConnection c : s.connections)
					if(c.enteredGame() && title.equals(MinestuckPlayerData.getTitle(c.getClientIdentifier())))
					{	//Title is already used
						MinestuckChannelHandler.sendToPlayer(MinestuckPacket.makePacket(MinestuckPacket.Type.PLAYER_DATA, PlayerDataPacket.TITLE_SELECT, title.getHeroClass(), title.getHeroAspect()), player);
						return;
					}
				for(PredefineData data : s.predefinedPlayers.values())
					if(title.equals(data.title))
					{
						MinestuckChannelHandler.sendToPlayer(MinestuckPacket.makePacket(MinestuckPacket.Type.PLAYER_DATA, PlayerDataPacket.TITLE_SELECT, title.getHeroClass(), title.getHeroAspect()), player);
						return;
					}
				
				MinestuckPlayerData.setTitle(identifier, title);
				MinestuckPlayerTracker.updateTitle(player);
			}
			
			Vec3d pos = titleSelectionMap.remove(player);
			
			player.setPosition(pos.x, pos.y, pos.z);
			((ItemCruxiteArtifact) MinestuckItems.cruxiteApple).onArtifactActivated(player);
			
		} else Debug.warnf("%s tried to select a title without entering.", player.getName());
	}
}