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
			Session target = createMergedSession(sessions, handler.skaianetHandler);
			handler.handleSuccessfulMerge(sessions, target);
			return target;
		} else if(sessions.size() == 1)
		{
			return sessions.stream().findAny().get();
		} else
		{
			Session session = new Session(handler.skaianetHandler);
			handler.addNewSession(session);
			return session;
		}
	}
	
	static Session mergedSessionFromAll(Set<Session> sessions, SkaianetHandler skaianetHandler)
	{
		Session session = new Session(skaianetHandler);
		for(Session other : sessions)
			session.inheritFrom(other);
		
		return session;
	}
	
	static List<Session> splitSession(Session originalSession, SkaianetHandler skaianetHandler)
	{
		double originalGutterMultiplier = originalSession.getGristGutter().gutterMultiplierForSession();
		Set<SburbConnection> unhandledConnections = originalSession.primaryConnections().collect(Collectors.toCollection(HashSet::new));
		List<ActiveConnection> activeConnections = skaianetHandler.activeConnections().collect(Collectors.toCollection(ArrayList::new));
		
		//Pick out as many session chains that we can from the remaining connections
		List<Session> sessions = new ArrayList<>();
		while(!unhandledConnections.isEmpty())
		{
			sessions.add(createSplitSession(unhandledConnections, activeConnections));
		}
		
		sessions.forEach(originalSession::removeOverlap);
		
		for(Session session : sessions)
		{
			double gutterMultiplier = session.getGristGutter().gutterMultiplierForSession();
			MutableGristSet takenGrist = originalSession.getGristGutter().takeFraction(gutterMultiplier/originalGutterMultiplier);
			session.getGristGutter().addGristFrom(takenGrist);
			originalGutterMultiplier -= gutterMultiplier;
		}
		
		return sessions;
	}
	
	private static Session createSplitSession(Set<SburbConnection> unhandledConnections, List<ActiveConnection> activeConnections)
	{
		SburbConnection next = unhandledConnections.iterator().next();
		Set<PlayerIdentifier> players = new HashSet<>();
		players.add(next.getClientIdentifier());
		Session newSession = new Session(next.skaianet);
		
		collectConnectionsWithMembers(unhandledConnections, activeConnections, players,
				connection -> newSession.addConnection(connection.getClientIdentifier(), connection.getServerIdentifier()));
		
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
	
	private static Session createMergedSession(Set<Session> sessions, SkaianetHandler skaianetHandler)
	{
		Session mergedSession = new Session(skaianetHandler);
		for(Session session : sessions)
			mergedSession.inheritFrom(session);
		return mergedSession;
	}
}