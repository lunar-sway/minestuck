package com.mraof.minestuck.skaianet;

import com.mraof.minestuck.MinestuckConfig;
import com.mraof.minestuck.player.PlayerIdentifier;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;

/**
 * An extension to SkaianetHandler with a focus on sessions
 * @author kirderf1
 */
public final class SessionHandler
{
	private static final Logger LOGGER = LogManager.getLogger();
	
	private static final String GLOBAL_SESSION_NAME = "global";
	
	/**
	 * The max numbers of players per session.
	 */
	public static final int maxSize = 144;
	
	/**
	 * If the current Minecraft world will act as if Minestuck.globalSession is true or not.
	 * Will be for example false even if Minestuck.globalSession is true if it can't merge all
	 * sessions into a single session.
	 */
	boolean singleSession;
	
	/**
	 * An array list of the current worlds sessions.
	 */
	private final Set<Session> sessions = new HashSet<>();
	private final Map<String, Session> sessionsByName = new HashMap<>();
	private final SkaianetHandler skaianetHandler;
	
	SessionHandler(SkaianetHandler skaianetHandler)
	{
		this.skaianetHandler = skaianetHandler;
		
		singleSession = MinestuckConfig.SERVER.globalSession.get();
		if(singleSession)
		{
			setGlobalSession(new Session());
		}
	}
	
	void onLoad()
	{
		if(singleSession && !MinestuckConfig.SERVER.globalSession.get())
			splitGlobalSession();
		else if(!singleSession && MinestuckConfig.SERVER.globalSession.get())
			mergeAll();
	}
	
	/**
	 * Merges all available sessions into one if it can.
	 * Used in the conversion of a non-global session world
	 * to a global session world.
	 */
	void mergeAll()
	{
		try
		{
			Session session = SessionMerger.mergedSessionFromAll(sessions);
			sessions.clear();
			sessionsByName.clear();
			
			setGlobalSession(session);
			
			singleSession = true;
			
		} catch(MergeResult.SessionMergeException e)
		{
			LOGGER.warn("Not able to merge all sessions together! Global session temporarily disabled for this time.");
		}
	}
	
	/**
	 * Looks for the session that the player is a part of.
	 * @param player A string of the player's username.
	 * @return A session that contains at least one connection, that the player is a part of.
	 */
	public Session getPlayerSession(PlayerIdentifier player)
	{
		if(singleSession)
			return getGlobalSession();
		for(Session s : sessions)
			if(s.containsPlayer(player))
				return s;
		return null;
	}
	
	Session getGlobalSession()
	{
		if(!singleSession)
			throw new IllegalStateException("Should not deal with global sessions at this time");
		Session s = sessionsByName.get(GLOBAL_SESSION_NAME);
		if(s == null)
		{
			LOGGER.error("Global session was not present on getGlobalSession() call. This should not happen! Creating a new global session.");
			s = new Session();
			setGlobalSession(s);
		}
		return s;
	}
	
	private void setGlobalSession(Session session)
	{
		if(!sessions.isEmpty() || !sessionsByName.isEmpty() || !skaianetHandler.connections.isEmpty())
			throw new IllegalStateException("Trying to set the global session to a non-cleared state!");
		session.name = GLOBAL_SESSION_NAME;
		addNewSession(session);
	}
	
	/**
	 * Splits up the main session into small sessions.
	 * Used for the conversion of a global session world to
	 * a non-global session.
	 */
	void splitGlobalSession()
	{
		if(MinestuckConfig.SERVER.globalSession.get() || !singleSession)
			return;
		
		Session s = getGlobalSession();
		split(s);
		singleSession = false;
	}
	
	private void split(Session session)
	{
		List<Session> sessions = SessionMerger.splitSession(session);
		sessions.forEach(session1 -> session1.checkIfCompleted(singleSession));
		this.sessions.addAll(sessions);
		removeIfEmpty(session);
	}
	
	void onConnectionChainBroken(Session session)
	{
		if(!singleSession)
			split(session);
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
		
		return cClient != null && sClient == sServer && (MinestuckConfig.SERVER.allowSecondaryConnections.get() || cClient == cServer)	//Reconnect within session
				|| cClient == null && !serverActive && !(sClient != null && sClient.locked) && !(sServer != null && sServer.locked);	//Connect with a new player and potentially create a main connection
	}
	
	void onConnectionCreated(SburbConnection connection) throws MergeResult.SessionMergeException
	{
		if(!canConnect(connection.getClientIdentifier(), connection.getServerIdentifier()))
			throw MergeResult.GENERIC_FAIL.exception();
		
		Session session = SessionMerger.getValidMergedSession(this, connection.getClientIdentifier(), connection.getServerIdentifier());
		session.connections.add(connection);
	}
	
