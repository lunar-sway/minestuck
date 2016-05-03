package com.mraof.minestuck.network.skaianet;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.Vec3i;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase.SpawnListEntry;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.MinestuckConfig;
import com.mraof.minestuck.entity.underling.EntityBasilisk;
import com.mraof.minestuck.entity.underling.EntityGiclops;
import com.mraof.minestuck.entity.underling.EntityImp;
import com.mraof.minestuck.entity.underling.EntityOgre;
import com.mraof.minestuck.entity.underling.EntityUnderling;
import com.mraof.minestuck.item.MinestuckItems;
import static com.mraof.minestuck.network.skaianet.SessionHandler.*;
import com.mraof.minestuck.tracker.MinestuckPlayerTracker;
import com.mraof.minestuck.util.Debug;
import com.mraof.minestuck.util.EnumAspect;
import com.mraof.minestuck.util.EnumClass;
import com.mraof.minestuck.util.GristHelper;
import com.mraof.minestuck.util.GristType;
import com.mraof.minestuck.util.MinestuckPlayerData;
import com.mraof.minestuck.util.Title;
import com.mraof.minestuck.util.UsernameHandler;
import com.mraof.minestuck.util.UsernameHandler.PlayerIdentifier;
import com.mraof.minestuck.world.MinestuckDimensionHandler;
import com.mraof.minestuck.world.lands.LandAspectRegistry;
import com.mraof.minestuck.world.lands.LandAspectRegistry.AspectCombination;
import com.mraof.minestuck.world.lands.terrain.TerrainLandAspect;
import com.mraof.minestuck.world.lands.title.TitleLandAspect;

/**
 * A class for managing sbrub-related stuff from outside this package that is dependent on connections and sessions.
 * For example: Titles, land aspects, underling grist types, entry items etc.
 * @author kirderf1
 */
public class SburbHandler
{
	
