package com.mraof.minestuck.skaianet;

import com.mraof.minestuck.MinestuckConfig;
import com.mraof.minestuck.player.PlayerIdentifier;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.MinecraftServer;

import javax.annotation.Nonnull;
import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * An extension to SkaianetHandler with a focus on sessions
 * @author kirderf1
 */
public sealed abstract class SessionHandler
{
	final SkaianetData skaianetData;
	
	SessionHandler(SkaianetData skaianetData)
	{
		this.skaianetData = skaianetData;
	}
	
	static SessionHandler init(SkaianetData skaianetData)
	{
		if(MinestuckConfig.SERVER.globalSession.get())
			return new Global(skaianetData, new Session(skaianetData));
		else
			return new Multi(skaianetData, Collections.emptyList());
	}
	
	static SessionHandler load(CompoundTag tag, SkaianetData skaianetData)
	{
		
		if(tag.contains("session", Tag.TAG_COMPOUND))
		{
			Session session = Session.read(tag.getCompound("session"), skaianetData);
			
			if(MinestuckConfig.SERVER.globalSession.get())
				return new Global(skaianetData, session);
			else
				return new Multi(skaianetData, createSplitSessions(session));
		} else
		{
			ListTag sessionsTag = tag.getList("sessions", Tag.TAG_COMPOUND);
			List<Session> sessions = new ArrayList<>();
			for(int i = 0; i < sessionsTag.size(); i++)
				sessions.add(Session.read(sessionsTag.getCompound(i), skaianetData));
			
			if(MinestuckConfig.SERVER.globalSession.get())
				return new Global(skaianetData, Session.createMergedSession(sessions, skaianetData));
			else
				return new Multi(skaianetData, sessions);
		}
	}
	
	abstract void write(CompoundTag compound);
	
	/**
	 * Looks for the session that the player is a part of.
	 * @param player A string of the player's username.
	 * @return A session that contains at least one connection, that the player is a part of.
	 */
	public abstract Session getPlayerSession(PlayerIdentifier player);
	
	public abstract Set<Session> getSessions();
	
	boolean doesSessionHaveMaxTier(Session session)
	{
		return session.completed;
	}
	
	abstract void onConnect(PlayerIdentifier client, PlayerIdentifier server);
	
	abstract void onDisconnect(PlayerIdentifier client, PlayerIdentifier server);
	
	void newPredefineData(PlayerIdentifier player)
	{}
	
	Map<Integer, String> getServerList(PlayerIdentifier client)
	{
		Optional<PlayerIdentifier> primaryServer = skaianetData.connections.primaryPartnerForClient(client);
		Map<Integer, String> map = new HashMap<>();
		for(PlayerIdentifier server : skaianetData.computerInteractions.openServerPlayers())
		{
			
			if(primaryServer.isPresent() && primaryServer.get().equals(server)
					|| primaryServer.isEmpty() && skaianetData.connections.canMakeNewRegularConnectionAsServer(server)
					|| skaianetData.connections.canMakeSecondaryConnection(client, server))
				map.put(server.getId(), server.getUsername());
		}
		return map;
	}
	
	/**
	 * @return all players whose title or land types (predefined or not) should influence the selection of the same for the specified player.
	 */
	Stream<PlayerIdentifier> playersToCheckForDataSelection(PlayerIdentifier player)
	{
		Session session = this.getPlayerSession(player);
		return session != null
				? session.getPlayers().stream().filter(otherPlayer -> !otherPlayer.equals(player))
				: Stream.empty();
	}
	
	public static SessionHandler get(MinecraftServer server)
	{
		return SkaianetData.get(server).sessionHandler;
	}
	
	/**
	 * An implementation that only allows for a single, global session, which all players are assumed to be part of
	 */
	private static final class Global extends SessionHandler
	{
		@Nonnull
		private final Session globalSession;
		
