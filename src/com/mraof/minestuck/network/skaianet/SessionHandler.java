package com.mraof.minestuck.network.skaianet;

import com.google.common.collect.Lists;
import com.mraof.minestuck.MinestuckConfig;
import com.mraof.minestuck.tracker.MinestuckPlayerTracker;
import com.mraof.minestuck.util.Debug;
import com.mraof.minestuck.util.IdentifierHandler;
import com.mraof.minestuck.util.IdentifierHandler.PlayerIdentifier;
import com.mraof.minestuck.util.MinestuckPlayerData;
import com.mraof.minestuck.util.Title;
import com.mraof.minestuck.world.MinestuckDimensionHandler;
import com.mraof.minestuck.world.lands.LandAspectRegistry;
import com.mraof.minestuck.world.lands.gen.ChunkProviderLands;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraftforge.common.DimensionManager;

import java.util.*;

/**
 * Handles session related stuff like title generation, consort choosing, and other session management stuff.
 * @author kirderf1
 */
public class SessionHandler
{
	
	static final String GLOBAL_SESSION_NAME = "global";
	
	/**
	 * The max numbers of players per session.
	 */
	public static int maxSize;
	
	/**
	 * If the current Minecraft world will act as if Minestuck.globalSession is true or not.
	 * Will be for example false even if Minestuck.globalSession is true if it can't merge all
	 * sessions into a single session.
	 */
	static boolean singleSession;
	
	/**
	 * An array list of the current worlds sessions.
	 */
	static List<Session> sessions = new ArrayList<Session>();
	static Map<String, Session> sessionsByName = new HashMap<String, Session>();
	
