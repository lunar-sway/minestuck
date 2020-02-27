package com.mraof.minestuck.skaianet;

import com.mraof.minestuck.MinestuckConfig;
import com.mraof.minestuck.player.PlayerIdentifier;

import java.util.*;
import java.util.function.Consumer;

final class SessionMerger
{
	
	static Session getValidMergedSession(SessionHandler handler, PlayerIdentifier client, PlayerIdentifier server) throws MergeResult.SessionMergeException
	{
		if(handler.singleSession)
		{
			verifyCanAdd(handler.sessions.get(0), client, server, MergeResult.GLOBAL_SESSION_FULL);
			return handler.sessions.get(0);
		} else
		{
			Session cs = handler.getPlayerSession(client), ss = handler.getPlayerSession(server);
			if(cs != null && ss != null)
			{
				Session target = createMergedSession(cs, ss);
				verifyCanAdd(target, client, server, MergeResult.MERGED_SESSION_FULL);
				handler.handleSuccessfulMerge(cs, ss, target);
				return target;
			} else if(cs != null)
			{
				verifyCanAdd(cs, client, server, MergeResult.CLIENT_SESSION_FULL);
				return cs;
			} else if(ss != null)
			{
				verifyCanAdd(ss, client, server, MergeResult.SERVER_SESSION_FULL);
				return ss;
			} else
			{
				Session session = new Session();
				verifyCanAdd(session, client, server, MergeResult.GENERIC_FAIL);
				handler.sessions.add(session);
				return session;
			}
		}
	}
	
	static Session mergedSessionFromAll(SessionHandler handler) throws MergeResult.SessionMergeException
	{
		Session session = new Session();
		for(Session other : handler.sessions)
			session.inheritFrom(other);
		
		return session;
	}
	
	static List<Session> splitSession(Session session)
	{
		if(session.locked)
			return Collections.emptyList();
		
		Set<SburbConnection> unhandledConnections = new HashSet<>(session.connections);
		Set<PlayerIdentifier> lockedPlayers = new HashSet<>();
		for(SburbConnection connection : session.connections)
		{
			if(!connection.canSplit)
			{
				unhandledConnections.remove(connection);
				lockedPlayers.add(connection.getClientIdentifier());
				if(connection.hasServerPlayer())
					lockedPlayers.add(connection.getServerIdentifier());
			}
		}
		
		lockedPlayers.addAll(session.predefinedPlayers.keySet());
		
		//Clear out all connections connected to players that should stay in the session
		collectConnectionsWithMembers(unhandledConnections, lockedPlayers, sburbConnection -> {});
		
		//Pick out as many session chains that we can from the remaining connections
		List<Session> sessions = new ArrayList<>();
		while(!unhandledConnections.isEmpty())
		{
			SburbConnection next = unhandledConnections.iterator().next();
			Set<PlayerIdentifier> players = new HashSet<>();
			players.add(next.getClientIdentifier());
			Session newSession = new Session();
			
			collectConnectionsWithMembers(unhandledConnections, players, connection -> newSession.connections.add(connection));
			sessions.add(newSession);
		}
		sessions.forEach(session1 -> session.connections.removeAll(session1.connections));
		
		return sessions;
	}
	
	private static void collectConnectionsWithMembers(Set<SburbConnection> unhandledConnections, Set<PlayerIdentifier> members, Consumer<SburbConnection> collector)
	{
		boolean addedAny;
		do
		{
			 addedAny = false;
			
			Iterator<SburbConnection> iterator = unhandledConnections.iterator();
			 while(iterator.hasNext())
			 {
			 	SburbConnection connection = iterator.next();
			 	if(members.contains(connection.getClientIdentifier()) || members.contains(connection.getServerIdentifier()))
				{
					collector.accept(connection);
					if(members.add(connection.getClientIdentifier()) || connection.hasServerPlayer() && members.add(connection.getServerIdentifier()))
						addedAny = true;
					iterator.remove();
				}
			 }
			 
		} while(addedAny);
	}
	
	private static void verifyCanAdd(Session target, PlayerIdentifier client, PlayerIdentifier server, MergeResult fullSessionResult) throws MergeResult.SessionMergeException
	{
		Set<PlayerIdentifier> players = target.getPlayerList();
		int size = players.size();
		if(!players.contains(client))
			size++;
		if(!players.contains(server))
			size++;
		
		if(target.locked && size != players.size())	//If the session is locked and we're trying to add a new player to it
			throw MergeResult.LOCKED.exception();
		
		if(MinestuckConfig.forceMaxSize && size > SessionHandler.maxSize)
			throw fullSessionResult.exception();
		
	}
	
	private static Session createMergedSession(Session s1, Session s2) throws MergeResult.SessionMergeException
	
	{
		Session mergedSession = new Session();
		mergedSession.inheritFrom(s1);
		mergedSession.inheritFrom(s2);
		return mergedSession;
	}
}