package com.mraof.minestuck.skaianet;

import com.mraof.minestuck.MinestuckConfig;
import com.mraof.minestuck.player.PlayerIdentifier;

import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

final class SessionMerger
{
	static Session getValidMergedSession(DefaultSessionHandler handler, PlayerIdentifier... players) throws MergeResult.SessionMergeException
	{
		Set<Session> sessions = Arrays.stream(players).map(handler::getPlayerSession).filter(Objects::nonNull).collect(Collectors.toSet());
		
		if(sessions.size() > 1)
		{
			Session target = createMergedSession(sessions);
			verifyCanAdd(target, MergeResult.MERGED_SESSION_FULL, players);
			handler.handleSuccessfulMerge(sessions, target);
			return target;
		} else if(sessions.size() == 1)
		{
			Session session = sessions.stream().findAny().get();
			verifyCanAdd(session, MergeResult.SESSION_FULL, players);
			return session;
		} else
		{
			Session session = new Session();
			verifyCanAdd(session, MergeResult.GENERIC_FAIL, players);
			handler.addNewSession(session);
			return session;
		}
	}
	
	static Session verifyCanAddToGlobal(Session session, PlayerIdentifier... players) throws MergeResult.SessionMergeException
	{
		verifyCanAdd(session, MergeResult.GLOBAL_SESSION_FULL, players);
		return session;
	}
	
	static Session mergedSessionFromAll(Set<Session> sessions) throws MergeResult.SessionMergeException
	{
		Session session = new Session();
		for(Session other : sessions)
			session.inheritFrom(other);
		
		return session;
	}
	
	static List<Session> splitSession(Session originalSession)
	{
		if(originalSession.locked)
			return Collections.emptyList();
		
		Set<SburbConnection> unhandledConnections = new HashSet<>(originalSession.connections);
		Set<PlayerIdentifier> unhandledPredefine = new HashSet<>(originalSession.predefinedPlayers.keySet());
		
		clearSessionLockedPlayers(originalSession, unhandledConnections, unhandledPredefine);
		
		//Pick out as many session chains that we can from the remaining connections
		List<Session> sessions = new ArrayList<>();
		while(!unhandledConnections.isEmpty())
		{
			sessions.add(createSplitSession(originalSession, unhandledConnections, unhandledPredefine));
		}
		
		//Create sessions from all predefined players that doesn't need to belong to a specific session
		for(PlayerIdentifier predefinedPlayer : unhandledPredefine)
		{
			Session session = new Session();
			
			session.predefinedPlayers.put(predefinedPlayer, originalSession.predefinedPlayers.get(predefinedPlayer));
			
			sessions.add(session);
		}
		
		sessions.forEach(session1 -> originalSession.connections.removeAll(session1.connections));
		sessions.forEach(session1 -> originalSession.predefinedPlayers.keySet().removeAll(session1.predefinedPlayers.keySet()));
		
		return sessions;
	}
	
	/**
	 * Clears out any connections and players that are locked to the session (and connections connected to these) from the provided sets.
	 */
	private static void clearSessionLockedPlayers(Session session, Set<SburbConnection> connections, Set<PlayerIdentifier> predefinedPlayers)
	{
		Set<PlayerIdentifier> lockedPlayers = new HashSet<>();
		//Add locked players from connections
		for(SburbConnection connection : session.connections)
		{
			if(connection.lockedToSession)
			{
				connections.remove(connection);
				lockedPlayers.add(connection.getClientIdentifier());
				if(connection.hasServerPlayer())
					lockedPlayers.add(connection.getServerIdentifier());
			}
		}
		//Add locked players from predefined data
		for(Map.Entry<PlayerIdentifier, PredefineData> entry : session.predefinedPlayers.entrySet())
		{
			if(entry.getValue().isLockedToSession())
				lockedPlayers.add(entry.getKey());
		}
		
		//Clear out all connections connected to players that should stay in the session
		collectConnectionsWithMembers(connections, lockedPlayers, sburbConnection -> {});
		
		predefinedPlayers.removeAll(lockedPlayers);
		
	}
	
	private static Session createSplitSession(Session originalSession, Set<SburbConnection> unhandledConnections, Set<PlayerIdentifier> predefinedPlayers)
	{
		SburbConnection next = unhandledConnections.iterator().next();
		Set<PlayerIdentifier> players = new HashSet<>();
		players.add(next.getClientIdentifier());
		Session newSession = new Session();
		
		collectConnectionsWithMembers(unhandledConnections, players, newSession.connections::add);
		for(PlayerIdentifier identifier : players)
		{
			if(originalSession.predefinedPlayers.containsKey(identifier))
				newSession.predefinedPlayers.put(identifier, originalSession.predefinedPlayers.get(identifier));
		}
		predefinedPlayers.removeAll(players);
		
		return newSession;
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
	
	private static void verifyCanAdd(Session target, MergeResult fullSessionResult, PlayerIdentifier... players) throws MergeResult.SessionMergeException
	{
		Set<PlayerIdentifier> playersInSession = target.getPlayerList();
		int size = playersInSession.size();
		for(PlayerIdentifier player : players)
		{
			if(!playersInSession.contains(player))
				size++;
		}
		
		if(target.locked && size != playersInSession.size())	//If the session is locked and we're trying to add a new player to it
			throw MergeResult.LOCKED.exception();
		
		if(MinestuckConfig.SERVER.forceMaxSize && size > SessionHandler.MAX_SIZE)
			throw fullSessionResult.exception();
	}
	
	private static Session createMergedSession(Set<Session> sessions) throws MergeResult.SessionMergeException
	{
		Session mergedSession = new Session();
		for(Session session : sessions)
			mergedSession.inheritFrom(session);
		return mergedSession;
	}
}