	/**
	 * Called when the server loads a new world, after
	 * Minestuck has loaded the sessions from file.
	 */
	static void serverStarted()
	{
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
	private static void mergeAll()
	{
		if(!canMergeAll() || sessions.size() == 0)
		{
			singleSession = sessions.size() == 0;
			if(!singleSession)
				Debug.warn("Not allowed to merge all sessions together! Global session temporarily disabled for this time.");
			return;
		}
		
		Session session = sessions.get(0);
		for(int i = 1; i < sessions.size(); i++)
		{
			Session s = sessions.remove(i);
			session.connections.addAll(s.connections);
			session.predefinedPlayers.putAll(s.predefinedPlayers);	//Used only when merging the global session
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
	private static boolean canMergeAll()
	{
		if(sessions.size() == 1 && (!sessions.get(0).isCustom() || sessions.get(0).name.equals(GLOBAL_SESSION_NAME)))
				return true;
		
		int players = 0;
		boolean skaiaUsed = false, prospitUsed = false, derseUsed = false;
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
			if(s.isCustom() || s.locked)
				return false;
			players += s.getPlayerList().size();
		}
		return players <= maxSize;
	}
	
	/**
	 * Looks for the session that the player is a part of.
	 * @param player A string of the player's username.
	 * @return A session that contains at least one connection, that the player is a part of.
	 */
	public static Session getPlayerSession(PlayerIdentifier player)
	{
		if(singleSession)
			return sessions.get(0);
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
	
	private static String canMerge(Session s0, Session s1)
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
		while(!session.connections.isEmpty() || first)
		{
			Session s = new Session();
			if(!first)
			{
				s.connections.add(session.connections.remove(0));
				
			} else
			{
				if(session.isCustom() && (!session.name.equals(GLOBAL_SESSION_NAME) || !session.predefinedPlayers.isEmpty()))
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
					if(s.containsPlayer(c.getClientIdentifier()) || s.containsPlayer(c.getServerIdentifier()) || first && !c.canSplit){
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
	private static boolean canConnect(PlayerIdentifier client, PlayerIdentifier server)
	{
		Session sClient = getPlayerSession(client), sServer = getPlayerSession(server);
		SburbConnection cClient = SkaianetHandler.getMainConnection(client, true);
		SburbConnection cServer = SkaianetHandler.getMainConnection(server, false);
		boolean serverActive = cServer != null;
		if(!serverActive && sServer != null)
			for(SburbConnection c : sServer.connections)
				if(c.getServerIdentifier().equals(server))
				{
					serverActive = true;
					break;
				}
		
		return cClient != null && sClient == sServer && (MinestuckConfig.allowSecondaryConnections || cClient == cServer)	//Reconnect within session
				|| cClient == null && !serverActive && !(sClient != null && sClient.locked) && !(sServer != null && sServer.locked);	//Connect with a new player and potentially create a main connection
	}
	
	/**
	 * @return Null if successful or an unlocalized error message describing reason.
	 */
	static String onConnectionCreated(SburbConnection connection)
	{
		if(!canConnect(connection.getClientIdentifier(), connection.getServerIdentifier()))
			return "computer.messageConnectFailed";
		if(singleSession)
		{
			if(sessions.size() == 0)
			{
				Debug.error("No session in list when global session should be turned on?");
				Session session = new Session();
				session.name = GLOBAL_SESSION_NAME;
				sessions.add(session);
				sessionsByName.put(session.name, session);
			}
			
			int i = (sessions.get(0).containsPlayer(connection.getClientIdentifier())?0:1)+(connection.getServerIdentifier().equals(IdentifierHandler.nullIdentifier) || sessions.get(0).containsPlayer(connection.getServerIdentifier())?0:1);
			if(MinestuckConfig.forceMaxSize && sessions.get(0).getPlayerList().size()+i > maxSize)
				return "computer.singleSessionFull";
			else
			{
				sessions.get(0).connections.add(connection);
				return null;
			}
		} else
		{
			Session sClient = getPlayerSession(connection.getClientIdentifier()), sServer = getPlayerSession(connection.getServerIdentifier());
			if(sClient == null && sServer == null)
			{
				Session s = new Session();
				sessions.add(s);
				s.connections.add(connection);
				return null;
			} else if(sClient == null || sServer == null)
			{
				if((sClient == null?sServer:sClient).locked || MinestuckConfig.forceMaxSize && !connection.getServerIdentifier().equals(IdentifierHandler.nullIdentifier) && (sClient == null?sServer:sClient).getPlayerList().size()+1 > maxSize)
					return "computer."+(sClient == null?"server":"client")+"SessionFull";
				(sClient == null?sServer:sClient).connections.add(connection);
				return null;
			} else
			{
				if(sClient == sServer)
				{
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
	static void onConnectionClosed(SburbConnection connection, boolean normal)
	{
		Session s = getPlayerSession(connection.getClientIdentifier());
		
		if(!connection.isMain)
		{
			s.connections.remove(connection);
			if(!singleSession)
				if(s.connections.size() == 0 && !s.isCustom())
					sessions.remove(s);
				else split(s);
		} else if(!normal) {
			s.connections.remove(connection);
			if(SkaianetHandler.getAssociatedPartner(connection.getClientIdentifier(), false) != null)
			{
				SburbConnection c = SkaianetHandler.getMainConnection(connection.getClientIdentifier(), false);
				if(c.isActive)
					SkaianetHandler.closeConnection(c.getClientIdentifier(), c.getServerIdentifier(), true);
				switch(MinestuckConfig.escapeFailureMode) {
				case 0:
					c.serverIdentifier = connection.getServerIdentifier();
					break;
				case 1:
					c.serverIdentifier = IdentifierHandler.nullIdentifier;
					break;
				}
			}
			if(s.connections.size() == 0 && !s.isCustom())
				sessions.remove(s);
		}
	}
	
	static List<Object> getServerList(PlayerIdentifier client)
	{
		ArrayList<Object> list = new ArrayList<Object>();
		for(PlayerIdentifier server : SkaianetHandler.serversOpen.keySet())
		{
			if(canConnect(client, server))
			{
				list.add(server.getId());
				list.add(server.getUsername());
			}
		}
		return list;
	}
	
	public static void connectByCommand(ICommandSender sender, ICommand command, PlayerIdentifier client, PlayerIdentifier server) throws CommandException
	{
		Session sc = getPlayerSession(client), ss = getPlayerSession(server);
		
		if(singleSession)
		{
			int i = (sc == null ? 1:0) + (ss == null ? 1 : 0); 
			sc = ss = sessions.get(0);
			if(MinestuckConfig.forceMaxSize && sc.getPlayerList().size() + i > maxSize)
				throw new CommandException("computer.singleSessionFull");
		}
		else
		{
			if(sc == null && ss == null)
			{
				if(sender.sendCommandFeedback())
					sender.sendMessage(new TextComponentString("Neither player is part of a session. Creating new session..."));
				sc = ss = new Session();
				sessions.add(sc);
			} else if(sc == null)
			{
				if(ss.locked)
					throw new CommandException("The server session is locked, and can no longer be modified!");
				if(MinestuckConfig.forceMaxSize && ss.getPlayerList().size() + 1 > maxSize)
					throw new CommandException("computer.serverSessionFull");
				sc = ss;
			} else if(ss == null)
			{
				if(sc.locked)
					throw new CommandException("The client session is locked, and can no longer be modified!");
				if(MinestuckConfig.forceMaxSize && sc.getPlayerList().size() + 1 > maxSize)
					throw new CommandException("computer.clientSessionFull");
				ss = sc;
			}
		}
		
		SburbConnection cc = SkaianetHandler.getMainConnection(client, true), cs = SkaianetHandler.getMainConnection(server, false);
		
		if(cc != null && cc == cs)
			throw new CommandException("Those players are already connected!");
		
		if(sc != ss)
		{
			String merge = merge(sc, ss, null);
			if(merge != null)
				throw new CommandException(merge);
		}
		
		boolean updateLandChain = false;
		if(cs != null)
		{
			if(cs.isActive)
				SkaianetHandler.closeConnection(server, cs.getClientIdentifier(), false);
			cs.serverIdentifier = IdentifierHandler.nullIdentifier;
			if(sender.sendCommandFeedback())
				sender.sendMessage(new TextComponentString(server.getUsername()+"'s old client player "+cs.getClientIdentifier().getUsername()+" is now without a server player.").setStyle(new Style().setColor(TextFormatting.YELLOW)));
			updateLandChain |= cs.enteredGame();
		}
		
		if(cc != null && cc.isActive)
			SkaianetHandler.closeConnection(client, cc.getServerIdentifier(), true);
		
		SburbConnection connection = SkaianetHandler.getConnection(client, server);
		if(cc == null)
		{
			if(connection != null)
				cc = connection;
			else
			{
				cc = new SburbConnection();
				SkaianetHandler.connections.add(cc);
				cc.clientIdentifier = client;
				cc.serverIdentifier = server;
				sc.connections.add(cc);
				cc.isActive = false;
				SburbHandler.onConnectionCreated(cc);
			}
			cc.isMain = true;
		} else
		{
			if(connection != null && connection.isActive)
			{
				SkaianetHandler.connections.remove(connection);
				sc.connections.remove(connection);
				cc.client = connection.client;
				cc.server = connection.server;
				cc.serverIdentifier = server;
			} else cc.serverIdentifier = server;
			updateLandChain |= cc.enteredGame();
		}
		
		SkaianetHandler.updateAll();
		if(updateLandChain)
			SkaianetHandler.sendLandChainUpdate();
		
		CommandBase.notifyCommandListener(sender, command, "commands.sburbServer.success", client.getUsername(), server.getUsername());
	}
	
	public static void createDebugLandsChain(List<LandAspectRegistry.AspectCombination> landspects, EntityPlayer player) throws CommandException
	{
		PlayerIdentifier identifier = IdentifierHandler.encode(player);
		Session s = getPlayerSession(identifier);
		if(s != null && s.locked)
			throw new CommandException("The session is locked, and can no longer be modified!");
		
		SburbConnection cc = SkaianetHandler.getMainConnection(identifier, true);
		if(s == null || cc == null || !cc.enteredGame())
			throw new CommandException("You should enter before using this.");
		if(cc.isActive)
			SkaianetHandler.closeConnection(identifier, cc.getServerIdentifier(), true);
		
		SburbConnection cs = SkaianetHandler.getMainConnection(identifier, false);
		if(cs != null) {
			if(cs.isActive)
				SkaianetHandler.closeConnection(identifier, cs.getClientIdentifier(), false);
			cs.serverIdentifier = IdentifierHandler.nullIdentifier;
			if(player.sendCommandFeedback())
				player.sendMessage(new TextComponentString(identifier.getUsername()+"'s old client player "+cs.getClientIdentifier().getUsername()+" is now without a server player.").setStyle(new Style().setColor(TextFormatting.YELLOW)));
		}
		
		SburbConnection c = cc;
		int i = 0;
		for(; i < landspects.size(); i++)
		{
			LandAspectRegistry.AspectCombination land = landspects.get(i);
			if(land == null)
				break;
			PlayerIdentifier fakePlayer = IdentifierHandler.createNewFakeIdentifier();
			c.serverIdentifier = fakePlayer;
			
			c = new SburbConnection();
			c.clientIdentifier = fakePlayer;
			c.serverIdentifier = IdentifierHandler.nullIdentifier;
			c.isActive = false;
			c.isMain = true;
			c.enteredGame = true;
			c.clientHomeLand = createDebugLand(land);
			
			s.connections.add(c);
			SkaianetHandler.connections.add(c);
			SburbHandler.onConnectionCreated(c);
			
		}
		
		if(i == landspects.size())
			c.serverIdentifier = identifier;
		else
		{
			PlayerIdentifier lastIdentifier = identifier;
			for(i = landspects.size() - 1; i >= 0; i++)
			{
				LandAspectRegistry.AspectCombination land = landspects.get(i);
				if(land == null)
					break;
				PlayerIdentifier fakePlayer = IdentifierHandler.createNewFakeIdentifier();
				
				c = new SburbConnection();
				c.clientIdentifier = fakePlayer;
				c.serverIdentifier = lastIdentifier;
				c.isActive = false;
				c.isMain = true;
				c.enteredGame = true;
				c.clientHomeLand = createDebugLand(land);
				
				s.connections.add(c);
				SkaianetHandler.connections.add(c);
				SburbHandler.onConnectionCreated(c);
				
				lastIdentifier = fakePlayer;
			}
		}
		
		SkaianetHandler.updateAll();
		MinestuckPlayerTracker.updateLands();
		SkaianetHandler.sendLandChainUpdate();
	}
	
	private static int createDebugLand(LandAspectRegistry.AspectCombination landspect) throws CommandException
	{
		int landId = MinestuckDimensionHandler.landDimensionIdStart;
		while (true)
		{
			if (!DimensionManager.isDimensionRegistered(landId))
			{
				break;
			}
			else
			{
				landId++;
			}
		}
		
		MinestuckDimensionHandler.registerLandDimension(landId, landspect);
		MinestuckDimensionHandler.setSpawn(landId, new BlockPos(0,0,0));
		return landId;
	}
	
	public static List<String> getSessionNames()
	{
		List<String> list = Lists.<String>newArrayList();
		for(Session session : sessions)
			if(session.name != null)
				list.add(session.name);
		return list;
	}
	
	/**
	 * Creates data to be used for the data checker
	 */
	public static NBTTagCompound createDataTag(MinecraftServer server)
	{
		NBTTagCompound nbt = new NBTTagCompound();
		NBTTagList sessionList = new NBTTagList();
		nbt.setTag("sessions", sessionList);
		for(int i = 0; i < sessions.size(); i++)
		{
			Session session = sessions.get(i);
			NBTTagList connectionList = new NBTTagList();
			Set<PlayerIdentifier> playerSet = new HashSet<>();
			for(SburbConnection c :session.connections)
			{
				if(c.isMain)
					playerSet.add(c.getClientIdentifier());
				NBTTagCompound connectionTag = new NBTTagCompound();
				connectionTag.setString("client", c.getClientIdentifier().getUsername());
				connectionTag.setString("clientId", c.getClientIdentifier().getString());
				if(!c.getServerIdentifier().equals(IdentifierHandler.nullIdentifier))
					connectionTag.setString("server", c.getServerIdentifier().getUsername());
				connectionTag.setBoolean("isMain", c.isMain);
				connectionTag.setBoolean("isActive", c.isActive);
				if(c.isMain)
				{
					connectionTag.setInteger("clientDim", c.enteredGame ? c.clientHomeLand : 0);
					if(c.enteredGame && DimensionManager.isDimensionRegistered(c.clientHomeLand))
					{
						LandAspectRegistry.AspectCombination aspects = MinestuckDimensionHandler.getAspects(c.clientHomeLand);
						IChunkGenerator chunkGen = server.getWorld(c.clientHomeLand).provider.createChunkGenerator();
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
						Title title = MinestuckPlayerData.getTitle(c.getClientIdentifier());
						connectionTag.setByte("class", title == null ? -1 : (byte) title.getHeroClass().ordinal());
						connectionTag.setByte("aspect", title == null ? -1 : (byte) title.getHeroAspect().ordinal());
					} else if(session.predefinedPlayers.containsKey(c.getClientIdentifier()))
					{
						PredefineData data = session.predefinedPlayers.get(c.getClientIdentifier());
						
						if(data.title != null)
						{
							connectionTag.setByte("class", (byte) data.title.getHeroClass().ordinal());
							connectionTag.setByte("aspect", (byte) data.title.getHeroAspect().ordinal());
						}
						
						if(data.landTerrain != null)
							connectionTag.setString("aspectTerrain", data.landTerrain.getPrimaryName());
						if(data.landTitle != null)
							connectionTag.setString("aspectTitle", data.landTitle.getPrimaryName());
					}
				}
				connectionList.appendTag(connectionTag);
			}
			
			for(Map.Entry<PlayerIdentifier, PredefineData> entry : session.predefinedPlayers.entrySet())
			{
				if(playerSet.contains(entry.getKey()))
					continue;
				
				NBTTagCompound connectionTag = new NBTTagCompound();
				
				connectionTag.setString("client", entry.getKey().getUsername());
				connectionTag.setString("clientId", entry.getKey().getString());
				connectionTag.setBoolean("isMain", true);
				connectionTag.setBoolean("isActive", false);
				connectionTag.setInteger("clientDim", 0);
				
				PredefineData data = entry.getValue();
				
				if(data.title != null)
				{
					connectionTag.setByte("class", (byte) data.title.getHeroClass().ordinal());
					connectionTag.setByte("aspect", (byte) data.title.getHeroAspect().ordinal());
				}
				
				if(data.landTerrain != null)
					connectionTag.setString("aspectTerrain", data.landTerrain.getPrimaryName());
				if(data.landTitle != null)
					connectionTag.setString("aspectTitle", data.landTitle.getPrimaryName());
				
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