package com.mraof.minestuck.skaianet;

import com.mraof.minestuck.api.alchemy.MutableGristSet;
import com.mraof.minestuck.player.PlayerIdentifier;

import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

final class SessionMerger
{
	static Session getValidMergedSession(DefaultSessionHandler handler, PlayerIdentifier... players)
	{
		Set<Session> sessions = Arrays.stream(players).map(handler::getPlayerSession).filter(Objects::nonNull).collect(Collectors.toSet());
		
		if(sessions.size() > 1)
		{
			Session target = createMergedSession(sessions);
			handler.handleSuccessfulMerge(sessions, target);
			return target;
		} else if(sessions.size() == 1)
		{
			return sessions.stream().findAny().get();
		} else
		{
			Session session = new Session();
			handler.addNewSession(session);
			return session;
		}
	}
	
	static Session mergedSessionFromAll(Set<Session> sessions)
	{
		Session session = new Session();
		for(Session other : sessions)
			session.inheritFrom(other);
		
		return session;
	}
	
	static List<Session> splitSession(Session originalSession, List<ActiveConnection> activeConnections)
	{
		double originalGutterMultiplier = originalSession.getGristGutter().gutterMultiplierForSession();
		Set<SburbConnection> unhandledConnections = new HashSet<>(originalSession.connections);
		Set<PlayerIdentifier> unhandledPredefine = new HashSet<>(originalSession.predefinedPlayers.keySet());
		activeConnections = new ArrayList<>(activeConnections);
		
		//Pick out as many session chains that we can from the remaining connections
		List<Session> sessions = new ArrayList<>();
		while(!unhandledConnections.isEmpty())
		{
			sessions.add(createSplitSession(originalSession, unhandledConnections, activeConnections, unhandledPredefine));
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
		
		for(Session session : sessions)
		{
			double gutterMultiplier = session.getGristGutter().gutterMultiplierForSession();
			MutableGristSet takenGrist = originalSession.getGristGutter().takeFraction(gutterMultiplier/originalGutterMultiplier);
			session.getGristGutter().addGristFrom(takenGrist);
			originalGutterMultiplier -= gutterMultiplier;
		}
		
		return sessions;
	}
	
	private static Session createSplitSession(Session originalSession, Set<SburbConnection> unhandledConnections, List<ActiveConnection> activeConnections, Set<PlayerIdentifier> predefinedPlayers)
	{
		SburbConnection next = unhandledConnections.iterator().next();
		Set<PlayerIdentifier> players = new HashSet<>();
		players.add(next.getClientIdentifier());
		Session newSession = new Session();
		
		collectConnectionsWithMembers(unhandledConnections, activeConnections, players, newSession.connections::add);
		for(PlayerIdentifier identifier : players)
		{
			if(originalSession.predefinedPlayers.containsKey(identifier))
				newSession.predefinedPlayers.put(identifier, originalSession.predefinedPlayers.get(identifier));
		}
		predefinedPlayers.removeAll(players);
		
		return newSession;
	}
	
	private static void collectConnectionsWithMembers(Set<SburbConnection> unhandledConnections, List<ActiveConnection> activeConnections, Set<PlayerIdentifier> members, Consumer<SburbConnection> collector)
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
			 
			 Iterator<ActiveConnection> iterator1 = activeConnections.iterator();
			 while(iterator1.hasNext())
			 {
				 ActiveConnection connection = iterator1.next();
				 if(members.contains(connection.client()) || members.contains(connection.server()))
				 {
					 if(members.add(connection.client()) || members.add(connection.server()))
						 addedAny = true;
					 iterator1.remove();
				 }
			 }
			 
		} while(addedAny);
	}
	
	private static Session createMergedSession(Set<Session> sessions)
	{
		Session mergedSession = new Session();
		for(Session session : sessions)
			mergedSession.inheritFrom(session);
		return mergedSession;
	}
}