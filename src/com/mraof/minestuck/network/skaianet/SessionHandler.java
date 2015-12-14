package com.mraof.minestuck.network.skaianet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraftforge.common.DimensionManager;

import com.mraof.minestuck.util.MinestuckPlayerData;
import com.mraof.minestuck.util.Title;
import com.mraof.minestuck.util.UsernameHandler;
import com.mraof.minestuck.world.MinestuckDimensionHandler;
import com.mraof.minestuck.world.lands.LandAspectRegistry;
import com.mraof.minestuck.world.lands.gen.ChunkProviderLands;
import com.mraof.minestuck.world.lands.terrain.TerrainLandAspect;
import com.mraof.minestuck.world.lands.title.TitleLandAspect;
import com.mraof.minestuck.MinestuckConfig;

/**
 * Handles session related stuff like title generation, consort choosing, and other session management stuff.
 * @author kirderf1
 */
public class SessionHandler {
	
	public static final String GLOBAL_SESSION_NAME = "global";
	
	/**
	 * The max numbers of players per session.
	 */
	public static int maxSize;
	
	/**
	 * If the current Minecraft world will act as if Minestuck.globalSession is true or not.
	 * Will be for example false even if Minestuck.globalSession is true if it can't merge all
	 * sessions into a single session.
	 */
	public static boolean singleSession;
	
	/**
	 * An array list of the current worlds sessions.
	 */
	static List<Session> sessions = new ArrayList<Session>();
	static Map<String, Session> sessionsByName = new HashMap<String, Session>();
	
	/**
	 * Called when the server loads a new world, after
	 * Minestuck has loaded the sessions from file.
	 */
	public static void serverStarted() {
		singleSession = MinestuckConfig.globalSession;
		if(!MinestuckConfig.globalSession) {
			split();
		} else
		{
			mergeAll();
			if(sessions.size() == 0)
			{
				Session mainSession = new Session();
				mainSession.name = GLOBAL_SESSION_NAME;
				sessions.add(mainSession);
				sessionsByName.put(mainSession.name, mainSession);
			}
		}
	}
	
	/**
	 * Merges all available sessions into one if it can.
	 * Used in the conversion of a non-global session world
	 * to a global session world.
	 */
	static void mergeAll() {
		if(!canMergeAll() || sessions.size() == 0)
		{
			singleSession = sessions.size() == 0;
			return;
		}
		
		Session session = sessions.get(0);
		for(int i = 1; i < sessions.size(); i++)
		{
			Session s = sessions.remove(i);
			session.connections.addAll(s.connections);
			if(s.skaiaId != 0) session.skaiaId = s.skaiaId;
			if(s.prospitId != 0) session.prospitId = s.prospitId;
			if(s.derseId != 0) session.derseId = s.derseId;
		}
		session.name = GLOBAL_SESSION_NAME;
		sessionsByName.clear();
		sessionsByName.put(session.name, session);
		
		session.completed = false;
	}
	
	/**
	 * Checks if it can merge all sessions in the current world into one.
	 * @return False if all registered players is more than maxSize, or if there exists more
	 * than one skaia, prospit, or derse dimension.
	 */
	static boolean canMergeAll()
	{
		int players = 0;
		boolean skaiaUsed = false, prospitUsed = false, derseUsed = false, customSession = false;
		for(Session s : sessions)
		{
			if(s.skaiaId != 0)
				if(skaiaUsed) return false;
				else skaiaUsed = true;
			if(s.prospitId != 0)
				if(prospitUsed) return false;
				else prospitUsed = true;
			if(s.derseId != 0)
				if(derseUsed) return false;
				else derseUsed = true;
			if(s.isCustom())
				return false;
			players += s.getPlayerList().size();
		}
		if(players > maxSize)
			return false;
		else return true;
	}
	
	/**
	 * Looks for the session that the player is a part of.
	 * @param player A string of the player's username.
	 * @return A session that contains at least one connection, that the player is a part of.
	 */
	static Session getPlayerSession(String player)
	{
		for(Session s : sessions)
			if(s.containsPlayer(player))
				return s;
		return null;
	}
	
