package com.mraof.minestuck.skaianet;

import com.mraof.minestuck.MinestuckConfig;
import com.mraof.minestuck.util.Debug;
import com.mraof.minestuck.player.IdentifierHandler;
import com.mraof.minestuck.player.PlayerIdentifier;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;

import java.util.*;

/**
 * Handles session related stuff like title generation, consort choosing, and other session management stuff.
 * @author kirderf1
 */
public class SessionHandler
{
	public static final String CONNECT_FAILED = "minestuck.connect_failed_message";
	public static final String SINGLE_SESSION_FULL = "minestuck.single_session_full_message";
	public static final String CLIENT_SESSION_FULL = "minestuck.client_session_full_message";
	public static final String SERVER_SESSION_FULL = "minestuck.server_session_full_message";
	public static final String BOTH_SESSIONS_FULL = "minestuck.both_sessions_full_message";
	
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
	boolean singleSession;
	
	/**
	 * An array list of the current worlds sessions.
	 */
	List<Session> sessions = new ArrayList<>();
	Map<String, Session> sessionsByName = new HashMap<>();
	final SkaianetHandler skaianetHandler;
	
	SessionHandler(SkaianetHandler skaianetHandler)
	{
		this.skaianetHandler = skaianetHandler;
	}
	
	void onLoad()
	{
		singleSession = MinestuckConfig.globalSession.get();
		if(!MinestuckConfig.globalSession.get()) {
			split();
		} else
		{
			mergeAll();
		}
	}
	