	/**
	 * @param normal If the connection was closed by normal means.
	 * (includes everything but getting crushed by a meteor and other reasons for removal of a main connection)
	 */
	void onConnectionClosed(SburbConnection connection, boolean normal)
	{	//TODO the design of this function may need to be looked over
		Session s = getPlayerSession(connection.getClientIdentifier());
		Objects.requireNonNull(s);	//If the connection exists, then there should be a session that contains it
		if(!connection.isMain())
		{
			s.connections.remove(connection);
			onConnectionChainBroken(s);
		} else if(!normal) {
			s.connections.remove(connection);
			if(skaianetHandler.getAssociatedPartner(connection.getClientIdentifier(), false) != null)
			{
				SburbConnection c = skaianetHandler.getMainConnection(connection.getClientIdentifier(), false);
				if(c.isActive())
					skaianetHandler.closeConnection(c.getClientIdentifier(), c.getServerIdentifier(), true);
				switch(MinestuckConfig.SERVER.escapeFailureMode.get())
				{
					case CLOSE:
						c.removeServerPlayer();
						c.setNewServerPlayer(connection.getServerIdentifier());
						break;
					case OPEN:
						c.removeServerPlayer();
						break;
				}
			}
			onConnectionChainBroken(s);
		}
	}
	
	Map<Integer, String> getServerList(PlayerIdentifier client)
	{
		Map<Integer, String> map = new HashMap<>();
		for(PlayerIdentifier server : skaianetHandler.openedServers.keySet())
		{
			if(canConnect(client, server))
			{
				map.put(server.getId(), server.getUsername());
			}
		}
		return map;
	}
	
	private void correctAndAddSession(Session session)
	{
		if(session.isCustom() && sessionsByName.containsKey(session.name))
		{
			LOGGER.error("Found session with name that is already being used. Removing session name before adding.");
			session.name = null;
		}
		try
		{
			addNewSession(session);
		} catch(RuntimeException e)
		{
			LOGGER.error("Failed to add session. This might lead to loss of data, so I hope you've got a backup! Reason \"{}\"", e.getMessage());
		}
	}
	
	void addNewSession(Session session)
	{
		if(sessions.contains(session))
			throw new IllegalStateException("Session has already been added: " + session.name);
		else if(session.isCustom() && sessionsByName.containsKey(session.name))
			throw new IllegalStateException("Session name is already in use: " + session.name);
		else if(session.connections.stream().anyMatch(skaianetHandler.connections::contains))
			throw new IllegalStateException("Session contained connections that have already been added: " + session.name);
		else
		{
			sessions.add(session);
			if(session.isCustom())
				sessionsByName.put(session.name, session);
			skaianetHandler.connections.addAll(session.connections);
		}
	}
	
	private void removeIfEmpty(Session session)
	{
		if(!singleSession && session.isEmpty())
		{
			sessions.remove(session);
			sessionsByName.remove(session.name);
		}
	}
	
	void handleSuccessfulMerge(Session s1, Session s2, Session result)
	{
		sessions.remove(s1);
		sessionsByName.remove(s1.name);
		sessions.remove(s2);
		sessionsByName.remove(s2.name);
		sessions.add(result);
		if(result.isCustom())
			sessionsByName.put(result.name, result);
	}
	
	/*
	public static List<String> getSessionNames()
	{
		List<String> list = Lists.<String>newArrayList();
		for(Session session : sessions)
			if(session.name != null)
				list.add(session.name);
		return list;
	}*/
	
	void read(CompoundNBT nbt)
	{
		sessions.clear();
		sessionsByName.clear();
		
		if(nbt.contains("session", Constants.NBT.TAG_COMPOUND))
		{
			//read as global session
			singleSession = true;
			setGlobalSession(Session.read(nbt.getCompound("session"), skaianetHandler));
		} else
		{
			singleSession = false;
			ListNBT list = nbt.getList("sessions", Constants.NBT.TAG_COMPOUND);
			for(int i = 0; i < list.size(); i++)
				correctAndAddSession(Session.read(list.getCompound(i), skaianetHandler));
		}
	}
	
	void write(CompoundNBT compound)
	{
		if(singleSession)
		{
			compound.put("session", getGlobalSession().write());
		} else
		{
			ListNBT list = new ListNBT();
			
			for(Session s : sessions)
				list.add(s.write());
			
			compound.put("sessions", list);
		}
	}
	
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