	static void generateTitle(PlayerIdentifier player)
	{
		Session session = getPlayerSession(player);
		Title title = null;
		if(session.predefinedPlayers.containsKey(player))
		{
			PredefineData data = session.predefinedPlayers.get(player);
			title = data.title;
		}
		
		if(title == null)
		{
			Random rand = new Random(Minestuck.worldSeed^player.hashCode());
			rand.nextInt();	//Avoid using same data as the artifact generation
			
			ArrayList<Title> usedTitles = new ArrayList<Title>();
			Set<PlayerIdentifier> playersEntered = new HashSet<PlayerIdentifier>();	//Used to avoid duplicates from both connections and predefined data
			playersEntered.add(player);
			for(SburbConnection c : session.connections)
				if(!c.getClientIdentifier().equals(player) && c.enteredGame)
				{
					usedTitles.add(MinestuckPlayerData.getTitle(c.getClientIdentifier()));
					playersEntered.add(c.getClientIdentifier());
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
				for(Title usedTitle : usedTitles)
					if(usedTitle.getHeroClass().ordinal() < 12)
						classFrequency[usedTitle.getHeroClass().ordinal()]++;
				
				EnumClass titleClass = null;
				int titleIndex = rand.nextInt(144 - usedTitles.size());
				for(int classIndex = 0; classIndex < 12; classIndex++)
				{
					int classChance = 12 - classFrequency[classIndex];
					if(titleIndex <= classChance)
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
		
		MinestuckPlayerData.setTitle(player, title);
		MinestuckPlayerTracker.updateTitle(player.getPlayer());
	}
	
	public static void managePredefinedSession(ICommandSender sender, ICommand command, String sessionName, String[] playerNames, boolean finish) throws CommandException
	{
		Session session = sessionsByName.get(sessionName);
		if(session == null)
		{
			if(finish && playerNames.length == 0)
				throw new CommandException("Couldn't find session with that name. Aborting the finalizing process.", sessionName);
			if(singleSession)
				throw new CommandException("Not allowed to create new sessions when global session is active. Use \"%s\" as session name for global session access.", GLOBAL_SESSION_NAME);
			
			if(sender.sendCommandFeedback())
				sender.addChatMessage(new ChatComponentText("Couldn't find session with that name, creating a new session..."));
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
					identifier = UsernameHandler.getForCommand(sender, playerName);
				} catch(CommandException c)
				{
					if(sender.sendCommandFeedback())
						sender.addChatMessage(new ChatComponentText(String.format(c.getMessage(), c.getErrorObjects())));
					continue;
				}
				
				if(!session.containsPlayer(identifier))
				{
					if(sender.sendCommandFeedback())
						sender.addChatMessage(new ChatComponentText("Failed to remove player \""+playerName+"\": Player isn't in session.").setChatStyle(new ChatStyle().setColor(EnumChatFormatting.RED)));
					continue;
				}
				
				if(session.predefinedPlayers.remove(identifier) == null)
				{
					if(sender.sendCommandFeedback())
						sender.addChatMessage(new ChatComponentText("Failed to remove player \""+playerName+"\": Player isn't registered with the session.").setChatStyle(new ChatStyle().setColor(EnumChatFormatting.RED)));
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
							sender.addChatMessage(new ChatComponentText("Removed player \""+playerName+"\", but they are still part of a connection in the session and will therefore be part of the session unless the connection is discarded.").setChatStyle(new ChatStyle().setColor(EnumChatFormatting.YELLOW)));
						skipFinishing = true;
						continue;
					}
				}
			} else
			{
				PlayerIdentifier identifier;
				try
				{
					identifier = UsernameHandler.getForCommand(sender, playerName);
				} catch(CommandException c)
				{
					if(sender.sendCommandFeedback())
						sender.addChatMessage(new ChatComponentText(String.format(c.getMessage(), c.getErrorObjects())));
					continue;
				}
				
				if(session.predefinedPlayers.containsKey(identifier))
				{
					if(sender.sendCommandFeedback())
						sender.addChatMessage(new ChatComponentText("Failed to add player \""+playerName+"\": Player is already registered with session.").setChatStyle(new ChatStyle().setColor(EnumChatFormatting.RED)));
					continue;
				}
				
				Session playerSession = getPlayerSession(identifier);
				
				if(playerSession != null)
				{
					if(merge(session, playerSession, null) != null)
					{
						if(sender.sendCommandFeedback())
							sender.addChatMessage(new ChatComponentText("Failed to add player \""+playerName+"\": Can't merge with the session that the player is already in.").setChatStyle(new ChatStyle().setColor(EnumChatFormatting.RED)));
						continue;
					}
				} else if(MinestuckConfig.forceMaxSize && session.getPlayerList().size() + 1 > maxSize)
				{
					if(sender.sendCommandFeedback())
						sender.addChatMessage(new ChatComponentText("Failed to add player \""+playerName+"\": The session can't accept more players with the current configurations.").setChatStyle(new ChatStyle().setColor(EnumChatFormatting.RED)));
					continue;
				}
				
				session.predefinedPlayers.put(identifier, new PredefineData());
				handled++;
			}
		}
		
		if(playerNames.length > 0)
			CommandBase.notifyOperators(sender, command, "commands.sburbSession.addSuccess", handled, playerNames.length);
		
		/*if(finish)
			if(!skipFinishing && handled == playerNames.length)
			{
				SburbHandler.finishSession(sender, command, session);
				
			} else throw new CommandException("Skipping to finalize the session due to one or more issues while adding players.");*/
	}
	
	public static void sessionName(ICommandSender sender, ICommand command, String playerName, String sessionName) throws CommandException
	{
		PlayerIdentifier identifier = UsernameHandler.getForCommand(sender, playerName);
		
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
			CommandBase.notifyOperators(sender, command, "commands.sburbSession.rename", prevName, sessionName, playerName);
		else CommandBase.notifyOperators(sender, command, "commands.sburbSession.name", sessionName, playerName);
	}
	
	public static void predefineTitle(ICommandSender sender, ICommand command, String playerName, String sessionName, Title title) throws CommandException
	{
		PlayerIdentifier identifier = predefineCheck(sender, playerName, sessionName);
		
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
		CommandBase.notifyOperators(sender, command, "commands.sburbSession.titleSuccess", playerName, title.getTitleName());
	}
	
	public static void predefineTerrainLandAspect(ICommandSender sender, ICommand command, String playerName, String sessionName, TerrainLandAspect aspect) throws CommandException
	{
		PlayerIdentifier identifier = predefineCheck(sender, playerName, sessionName);
		
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
		CommandBase.notifyOperators(sender, command, "commands.sburbSession.landTerrainSuccess", playerName, aspect.getPrimaryName());
	}
	
