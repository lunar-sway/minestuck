package com.mraof.minestuck.skaianet;

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
	
	static Set<PlayerIdentifier> collectAllConnectedPlayers(PlayerIdentifier player, SkaianetHandler skaianet)
	{
		Set<PlayerIdentifier> players = new HashSet<>();
		Queue<PlayerIdentifier> uncheckedPlayers = new LinkedList<>();
		
		players.add(player);
		
		for(PlayerIdentifier nextPlayer = player; nextPlayer != null; nextPlayer = uncheckedPlayers.poll())
		{
			findDirectlyConnectedPlayers(nextPlayer, skaianet, connectedPlayer -> {
				if(players.add(connectedPlayer))
					uncheckedPlayers.add(connectedPlayer);
			});
		}
		
		return players;
	}
	
	private static void findDirectlyConnectedPlayers(PlayerIdentifier player, SkaianetHandler skaianetHandler, Consumer<PlayerIdentifier> playerConsumer)
	{
		skaianetHandler.primaryPartnerForClient(player).ifPresent(playerConsumer);
		skaianetHandler.primaryPartnerForServer(player).ifPresent(playerConsumer);
		
		skaianetHandler.getActiveConnection(player).ifPresent(connection -> playerConsumer.accept(connection.server()));
		skaianetHandler.activeConnections().filter(connection -> connection.server().equals(player)).forEach(connection -> playerConsumer.accept(connection.client()));
	}
	
	private static Session createMergedSession(Set<Session> sessions, SkaianetHandler skaianetHandler)
	{
		Session mergedSession = new Session(skaianetHandler);
		for(Session session : sessions)
			mergedSession.inheritFrom(session);
		return mergedSession;
	}
}