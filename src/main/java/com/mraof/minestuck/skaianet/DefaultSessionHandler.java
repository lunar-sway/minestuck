package com.mraof.minestuck.skaianet;

import com.mraof.minestuck.MinestuckConfig;
import com.mraof.minestuck.player.PlayerIdentifier;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * The standard implementation of SessionHandler which allows multiple sessions
 */
public final class DefaultSessionHandler extends SessionHandler
{
	private static final Logger LOGGER = LogManager.getLogger();
	
	/**
	 * An array list of the current worlds sessions.
	 */
	private final Set<Session> sessions = new HashSet<>();
	
	DefaultSessionHandler(SkaianetHandler skaianetHandler)
	{
		super(skaianetHandler);
	}
	
	DefaultSessionHandler(SkaianetHandler skaianetHandler, Session globalSession)
	{
		this(skaianetHandler);
		sessions.add(globalSession);
		split(globalSession);
	}
	
	DefaultSessionHandler(SkaianetHandler skaianetHandler, ListTag list)
	{
		super(skaianetHandler);
		for(int i = 0; i < list.size(); i++)
			correctAndAddSession(Session.read(list.getCompound(i), skaianetHandler));
	}
	
	@Override
	void write(CompoundTag compound)
	{
		ListTag list = new ListTag();
		
		for(Session s : sessions)
			list.add(s.write());
		
		compound.put("sessions", list);
	}
	
	@Override
	SessionHandler getActual()
	{
		if(MinestuckConfig.SERVER.globalSession.get())
			return new GlobalSessionHandler(skaianetHandler,
					Session.createMergedSession(this.sessions, this.skaianetHandler));
		
		return this;
	}
	
	@Override
	public Session getPlayerSession(PlayerIdentifier player)
	{
		for(Session s : sessions)
			if(s.containsPlayer(player))
				return s;
		return null;
	}
	
	@Override
	public Set<Session> getSessions()
	{
		return Collections.unmodifiableSet(sessions);
	}
	
	Session prepareSessionFor(PlayerIdentifier... players)
	{
		Set<Session> sessions = Arrays.stream(players).map(this::getPlayerSession).filter(Objects::nonNull).collect(Collectors.toSet());
		
		if(sessions.size() > 1)
		{
			this.sessions.removeAll(sessions);
			Session newSession = Session.createMergedSession(sessions, this.skaianetHandler);
			this.sessions.add(newSession);
			return newSession;
		} else if(sessions.size() == 1)
		{
			return sessions.stream().findAny().get();
		} else
		{
			Session session = new Session(skaianetHandler);
			this.sessions.add(session);
			return session;
		}
	}
	
	@Override
	void onConnect(PlayerIdentifier client, PlayerIdentifier server)
	{
		prepareSessionFor(client, server).addPlayer(client).addPlayer(server);
	}
	
	@Override
	void onDisconnect(PlayerIdentifier client, PlayerIdentifier server)
	{
		Session s = Objects.requireNonNull(getPlayerSession(client));
		
		SburbPlayerData playerData = skaianetHandler.getOrCreateData(client);
		if(!playerData.isPrimaryServerPlayer(server))
			split(s);
	}
	
	private void correctAndAddSession(Session session)
	{
		try
		{
			if(session.getPlayers().stream().anyMatch(player -> this.getPlayerSession(player) != null))
				throw new IllegalStateException("Session contained connections that have already been added");
			
			sessions.add(session);
		} catch(RuntimeException e)
		{
			LOGGER.error("Failed to add session. This might lead to loss of data, so I hope you've got a backup! Reason \"{}\"", e.getMessage());
		}
	}
	
	private void split(Session session)
	{
		sessions.remove(session);
		while(!session.getPlayers().isEmpty())
		{
			PlayerIdentifier remainingPlayer = session.getPlayers().iterator().next();
			Set<PlayerIdentifier> players = collectAllConnectedPlayers(remainingPlayer, skaianetHandler);
			Session newSession = session.createSessionSplit(players);
			newSession.checkIfCompleted();
			if(newSession.getPlayers().size() > 1 || newSession.getGristGutter().gutterMultiplierForSession() > 0)
				this.sessions.add(newSession);
		}
	}
	
	static Set<PlayerIdentifier> collectAllConnectedPlayers(PlayerIdentifier player, SkaianetHandler skaianetHandler)
	{
		Set<PlayerIdentifier> players = new HashSet<>();
		Queue<PlayerIdentifier> uncheckedPlayers = new LinkedList<>();
		
		players.add(player);
		
		for(PlayerIdentifier nextPlayer = player; nextPlayer != null; nextPlayer = uncheckedPlayers.poll())
		{
			findDirectlyConnectedPlayers(nextPlayer, skaianetHandler, connectedPlayer -> {
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
}