	static String merge(Session cs, Session ss, SburbConnection sb)
	{
		String s = canMerge(cs, ss);
		if(s == null)
		{
			sessions.remove(ss);
			if(sb != null)
				cs.connections.add(sb);
			cs.connections.addAll(ss.connections);
			if(cs.skaiaId == 0) cs.skaiaId = ss.skaiaId;
			if(cs.prospitId == 0) cs.prospitId = ss.prospitId;
			if(cs.derseId == 0) cs.derseId = ss.derseId;
			
			if(ss.isCustom())
			{
				sessionsByName.remove(ss.name);
				cs.name = ss.name;
				sessionsByName.put(cs.name, cs);
			}
			
		}
		return s;
	}
	
	static String canMerge(Session s0, Session s1)
	{
		if(s0.isCustom() && s1.isCustom() || s0.locked || s1.locked)
			return "computer.messageConnectFail";
		if(MinestuckConfig.forceMaxSize && s0.getPlayerList().size()+s1.getPlayerList().size()>maxSize)
			return "session.bothSessionsFull";
		return null;
	}
	
	/**
	 * Splits up the main session into small sessions.
	 * Used for the conversion of a global session world to
	 * a non-global session.
	 */
	static void split()
	{
		if(MinestuckConfig.globalSession || sessions.size() != 1)
			return;
		
		Session s = sessions.get(0);
		s.name = null;
		split(s);
	}
	
	static void split(Session session)
	{
		if(session.locked)
			return;
		
		sessions.remove(session);
		if(session.isCustom())
			sessionsByName.remove(session.name);
		boolean first = true;
		while(!session.connections.isEmpty()){
			Session s = new Session();
			if(!first)
			{
				s.connections.add(session.connections.remove(0));
				
			} else
			{
				if(session.isCustom())
				{
					s.name = session.name;
					s.predefinedPlayers.putAll(session.predefinedPlayers);
					sessionsByName.put(s.name, s);
				}
				s.skaiaId = session.skaiaId;
				s.prospitId = session.prospitId;
				s.derseId = session.derseId;
			}
			boolean found;
			do {
				found = false;
				Iterator<SburbConnection> iter = session.connections.iterator();
				while(iter.hasNext()){
					SburbConnection c = iter.next();
					if(s.containsPlayer(c.getClientName()) || s.containsPlayer(c.getServerName()) || first && !c.canSplit){
						found = true;
						iter.remove();
						s.connections.add(c);
					}
				}
			} while(found);
			s.checkIfCompleted();
			if(s.connections.size() > 0 || s.isCustom())
				sessions.add(s);
			first = false;
		}
	}
	
	/**
	 * Will check if two players can connect based on their main connections and sessions.
	 * Does NOT include session size checking.
	 * @return True if client connection is not null and client and server session is the same or 
	 * client connection is null and server connection is null.
	 */
	static boolean canConnect(String client, String server) {
		Session sClient = getPlayerSession(client), sServer = getPlayerSession(server);
		SburbConnection cClient = SkaianetHandler.getMainConnection(client, true);
		SburbConnection cServer = SkaianetHandler.getMainConnection(server, false);
		return cClient != null && sClient == sServer && (MinestuckConfig.allowSecondaryConnections || cClient == cServer)
				|| cClient == null && cServer == null && !(sClient != null && sClient.locked) && !(sServer != null && sServer.locked);
	}
	
