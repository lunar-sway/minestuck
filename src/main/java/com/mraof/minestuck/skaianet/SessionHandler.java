package com.mraof.minestuck.skaianet;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.MinestuckConfig;
import com.mraof.minestuck.player.IdentifierHandler;
import com.mraof.minestuck.player.PlayerIdentifier;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;

import javax.annotation.Nonnull;
import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Stream;

/**
 * Object that manages access to sessions.
 * It has two distinct implementations that are used based on the "globalSession" config option.
 * One maintains a "global session" that gets used for all players,
 * while the other dynamically creates, merges and splits up multiple sessions as players connect and disconnect to each other.
 * @author kirderf1
 */
@Mod.EventBusSubscriber(modid = Minestuck.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public sealed abstract class SessionHandler
{
	final SkaianetData skaianetData;
	
	SessionHandler(SkaianetData skaianetData)
	{
		this.skaianetData = skaianetData;
	}
	
	static SessionHandler init(boolean globalSession, SkaianetData skaianetData)
	{
		if(globalSession)
			return new Global(skaianetData, new Session(skaianetData));
		else
			return new Multi(skaianetData, Collections.emptyList());
	}
	
	static SessionHandler load(CompoundTag tag, boolean globalSession, SkaianetData skaianetData)
	{
		
		if(tag.contains("session", Tag.TAG_COMPOUND))
		{
			Session session = Session.read(tag.getCompound("session"), skaianetData);
			
			if(globalSession)
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
				return new Global(skaianetData, sessions.stream().reduce(new Session(skaianetData), Session::merge));
			else
				return new Multi(skaianetData, sessions);
		}
	}
	
	abstract void write(CompoundTag compound);
	
	/**
	 * Looks for the session that the player is a part of, and returns it within an optional if present.
	 * This function should not have any side effects.
	 * While a session should only be absent if the player is not connected to any other player,
	 * a session is not necessarily absent because the player is not connected to any other player.
	 * @see SessionHandler#getOrCreateSession(PlayerIdentifier)
	 */
	public abstract Optional<Session> getSession(PlayerIdentifier player);
	
	/**
	 * Looks for the session that the player is a part of, creates one if there is none, and returns it.
	 * This function may create and add a new session as a side effect.
	 * @see SessionHandler#getSession(PlayerIdentifier)
	 */
	public abstract Session getOrCreateSession(PlayerIdentifier player);
	
	public boolean isInSameSession(PlayerIdentifier player1, PlayerIdentifier player2)
	{
		Optional<Session> session = this.getSession(player1);
		return session.isPresent() && session.get().containsPlayer(player2);
	}
	
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
		return this.getSession(player).stream()
				.flatMap(session -> session.getPlayers().stream().filter(otherPlayer -> !otherPlayer.equals(player)));
	}
	
	public static SessionHandler get(MinecraftServer server)
	{
		return SkaianetData.get(server).sessionHandler;
	}
	
	/**
	 * A dirty fix to make sure that a player is in the global session. Should eventually be replaced with a better solution.
	 */
	@SubscribeEvent
	public static void onPlayerLogIn(PlayerEvent.PlayerLoggedInEvent event)
	{
		if(!(event.getEntity() instanceof ServerPlayer player))
			return;
		PlayerIdentifier playerId = IdentifierHandler.encode(player);
		if(playerId != null && SessionHandler.get(player.server) instanceof Global global)
			global.onNewPlayer(playerId);
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
			
			// Complements what onNewPlayer() does, but for all players and at server-start only.
			skaianetData.players().forEach(globalSession::addPlayer);
		}
		
		void onNewPlayer(PlayerIdentifier player)
		{
			globalSession.addPlayer(player);
		}
		
		@Override
		void write(CompoundTag compound)
		{
			compound.put("session", globalSession.write());
		}
		
		@Override
		public Optional<Session> getSession(PlayerIdentifier player)
		{
			return Optional.of(globalSession);
		}
		
		@Override
		public Session getOrCreateSession(PlayerIdentifier player)
		{
			return globalSession;
		}
		
		@Override
		public boolean isInSameSession(PlayerIdentifier player1, PlayerIdentifier player2)
		{
			return true;
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
		public Optional<Session> getSession(PlayerIdentifier player)
		{
			return sessions.stream().filter(s -> s.containsPlayer(player)).findAny();
		}
		
		@Override
		public Session getOrCreateSession(PlayerIdentifier player)
		{
			Optional<Session> session = this.getSession(player);
			
			if(session.isPresent())
				return session.get();
			
			Session newSession = new Session(skaianetData).addPlayer(player);
			this.sessions.add(newSession);
			return newSession;
		}
		
		@Override
		public Set<Session> getSessions()
		{
			return Collections.unmodifiableSet(sessions);
		}
		
		@Override
		void onConnect(PlayerIdentifier client, PlayerIdentifier server)
		{
			Session clientSession = this.getOrCreateSession(client), serverSession = this.getOrCreateSession(server);
			if(clientSession == serverSession)
				return;
			
			if(clientSession.getPlayers().size() < serverSession.getPlayers().size())
			{
				this.sessions.remove(clientSession);
				serverSession.merge(clientSession);
			} else
			{
				this.sessions.remove(serverSession);
				clientSession.merge(serverSession);
			}
		}
		
		@Override
		void onDisconnect(PlayerIdentifier client, PlayerIdentifier server)
		{
			Optional<Session> s = this.getSession(client);
			
			if(s.isPresent() && s.get().containsPlayer(server) && !skaianetData.connections.isPrimaryPair(client, server))
			{
				this.sessions.remove(s.get());
				this.sessions.addAll(createSplitSessions(s.get()));
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
			
			if(players.size() == session.getPlayers().size())
			{
				newSessions.add(session);
				break;
			}
			
			Session newSession = session.createSessionSplit(players);
			newSession.checkIfCompleted();
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
