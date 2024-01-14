package com.mraof.minestuck.skaianet;

import com.mraof.minestuck.player.PlayerIdentifier;

import java.util.*;
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
		Set<PlayerIdentifier> unhandledClientPlayers = originalSession.primaryClientPlayers().collect(Collectors.toCollection(HashSet::new));
		List<ActiveConnection> activeConnections = skaianetHandler.activeConnections().collect(Collectors.toCollection(ArrayList::new));
		
		//Pick out as many session chains that we can from the remaining connections
		List<Session> sessions = new ArrayList<>();
		while(!originalSession.getPlayers().isEmpty())
		{
			PlayerIdentifier nextPlayer = originalSession.getPlayers().iterator().next();
			Set<PlayerIdentifier> clientPlayers = collectConnectionsWithMembers(unhandledClientPlayers, activeConnections, nextPlayer, skaianetHandler);
			
			sessions.add(originalSession.createSessionSplit(clientPlayers));
		}
		
		return sessions;
	}
	
	private static Set<PlayerIdentifier> collectConnectionsWithMembers(Set<PlayerIdentifier> unhandledClientPlayers, List<ActiveConnection> activeConnections,
													  PlayerIdentifier nextPlayer, SkaianetHandler skaianet)
	{
		Set<PlayerIdentifier> clientPlayers = new HashSet<>();
		Set<PlayerIdentifier> members = new HashSet<>();
		members.add(nextPlayer);
		boolean addedAny;
		do
		{
			 addedAny = false;
			
			Iterator<PlayerIdentifier> iterator = unhandledClientPlayers.iterator();
			while(iterator.hasNext())
			{
				PlayerIdentifier player = iterator.next();
				Optional<PlayerIdentifier> serverPlayer = skaianet.getOrCreateData(player).primaryServerPlayer();
				if(members.contains(player) || serverPlayer.isPresent() && members.contains(serverPlayer.get()))
				{
					clientPlayers.add(player);
					if(members.add(player) || serverPlayer.isPresent() && members.add(serverPlayer.get()))
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
					 clientPlayers.add(connection.client());
					 if(members.add(connection.client()) || members.add(connection.server()))
						 addedAny = true;
					 iterator1.remove();
				 }
			 }
			 
		} while(addedAny);
		return clientPlayers;
	}
	
	private static Session createMergedSession(Set<Session> sessions, SkaianetHandler skaianetHandler)
	{
		Session mergedSession = new Session(skaianetHandler);
		for(Session session : sessions)
			mergedSession.inheritFrom(session);
		return mergedSession;
	}
}