	/**
	 * @return Null if successful or an unlocalized error message describing reason.
	 */
	static String onConnectionCreated(SburbConnection connection) {
		if(!canConnect(connection.getClientName(), connection.getServerName()))
			return "computer.messageConnectFailed";
		if(singleSession) {
			if(sessions.size() == 0)
				return "computer.messageConnectFailed";
			int i = (sessions.get(0).containsPlayer(connection.getClientName())?0:1)+(sessions.get(0).containsPlayer(connection.getServerName())?0:1);
			if(MinestuckConfig.forceMaxSize && sessions.get(0).getPlayerList().size()+i > maxSize)
				return "computer.singleSessionFull";
			else {
				sessions.get(0).connections.add(connection);
				return null;
			}
		} else {
			Session sClient = getPlayerSession(connection.getClientName()), sServer = getPlayerSession(connection.getServerName());
			if(sClient == null && sServer == null)
			{
				Session s = new Session();
				sessions.add(s);
				s.connections.add(connection);
				return null;
			} else if(sClient == null || sServer == null) {
				if((sClient == null?sServer:sClient).locked || MinestuckConfig.forceMaxSize && (sClient == null?sServer:sClient).getPlayerList().size()+1 > maxSize)
					return "computer."+(sClient == null?"server":"client")+"SessionFull";
				(sClient == null?sServer:sClient).connections.add(connection);
				return null;
			} else {
				if(sClient == sServer) {
					sClient.connections.add(connection);
					return null;
				}
				else return merge(sClient, sServer, connection);
			}
		}
	}
	
	/**
	 * @param normal If the connection was closed by normal means.
	 * (includes everything but getting crushed by a meteor and other reasons for removal of a main connection)
	 */
	static void onConnectionClosed(SburbConnection connection, boolean normal) {
		Session s = getPlayerSession(connection.getClientName());
		
		if(!connection.isMain)
		{
			s.connections.remove(connection);
			if(!singleSession)
				if(s.connections.size() == 0 && !s.isCustom())
					sessions.remove(s);
				else split(s);
		} else if(!normal) {
			s.connections.remove(connection);
			if(!SkaianetHandler.getAssociatedPartner(connection.getClientName(), false).isEmpty() && !connection.getServerName().equals(".null")) {
				SburbConnection c = SkaianetHandler.getMainConnection(connection.getClientName(), false);
				if(c.isActive)
					SkaianetHandler.closeConnection(c.getClientName(), c.getServerName(), true);
				switch(MinestuckConfig.escapeFailureMode) {
				case 0:
					c.serverName = connection.getServerName();
					break;
				case 1:
					c.serverName = ".null";
					break;
				}
			}
			if(s.connections.size() == 0 && !s.isCustom())
				sessions.remove(s);
		}
	}
	