	/**
	 * Merges all available sessions into one if it can.
	 * Used in the conversion of a non-global session world
	 * to a global session world.
	 */
	void mergeAll()
	{
		if(sessions.size() == 0 ||!canMergeAll())
		{
			singleSession = sessions.size() == 0;
			if(!singleSession)
				Debug.warn("Not allowed to merge all sessions together! Global session temporarily disabled for this time.");
			else
			{
				Session mainSession = new Session();
				mainSession.name = GLOBAL_SESSION_NAME;
				sessions.add(mainSession);
				sessionsByName.put(mainSession.name, mainSession);
			}
			
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
	private boolean canMergeAll()
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
	public Session getPlayerSession(PlayerIdentifier player)
	{
		if(singleSession)
			return sessions.get(0);
		for(Session s : sessions)
			if(s.containsPlayer(player))
				return s;
		return null;
	}
	
	String merge(Session cs, Session ss, SburbConnection sb)
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
			return CONNECT_FAILED;
		if(MinestuckConfig.forceMaxSize && s0.getPlayerList().size()+s1.getPlayerList().size()>maxSize)
			return BOTH_SESSIONS_FULL;
		return null;
	}
	
	/**
	 * Splits up the main session into small sessions.
	 * Used for the conversion of a global session world to
	 * a non-global session.
	 */
	void split()
	{
		if(MinestuckConfig.globalSession.get() || sessions.size() != 1)
			return;
		
		Session s = sessions.get(0);
		split(s);
	}
	
	void split(Session session)
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
					if(s.containsPlayer(c.getClientIdentifier()) || s.containsPlayer(c.getServerIdentifier()) || first && !c.canSplit)
					{
						found = true;
						iter.remove();
						s.connections.add(c);
					}
				}
			} while(found);
			s.checkIfCompleted(singleSession);
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
	private boolean canConnect(PlayerIdentifier client, PlayerIdentifier server)
	{
		Session sClient = getPlayerSession(client), sServer = getPlayerSession(server);
		SburbConnection cClient = skaianetHandler.getMainConnection(client, true);
		SburbConnection cServer = skaianetHandler.getMainConnection(server, false);
		boolean serverActive = cServer != null;
		if(!serverActive && sServer != null)
			for(SburbConnection c : sServer.connections)
				if(c.getServerIdentifier().equals(server))
				{
					serverActive = true;
					break;
				}
		
		return cClient != null && sClient == sServer && (MinestuckConfig.allowSecondaryConnections.get() || cClient == cServer)	//Reconnect within session
				|| cClient == null && !serverActive && !(sClient != null && sClient.locked) && !(sServer != null && sServer.locked);	//Connect with a new player and potentially create a main connection
	}
	
	/**
	 * @return Null if successful or an unlocalized error message describing reason.
	 */
	String onConnectionCreated(SburbConnection connection)
	{
		if(!canConnect(connection.getClientIdentifier(), connection.getServerIdentifier()))
			return CONNECT_FAILED;
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
			
			int i = (sessions.get(0).containsPlayer(connection.getClientIdentifier())?0:1)+(connection.getServerIdentifier().equals(IdentifierHandler.NULL_IDENTIFIER) || sessions.get(0).containsPlayer(connection.getServerIdentifier())?0:1);
			if(MinestuckConfig.forceMaxSize && sessions.get(0).getPlayerList().size()+i > maxSize)
				return SINGLE_SESSION_FULL;
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
				if((sClient == null?sServer:sClient).locked || MinestuckConfig.forceMaxSize && !connection.getServerIdentifier().equals(IdentifierHandler.NULL_IDENTIFIER) && (sClient == null?sServer:sClient).getPlayerList().size()+1 > maxSize)
					return sClient == null ? SERVER_SESSION_FULL : CLIENT_SESSION_FULL;
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
	void onConnectionClosed(SburbConnection connection, boolean normal)
	{
		Session s = getPlayerSession(connection.getClientIdentifier());
		
		if(!connection.isMain())
		{
			s.connections.remove(connection);
			if(!singleSession)
				if(s.connections.size() == 0 && !s.isCustom())
					sessions.remove(s);
				else split(s);
		} else if(!normal) {
			s.connections.remove(connection);
			if(skaianetHandler.getAssociatedPartner(connection.getClientIdentifier(), false) != null)
			{
				SburbConnection c = skaianetHandler.getMainConnection(connection.getClientIdentifier(), false);
				if(c.isActive())
					skaianetHandler.closeConnection(c.getClientIdentifier(), c.getServerIdentifier(), true);
				switch(MinestuckConfig.escapeFailureMode) {
				case 0:
					c.serverIdentifier = connection.getServerIdentifier();
					break;
				case 1:
					c.serverIdentifier = IdentifierHandler.NULL_IDENTIFIER;
					break;
				}
			}
			if(s.connections.size() == 0 && !s.isCustom())
				sessions.remove(s);
		}
	}
	
	Map<Integer, String> getServerList(PlayerIdentifier client)
	{
		Map<Integer, String> map = new HashMap<>();
		for(PlayerIdentifier server : skaianetHandler.serversOpen.keySet())
		{
			if(canConnect(client, server))
			{
				map.put(server.getId(), server.getUsername());
			}
		}
		return map;
	}
	
	/*public static void connectByCommand(ICommandSender sender, ICommand command, PlayerIdentifier client, PlayerIdentifier server) throws CommandException
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
	
	public static void createDebugLandsChain(List<LandAspects> landspects, EntityPlayer player) throws CommandException
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
	
	private static int createDebugLand(LandAspects landspect) throws CommandException
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
	}*/
	
	/**
	 * Creates data to be used for the data checker
	 */
	public CompoundNBT createDataTag()
	{
		CompoundNBT nbt = new CompoundNBT();
		ListNBT sessionList = new ListNBT();
		nbt.put("sessions", sessionList);
		for(Session session : sessions)
		{
			sessionList.add(session.createDataTag());
		}
		return nbt;
	}
	
	public static SessionHandler get(MinecraftServer server)
	{
		return SkaianetHandler.get(server).sessionHandler;
	}
	
	public static SessionHandler get(World world)
	{
		return SkaianetHandler.get(world).sessionHandler;
	}
}