	public static void predefineTitleLandAspect(ICommandSender sender, ICommand command, String playerName, String sessionName, TitleLandAspect aspect) throws CommandException
	{
		PlayerIdentifier identifier = predefineCheck(sender, playerName, sessionName);
		
		Session session = sessionsByName.get(sessionName);
		SburbConnection clientConnection = SkaianetHandler.getClientConnection(identifier);
		PredefineData data = session.predefinedPlayers.get(identifier);
		
		if(clientConnection != null && clientConnection.enteredGame())
			throw new CommandException("You can't change your land aspects after having entered the medium.");
		if(sender.sendCommandFeedback())
			if(data.title == null)
				sender.addChatMessage(new ChatComponentText("Beware that the title generated might not be suited for this land aspect.").setChatStyle(new ChatStyle().setColor(EnumChatFormatting.YELLOW)));
			else if(!LandAspectRegistry.containsTitleLandAspect(data.title.getHeroAspect(), aspect))
				sender.addChatMessage(new ChatComponentText("Beware that the title predefined isn't suited for this land aspect.").setChatStyle(new ChatStyle().setColor(EnumChatFormatting.YELLOW)));
		
		if(data.landTerrain != null && !aspect.isAspectCompatible(data.landTerrain))
		{
			data.landTerrain = null;
			if(sender.sendCommandFeedback())
				sender.addChatMessage(new ChatComponentText("The terrain aspect previously chosen isn't compatible with this land aspect, and has therefore been removed.").setChatStyle(new ChatStyle().setColor(EnumChatFormatting.YELLOW)));
		}
		
		data.landTitle = aspect;
		CommandBase.notifyOperators(sender, command, "commands.sburbSession.landTitleSuccess", playerName, aspect.getPrimaryName());
	}
	
	private static PlayerIdentifier predefineCheck(ICommandSender sender, String playerName, String sessionName) throws CommandException
	{
		PlayerIdentifier identifier = UsernameHandler.getForCommand(sender, playerName);
		
		Session session = sessionsByName.get(sessionName), playerSession = getPlayerSession(identifier);
		if(session == null)
			throw new CommandException("Couldn't find session with the name %s", sessionName);
		if(playerSession != null && session != playerSession)
			throw new CommandException("The player is already in another session!");
		if(playerSession == null || !session.predefinedPlayers.containsKey(identifier))
		{
			if(sender.sendCommandFeedback())
				sender.addChatMessage(new ChatComponentText("Couldn't find session for player or player isn't registered with this session yet. Adding player to session "+sessionName));
			session.predefinedPlayers.put(identifier, new PredefineData());
		}
		return identifier;
	}
	
	//Decided to skip completing this because I couldn't figure out the algorithms,
	//and I didn't want it to slow down session predefine more than it already have
	//Continue if you think you can do it.
	static void finishSession(ICommandSender sender, ICommand command, Session session) throws CommandException
	{
		Random rand = new Random();	//What seed?
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
		if(s == null) {
			return -1;
		}
		if(s.completed)
			return Integer.MAX_VALUE;
		SburbConnection c = SkaianetHandler.getClientConnection(client);
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
		
		int impWeight = 0, ogreWeight = 0, basiliskWeight = 0, giclopsWeight = 0;
		
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
		if(giclopsWeight > 0)
			list.add(new SpawnListEntry(EntityGiclops.class, giclopsWeight, 1, Math.max(1, giclopsWeight/2)));
		
		difficultyList[difficulty] = list;
		
		return list;
	}
	
	static void onFirstItemGiven(SburbConnection connection) {
		
	}
	
	static void onGameEntered(SburbConnection connection)
	{
		generateTitle(connection.getClientIdentifier());
		getPlayerSession(connection.getClientIdentifier()).checkIfCompleted();
		genLandAspects(connection);
		
		
	}
	
	public static boolean canSelectColor(EntityPlayerMP player)
	{
		PlayerIdentifier identifier = UsernameHandler.encode(player);
		for(SburbConnection c : SkaianetHandler.connections)
			if(c.getClientIdentifier().equals(identifier))
				return false;
		return true;
	}

	static void onConnectionCreated(SburbConnection c)
	{
		Random rand = new Random(Minestuck.worldSeed^c.getClientIdentifier().hashCode());
		c.artifactType = rand.nextInt(2);
		Debug.infof("Randomized artifact type to be: %d for player %s.", c.artifactType, c.getClientIdentifier().getUsername());
	}
}