	public static void managePredefinedSession(ICommandSender sender, ICommand command, String sessionName, String[] playerNames, boolean finish) throws CommandException
	{
		Session session = sessionsByName.get(sessionName);
		if(session == null)
		{
			if(finish && playerNames.length == 0)
				throw new CommandException("Couldn't find session with that name. Aborting the finalizing process.", sessionName);
			sender.addChatMessage(new ChatComponentText("Couldn't find session with that name, creating a new session..."));
			session = new Session();
			session.name = sessionName;
			sessions.add(session);
			sessionsByName.put(session.name, session);
		}
		
		if(session.locked)
			throw new CommandException("That session should already be fully predefined.");
		
		int handled = 0;
		boolean skipFinishing = false;
		for(String playerName : playerNames)
		{
			if(playerName.startsWith("!"))
			{
				playerName = playerName.substring(1);
				if(!session.containsPlayer(playerName))
				{
					sender.addChatMessage(new ChatComponentText("Failed to remove player \""+playerName+"\": Player isn't in session.").setChatStyle(new ChatStyle().setColor(EnumChatFormatting.RED)));
					continue;
				}
				
				if(session.predefinedPlayers.remove(playerName) != null)
				{
					sender.addChatMessage(new ChatComponentText("Failed to remove player \""+playerName+"\": Player isn't registered with the session.").setChatStyle(new ChatStyle().setColor(EnumChatFormatting.RED)));
					continue;
				}
				
				handled++;
				
				if(session.containsPlayer(playerName))
				{
					sender.addChatMessage(new ChatComponentText("Removed player \""+playerName+"\", but they are still part of a connection in the session and will therefore be part of the session unless the connection is discarded.").setChatStyle(new ChatStyle().setColor(EnumChatFormatting.YELLOW)));
					skipFinishing = true;
					continue;
				}
			} else
			{
				if(session.predefinedPlayers.containsKey(playerName))
				{
					sender.addChatMessage(new ChatComponentText("Failed to add player \""+playerName+"\": Player is already registered with session.").setChatStyle(new ChatStyle().setColor(EnumChatFormatting.RED)));
					continue;
				}
				
				Session playerSession = getPlayerSession(playerName);
				
				if(playerSession != null)
				{
					if(merge(session, playerSession, null) != null)
					{
						sender.addChatMessage(new ChatComponentText("Failed to add player \""+playerName+"\": Can't merge with the session that the player is already in.").setChatStyle(new ChatStyle().setColor(EnumChatFormatting.RED)));
						continue;
					}
				} else if(MinestuckConfig.forceMaxSize && session.getPlayerList().size() + 1 > maxSize)
				{
					sender.addChatMessage(new ChatComponentText("Failed to add player \""+playerName+"\": The session can't accept more players with the current configurations.").setChatStyle(new ChatStyle().setColor(EnumChatFormatting.RED)));
					continue;
				}
				
				session.predefinedPlayers.put(playerName, new PredefineData());
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
	
	public static void sessionName(ICommandSender sender, ICommand command, String player, String sessionName) throws CommandException
	{
		Session playerSession = getPlayerSession(player), session = sessionsByName.get(sessionName);
		if(playerSession == null)
			throw new CommandException("Couldn't find session for player \"%s\"", player);
		if(session != null)
			throw new CommandException("That session name is already taken.");
		
		if(playerSession.name != null)
			sessionsByName.remove(playerSession.name);
		String prevName = playerSession.name;
		playerSession.name = sessionName;
		sessionsByName.put(playerSession.name, playerSession);
		
		if(prevName != null)
			CommandBase.notifyOperators(sender, command, "commands.sburbSession.rename", prevName, sessionName, UsernameHandler.decode(player));
		else CommandBase.notifyOperators(sender, command, "commands.sburbSession.name", sessionName, UsernameHandler.decode(player));
	}
	
	public static void predefineTitle(ICommandSender sender, ICommand command, String player, String sessionName, Title title) throws CommandException
	{
		predefineCheck(sender, player, sessionName);
		
		Title playerTitle = MinestuckPlayerData.getTitle(player);
		if(playerTitle != null)
			throw new CommandException("You can't change your title after having entered the medium.");
		
		Session session = sessionsByName.get(sessionName);
		for(SburbConnection c : session.connections)
			if(c.isMain && c.enteredGame && title.equals(MinestuckPlayerData.getTitle(c.getClientName())))
				throw new CommandException("This title is already used by %s.", c.getClientName());
		for(Map.Entry<String, PredefineData> entry : session.predefinedPlayers.entrySet())
			if(entry.getValue().title != null && title.equals(entry.getValue().title))
				throw new CommandException("This title is already assigned to %s.", entry.getKey());
		
		PredefineData data = session.predefinedPlayers.get(player);
		data.title = title;
		CommandBase.notifyOperators(sender, command, "commands.sburbSession.titleSuccess", player, title.getTitleName());
	}
	
	public static void predefineTerrainLandAspect(ICommandSender sender, ICommand command, String player, String sessionName, TerrainLandAspect aspect) throws CommandException
	{
		predefineCheck(sender, player, sessionName);
		
		Session session = sessionsByName.get(sessionName);
		SburbConnection clientConnection = SkaianetHandler.getClientConnection(player);
		PredefineData data = session.predefinedPlayers.get(player);
		
		if(clientConnection != null && clientConnection.enteredGame())
			throw new CommandException("You can't change your land aspects after having entered the medium.");
		if(data.landTitle == null)
			throw new CommandException("You should define the other land aspect before this one.");
		if(!data.landTitle.isAspectCompatible(aspect))
			throw new CommandException("That terrain land aspect isn't compatible with the other land aspect.");
		
		data.landTerrain = aspect;
		CommandBase.notifyOperators(sender, command, "commands.sburbSession.landTerrainSuccess", player, aspect.getPrimaryName());
	}
	
	public static void predefineTitleLandAspect(ICommandSender sender, ICommand command, String player, String sessionName, TitleLandAspect aspect) throws CommandException
	{
		predefineCheck(sender, player, sessionName);
		
		Session session = sessionsByName.get(sessionName);
		SburbConnection clientConnection = SkaianetHandler.getClientConnection(player);
		PredefineData data = session.predefinedPlayers.get(player);
		
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
		CommandBase.notifyOperators(sender, command, "commands.sburbSession.landTitleSuccess", player, aspect.getPrimaryName());
	}
	
	private static void predefineCheck(ICommandSender sender, String player, String sessionName) throws CommandException
	{
		Session session = sessionsByName.get(sessionName), playerSession = getPlayerSession(player);
		if(session == null)
			throw new CommandException("Couldn't find session with the name %s", sessionName);
		if(playerSession != null && session != playerSession)
			throw new CommandException("The player is already in another session!");
		if(playerSession == null || !session.predefinedPlayers.containsKey(player))
		{
			if(sender.sendCommandFeedback())
				sender.addChatMessage(new ChatComponentText("Couldn't find session for player or player isn't registered with this session yet. Adding player to session "+sessionName));
			session.predefinedPlayers.put(player, new PredefineData());
		}
	}
	
	static List<String> getServerList(String client) {
		ArrayList<String> list = new ArrayList<String>();
		for(String server : SkaianetHandler.serversOpen.keySet()) {
			if(canConnect(client, server))
				list.add(server);
		}
		return list;
	}
	
	public static NBTTagCompound createDataTag()
	{
		NBTTagCompound nbt = new NBTTagCompound();
		NBTTagList sessionList = new NBTTagList();
		nbt.setTag("sessions", sessionList);
		int nameIndex = 1;
		for(int i = 0; i < sessions.size(); i++)
		{
			Session session = sessions.get(i);
			NBTTagList connectionList = new NBTTagList();
			for(SburbConnection c :session.connections)
			{
				NBTTagCompound connectionTag = new NBTTagCompound();
				connectionTag.setString("client", c.getClientName());
				connectionTag.setString("server", c.getServerName());
				connectionTag.setBoolean("isMain", c.isMain);
				connectionTag.setBoolean("isActive", c.isActive);
				if(c.isMain)
				{
					connectionTag.setInteger("clientDim", c.enteredGame ? c.clientHomeLand : 0);
					if(c.enteredGame && DimensionManager.isDimensionRegistered(c.clientHomeLand))
					{
						LandAspectRegistry.AspectCombination aspects = MinestuckDimensionHandler.getAspects(c.clientHomeLand);
						IChunkProvider chunkGen = MinecraftServer.getServer().worldServerForDimension(c.clientHomeLand).provider.createChunkGenerator();
						if(chunkGen instanceof ChunkProviderLands)
						{
							ChunkProviderLands landChunkGen = (ChunkProviderLands) chunkGen;
							if(landChunkGen.nameOrder)
							{
								connectionTag.setString("aspect1", aspects.aspectTerrain.getNames()[landChunkGen.nameIndex1]);
								connectionTag.setString("aspect2", aspects.aspectTitle.getNames()[landChunkGen.nameIndex2]);
							} else
							{
								connectionTag.setString("aspect1", aspects.aspectTitle.getNames()[landChunkGen.nameIndex2]);
								connectionTag.setString("aspect2", aspects.aspectTerrain.getNames()[landChunkGen.nameIndex1]);
							}
						}
						Title title = MinestuckPlayerData.getTitle(c.getClientName());
						connectionTag.setByte("class", (byte) title.getHeroClass().ordinal());
						connectionTag.setByte("aspect", (byte) title.getHeroAspect().ordinal());
					}
				}
				connectionList.appendTag(connectionTag);
			}
			
			NBTTagCompound sessionTag = new NBTTagCompound();
			if(session.name != null)
				sessionTag.setString("name", session.name);
			sessionTag.setTag("connections", connectionList);
			sessionList.appendTag(sessionTag);
		}
		return nbt;
	}
}