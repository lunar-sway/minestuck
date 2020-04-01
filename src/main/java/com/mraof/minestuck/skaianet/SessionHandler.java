package com.mraof.minestuck.skaianet;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mraof.minestuck.MinestuckConfig;
import com.mraof.minestuck.command.DebugLandsCommand;
import com.mraof.minestuck.command.SburbConnectionCommand;
import com.mraof.minestuck.player.IdentifierHandler;
import com.mraof.minestuck.player.PlayerIdentifier;
import com.mraof.minestuck.util.Debug;
import com.mraof.minestuck.world.MSDimensionTypes;
import com.mraof.minestuck.world.lands.LandTypePair;
import net.minecraft.command.CommandSource;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.util.Constants;

import java.util.*;

/**
 * An extension to SkaianetHandler with a focus on sessions
 * @author kirderf1
 */
public final class SessionHandler
{
	private static final String GLOBAL_SESSION_NAME = "global";
	
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
	private final Set<Session> sessions = new HashSet<>();
	private final Map<String, Session> sessionsByName = new HashMap<>();
	private final SkaianetHandler skaianetHandler;
	
	SessionHandler(SkaianetHandler skaianetHandler)
	{
		this.skaianetHandler = skaianetHandler;
	}
	
	void onLoad()
	{
		singleSession = sessionsByName.containsKey(GLOBAL_SESSION_NAME) && sessions.size() == 1;	//TODO Make this into a saved property instead
		if(singleSession && !MinestuckConfig.globalSession.get()) {
			splitGlobalSession();
		} else if(!singleSession && MinestuckConfig.globalSession.get())
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
		try
		{
			Session session = SessionMerger.mergedSessionFromAll(sessions);
			sessions.clear();
			sessionsByName.clear();
			session.name = GLOBAL_SESSION_NAME;
			sessions.add(session);
			sessionsByName.put(session.name, session);
		} catch(MergeResult.SessionMergeException e)
		{
			singleSession = false;
			Debug.warn("Not able to merge all sessions together! Global session temporarily disabled for this time.");
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
		return sessionsByName.get(GLOBAL_SESSION_NAME);
	}
	
	/**
	 * Splits up the main session into small sessions.
	 * Used for the conversion of a global session world to
	 * a non-global session.
	 */
	void splitGlobalSession()
	{
		if(MinestuckConfig.globalSession.get() || sessions.size() != 1)
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
		if(session.connections.isEmpty() && !session.isCustom())
			this.sessions.remove(session);
	}
	
	private void onConnectionChainBroken(Session session)
	{
		if(singleSession)
			return;
		if(session.connections.isEmpty() && !session.isCustom())
			sessions.remove(session);
		else split(session);
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
	{
		Session s = getPlayerSession(connection.getClientIdentifier());
		
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
				switch(MinestuckConfig.escapeFailureMode) {
				case 0:
					c.removeServerPlayer();
					c.setNewServerPlayer(connection.getServerIdentifier());
					break;
				case 1:
					c.removeServerPlayer();
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
	
	public int connectByCommand(CommandSource source, PlayerIdentifier client, PlayerIdentifier server) throws CommandSyntaxException
	{
		try
		{
			Session target = SessionMerger.getValidMergedSession(this, client, server);
			if(target.locked)
			{
				throw SburbConnectionCommand.LOCKED_EXCEPTION.create();
			}
			
			if(forceConnection(target, client, server))
			{
				source.sendFeedback(new TranslationTextComponent(SburbConnectionCommand.SUCCESS, client.getUsername(), server.getUsername()), true);
				return 1;
			} else
			{
				throw SburbConnectionCommand.CONNECTED_EXCEPTION.create();
			}
		} catch(MergeResult.SessionMergeException e)
		{
			throw SburbConnectionCommand.MERGE_EXCEPTION.create(e.getResult());
		}
	}
	
	private boolean forceConnection(Session session, PlayerIdentifier client, PlayerIdentifier server)
	{
		SburbConnection cc = skaianetHandler.getMainConnection(client, true), cs = skaianetHandler.getMainConnection(server, false);
		
		if(cc != null && cc == cs || session.locked)
			return false;
		
		boolean updateLandChain = false;
		if(cs != null)
		{
			if(cs.isActive())
				skaianetHandler.closeConnection(server, cs.getClientIdentifier(), false);
			cs.removeServerPlayer();
			updateLandChain = cs.hasEntered();
		}
		
		if(cc != null && cc.isActive())
			skaianetHandler.closeConnection(client, cc.getServerIdentifier(), true);
		
		SburbConnection connection = skaianetHandler.getConnection(client, server);
		if(cc == null)
		{
			if(connection != null)
				cc = connection;
			else
			{
				cc = new SburbConnection(client, server, skaianetHandler);
				skaianetHandler.connections.add(cc);
				session.connections.add(cc);
				SburbHandler.onConnectionCreated(cc);
			}
			cc.setIsMain();
		} else
		{
			cc.removeServerPlayer();
			cc.setNewServerPlayer(server);
			if(connection != null && connection.isActive())
			{
				skaianetHandler.connections.remove(connection);
				session.connections.remove(connection);
				cc.setActive(connection.getClientComputer(), connection.getServerComputer());
			}
			updateLandChain |= cc.hasEntered();
		}
		
		onConnectionChainBroken(session);
		
		skaianetHandler.updateAll();
		if(updateLandChain)
			skaianetHandler.infoTracker.reloadLandChains();
		
		return true;
	}
	
	void addNewSession(Session session)
	{
		if(sessions.contains(session))
			throw new IllegalStateException("Session has already been added");
		else if(sessionsByName.containsKey(session.name))
			throw new IllegalStateException("Session name is already in use");
		else
		{
			sessions.add(session);
			sessionsByName.put(session.name, session);
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
	
	public void createDebugLandsChain(ServerPlayerEntity player, List<LandTypePair> landTypes, CommandSource source) throws CommandSyntaxException
	{
		PlayerIdentifier identifier = IdentifierHandler.encode(player);
		Session s = getPlayerSession(identifier);
		if(s != null && s.locked)
			throw SburbConnectionCommand.LOCKED_EXCEPTION.create();
		
		SburbConnection cc = skaianetHandler.getMainConnection(identifier, true);
		if(s == null || cc == null || !cc.hasEntered())
			throw DebugLandsCommand.MUST_ENTER_EXCEPTION.create();
		if(cc.isActive())
			skaianetHandler.closeConnection(identifier, cc.getServerIdentifier(), true);
		
		SburbConnection cs = skaianetHandler.getMainConnection(identifier, false);
		if(cs != null) {
			if(cs.isActive())
				skaianetHandler.closeConnection(identifier, cs.getClientIdentifier(), false);
			cs.removeServerPlayer();
			source.sendFeedback(new StringTextComponent(identifier.getUsername()+"'s old client player "+cs.getClientIdentifier().getUsername()+" is now without a server player.").setStyle(new Style().setColor(TextFormatting.YELLOW)), true);
		}
		
		cc.removeServerPlayer();
		SburbConnection c = cc;
		int i = 0;
		for(; i < landTypes.size(); i++)
		{
			LandTypePair land = landTypes.get(i);
			if(land == null)
				break;
			PlayerIdentifier fakePlayer = IdentifierHandler.createNewFakeIdentifier();
			c.setNewServerPlayer(fakePlayer);
			
			c = skaianetHandler.makeConnectionWithLand(land, createDebugLand(land), fakePlayer, IdentifierHandler.NULL_IDENTIFIER, s);
		}
		
		if(i == landTypes.size())
			c.setNewServerPlayer(identifier);
		else
		{
			PlayerIdentifier lastIdentifier = identifier;
			for(i = landTypes.size() - 1; i >= 0; i++)
			{
				LandTypePair land = landTypes.get(i);
				if(land == null)
					break;
				PlayerIdentifier fakePlayer = IdentifierHandler.createNewFakeIdentifier();
				
				c = skaianetHandler.makeConnectionWithLand(land, createDebugLand(land), fakePlayer, lastIdentifier, s);
				
				lastIdentifier = fakePlayer;
			}
		}
		
		skaianetHandler.updateAll();
		skaianetHandler.infoTracker.reloadLandChains();
	}
	
	private static DimensionType createDebugLand(LandTypePair landTypes) throws CommandSyntaxException
	{
		String base = "minestuck:debug_land";
		
		ResourceLocation landName = new ResourceLocation(base);
		
		for(int i = 0; DimensionType.byName(landName) != null; i++)
		{
			landName = new ResourceLocation(base+"_"+i);
		}
		
		return DimensionManager.registerDimension(landName, MSDimensionTypes.LANDS, null, true);
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
		ListNBT list = nbt.getList("sessions", Constants.NBT.TAG_COMPOUND);
		for(int i = 0; i < list.size(); i++)
		{
			Session session = Session.read(list.getCompound(i), skaianetHandler);
			sessions.add(session);
			skaianetHandler.connections.addAll(session.connections);
			
			if(session.isCustom())
			{
				if(sessionsByName.containsKey(session.name))
					Debug.warnf("A session with a duplicate name has been loaded! (Session '%s') Either a bug or someone messing with the data file.", session.name);
				sessionsByName.put(session.name, session);
			}
		}
	}
	
	void write(CompoundNBT compound)
	{
		ListNBT list = new ListNBT();
		
		for(Session s : sessions)
			list.add(s.write());
		
		compound.put("sessions", list);
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