		Global(SkaianetData skaianetData, Session session)
		{
			super(skaianetData);
			globalSession = Objects.requireNonNull(session);
			skaianetData.players().forEach(globalSession::addPlayer);
		}
		
		@Override
		void write(CompoundTag compound)
		{
			compound.put("session", globalSession.write());
		}
		
		@Override
		public Session getPlayerSession(PlayerIdentifier player)
		{
			return globalSession;
		}
		
		@Override
		public Set<Session> getSessions()
		{
			return Collections.singleton(globalSession);
		}
		
		@Override
		boolean doesSessionHaveMaxTier(Session session)
		{
			return false;
		}
		
		@Override
		void onConnect(PlayerIdentifier client, PlayerIdentifier server)
		{
			globalSession.addPlayer(client).addPlayer(server);
		}
		
		@Override
		void onDisconnect(PlayerIdentifier client, PlayerIdentifier server)
		{}
		
		@Override
		void newPredefineData(PlayerIdentifier player)
		{
			globalSession.addPlayer(player);
		}
	}
	
	/**
	 * The standard implementation of SessionHandler which allows multiple sessions
	 */
	private static final class Multi extends SessionHandler
	{
		/**
		 * An array list of the current worlds sessions.
		 */
		private final Set<Session> sessions = new HashSet<>();
		
		Multi(SkaianetData skaianetData, List<Session> sessions)
		{
			super(skaianetData);
			this.sessions.addAll(sessions);
			this.sessions.forEach(Session::checkIfCompleted);
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
				Session newSession = Session.createMergedSession(sessions, this.skaianetData);
				this.sessions.add(newSession);
				return newSession;
			} else if(sessions.size() == 1)
			{
				return sessions.stream().findAny().get();
			} else
			{
				Session session = new Session(skaianetData);
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
			
			if(!skaianetData.connections.isPrimaryPair(client, server))
			{
				this.sessions.remove(s);
				this.sessions.addAll(createSplitSessions(s));
			}
		}
	}
	
	private static List<Session> createSplitSessions(Session session)
	{
		List<Session> newSessions = new ArrayList<>();
		while(!session.getPlayers().isEmpty())
		{
			PlayerIdentifier remainingPlayer = session.getPlayers().iterator().next();
			Set<PlayerIdentifier> players = collectAllConnectedPlayers(remainingPlayer, session.skaianetData);
			Session newSession = session.createSessionSplit(players);
			newSession.checkIfCompleted();
			if(newSession.getPlayers().size() > 1 || newSession.getGristGutter().gutterMultiplierForSession() > 0)
				newSessions.add(newSession);
		}
		return newSessions;
	}
	
	private static Set<PlayerIdentifier> collectAllConnectedPlayers(PlayerIdentifier player, SkaianetData skaianetData)
	{
		Set<PlayerIdentifier> players = new HashSet<>();
		Queue<PlayerIdentifier> uncheckedPlayers = new LinkedList<>();
		
		players.add(player);
		
		for(PlayerIdentifier nextPlayer = player; nextPlayer != null; nextPlayer = uncheckedPlayers.poll())
		{
			findDirectlyConnectedPlayers(nextPlayer, skaianetData, connectedPlayer -> {
				if(players.add(connectedPlayer))
					uncheckedPlayers.add(connectedPlayer);
			});
		}
		
		return players;
	}
	
	private static void findDirectlyConnectedPlayers(PlayerIdentifier player, SkaianetData skaianetData, Consumer<PlayerIdentifier> playerConsumer)
	{
		skaianetData.connections.primaryPartnerForClient(player).ifPresent(playerConsumer);
		skaianetData.connections.primaryPartnerForServer(player).ifPresent(playerConsumer);
		
		skaianetData.connections.getActiveConnection(player).ifPresent(connection -> playerConsumer.accept(connection.server()));
		skaianetData.connections.activeConnections().filter(connection -> connection.server().equals(player)).forEach(connection -> playerConsumer.accept(connection.client()));
	}
}
