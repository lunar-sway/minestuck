package com.mraof.minestuck.skaianet;

import com.google.common.collect.AbstractIterator;
import com.mojang.serialization.DataResult;
import com.mraof.minestuck.MinestuckConfig;
import com.mraof.minestuck.computer.ComputerReference;
import com.mraof.minestuck.computer.ISburbComputer;
import com.mraof.minestuck.event.SburbEvent;
import com.mraof.minestuck.player.IdentifierHandler;
import com.mraof.minestuck.player.PlayerIdentifier;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.server.MinecraftServer;
import net.neoforged.neoforge.common.NeoForge;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

/**
 * Handles active connections and primary connections.
 * Sets the rules for when a player can connect to another player.
 * @author kirderf1
 */
public final class SburbConnections
{
	private static final Logger LOGGER = LogManager.getLogger();
	
	public static final String CLOSED = "minestuck.closed_message";
	
	private final SkaianetData skaianetData;
	private final List<ActiveConnection> activeConnections = new ArrayList<>();
	private final Map<PlayerIdentifier, Optional<PlayerIdentifier>> primaryClientToServerMap = new HashMap<>();
	
	SburbConnections(SkaianetData skaianetData)
	{
		this.skaianetData = skaianetData;
	}
	
	SburbConnections(SkaianetData skaianetData, CompoundTag tag)
	{
		this(skaianetData);
		
		ListTag activeConnectionList = tag.getList("connections", Tag.TAG_COMPOUND);
		for(int i = 0; i < activeConnectionList.size(); i++)
		{
			ActiveConnection.read(activeConnectionList.getCompound(i))
					.resultOrPartial(LOGGER::error).ifPresent(this.activeConnections::add);
		}
		
		ListTag primaryConnectionList = tag.getList("primary_connections", Tag.TAG_COMPOUND);
		for(int i = 0; i < primaryConnectionList.size(); i++)
		{
			CompoundTag connectionTag = primaryConnectionList.getCompound(i);
			
			Optional<PlayerIdentifier> client = IdentifierHandler.load(connectionTag, "client").resultOrPartial(LOGGER::error);
			
			if(client.isEmpty())
			{
				LOGGER.error("Unable to load client id for primary connection from data: {}", connectionTag);
				continue;
			}
			
			Optional<PlayerIdentifier> server = IdentifierHandler.loadOptional(connectionTag, "server")
					.resultOrPartial(LOGGER::error).flatMap(Function.identity());
			this.primaryClientToServerMap.put(client.get(), server);
		}
	}
	
	void write(CompoundTag tag)
	{
		ListTag activeConnectionList = new ListTag();
		for(ActiveConnection connection : this.activeConnections)
			activeConnectionList.add(connection.write());
		tag.put("connections", activeConnectionList);
		
		ListTag primaryConnectionList = new ListTag();
		for(Map.Entry<PlayerIdentifier, Optional<PlayerIdentifier>> entry : this.primaryClientToServerMap.entrySet())
		{
			CompoundTag connectionTag = new CompoundTag();
			entry.getKey().saveToNBT(connectionTag, "client");
			entry.getValue().ifPresent(server -> server.saveToNBT(connectionTag, "server"));
			primaryConnectionList.add(connectionTag);
		}
		tag.put("primary_connections", primaryConnectionList);
	}
	
	void readOldConnectionData(CompoundTag connectionTag, Consumer<PlayerIdentifier> playerConsumer)
	{
		boolean isMain = connectionTag.getBoolean("IsMain");
		boolean active = !isMain || connectionTag.getBoolean("IsActive");
		
		Optional<PlayerIdentifier> client = IdentifierHandler.load(connectionTag, "client").resultOrPartial(LOGGER::error);
		client.ifPresent(playerConsumer);
		if(client.isEmpty())
		{
			LOGGER.error("Unable to load client id for old connection from data: {}", connectionTag);
			return;
		}
		
		Optional<PlayerIdentifier> server = IdentifierHandler.loadNullable(connectionTag, "server")
				.resultOrPartial(LOGGER::error).flatMap(Function.identity());
		server.ifPresent(playerConsumer);
		
		if(active && server.isPresent())
		{
			DataResult.unbox(DataResult.instance().apply2(
					(clientComputer, serverComputer) -> new ActiveConnection(client.get(), clientComputer, server.get(), serverComputer),
					ComputerReference.CODEC.parse(NbtOps.INSTANCE, connectionTag.getCompound("client_computer")),
					ComputerReference.CODEC.parse(NbtOps.INSTANCE, connectionTag.getCompound("server_computer")))
			).resultOrPartial(LOGGER::error).ifPresent(activeConnections::add);
		}
		if(isMain)
			this.primaryClientToServerMap.put(client.get(), server);
		
		LOGGER.debug("Parsed old connection between {} and {}", client.get(), server);
	}
	
	public static SburbConnections get(MinecraftServer mcServer)
	{
		return SkaianetData.get(mcServer).connections;
	}
	
	Optional<ISburbComputer> clientComputerIfValid(ActiveConnection connection)
	{
		return Optional.ofNullable(connection.clientComputer().getComputer(skaianetData.mcServer))
				.filter(computer -> connection.client().equals(computer.getOwner()) && computer.getClientBoolean("connectedToServer"));
	}
	
	Optional<ISburbComputer> serverComputerIfValid(ActiveConnection connection)
	{
		return Optional.ofNullable(connection.serverComputer().getComputer(skaianetData.mcServer))
				.filter(computer -> connection.server().equals(computer.getOwner()));
	}
	
	public Optional<ActiveConnection> getCheckedActiveConnection(PlayerIdentifier client)
	{
		return getActiveConnection(client).flatMap(connection -> {
			Optional<ISburbComputer> cc = clientComputerIfValid(connection),
					sc = serverComputerIfValid(connection);
			if(cc.isEmpty() || sc.isEmpty())
			{
				this.closeConnection(connection, cc.orElse(null), sc.orElse(null));
				return Optional.empty();
			}
			
			return Optional.of(connection);
		});
	}
	
	public Optional<ActiveConnection> getActiveConnection(PlayerIdentifier client)
	{
		return activeConnections().filter(c -> c.client().equals(client)).findAny();
	}
	
	public Optional<ActiveConnection> getServerConnection(ISburbComputer computer)
	{
		return activeConnections().filter(c -> c.isServer(computer)).findAny();
	}
	
	public Optional<ActiveConnection> getClientConnection(ISburbComputer computer)
	{
		return activeConnections().filter(c -> c.isClient(computer)).findAny();
	}
	
	public Stream<ActiveConnection> activeConnections()
	{
		return activeConnections.stream();
	}
	
	public boolean hasPrimaryConnectionForClient(PlayerIdentifier player)
	{
		return primaryClientToServerMap.containsKey(player);
	}
	
	public Optional<PlayerIdentifier> primaryPartnerForClient(PlayerIdentifier player)
	{
		return Objects.requireNonNullElse(primaryClientToServerMap.get(player), Optional.empty());
	}
	
	public boolean hasPrimaryConnectionForServer(PlayerIdentifier player)
	{
		return primaryClientToServerMap.containsValue(Optional.of(player));
	}
	
	public Optional<PlayerIdentifier> primaryPartnerForServer(PlayerIdentifier player)
	{
		return primaryClientToServerMap.entrySet().stream().filter(entry -> Optional.of(player).equals(entry.getValue())).findAny().map(Map.Entry::getKey);
	}
	
	public boolean isPrimaryPair(PlayerIdentifier client, PlayerIdentifier server)
	{
		return primaryPartnerForClient(client).map(server::equals).orElse(false);
	}
	
	public boolean canMakeNewRegularConnectionAsServer(PlayerIdentifier serverPlayer)
	{
		return !this.hasPrimaryConnectionForServer(serverPlayer)
				&& this.activeConnections().filter(connection -> connection.server().equals(serverPlayer))
				.allMatch(connection -> this.hasPrimaryConnectionForClient(connection.client()));
	}
	
	boolean canMakeSecondaryConnection(PlayerIdentifier client, PlayerIdentifier server)
	{
		return MinestuckConfig.SERVER.allowSecondaryConnections.get()
				&& this.primaryPartnerForClient(client).isPresent()
				&& skaianetData.sessionHandler.isInSameSession(client, server);
	}
	
	boolean tryConnect(ISburbComputer clientComputer, ISburbComputer serverComputer)
	{
		PlayerIdentifier clientPlayer = clientComputer.getOwner(), serverPlayer = serverComputer.getOwner();
		
		if(this.getActiveConnection(clientPlayer).isPresent())
			return false;
		
		if(!this.hasPrimaryConnectionForClient(clientPlayer))
		{
			if(!this.canMakeNewRegularConnectionAsServer(serverPlayer))
				return false;
			
			this.newActiveConnection(clientComputer, serverComputer, SburbEvent.ConnectionType.REGULAR);
			return true;
		}
		
		Optional<PlayerIdentifier> primaryServer = this.primaryPartnerForClient(clientPlayer);
		if(primaryServer.isEmpty())
		{
			if(!this.canMakeNewRegularConnectionAsServer(serverPlayer))
				return false;
			
			this.setPrimaryConnection(clientPlayer, serverPlayer);
			this.newActiveConnection(clientComputer, serverComputer, SburbEvent.ConnectionType.NEW_SERVER);
			return true;
		}
		
		if(primaryServer.get().equals(serverPlayer))
		{
			this.newActiveConnection(clientComputer, serverComputer, SburbEvent.ConnectionType.RESUME);
			return true;
		}
		if(this.canMakeSecondaryConnection(clientPlayer, serverPlayer))
		{
			this.newActiveConnection(clientComputer, serverComputer, SburbEvent.ConnectionType.SECONDARY);
			return true;
		}
		return false;
	}
	
	private void newActiveConnection(ISburbComputer client, ISburbComputer server,
									 SburbEvent.ConnectionType type)
	{
		Objects.requireNonNull(client);
		Objects.requireNonNull(server);
		
		if(this.getActiveConnection(client.getOwner()).isPresent())
			throw new IllegalStateException("Should not activate sburb connection when already active");
		
		ActiveConnection activeConnection = new ActiveConnection(client, server);
		this.activeConnections.add(activeConnection);
		skaianetData.sessionHandler.onConnect(activeConnection.client(), activeConnection.server());
		skaianetData.infoTracker.markDirty(activeConnection);
		
		client.connected(server.getOwner(), true);
		server.connected(client.getOwner(), false);
		
		NeoForge.EVENT_BUS.post(new SburbEvent.ConnectionCreated(skaianetData.mcServer, activeConnection, type));
	}
	
	private void closeConnectionIf(Predicate<ActiveConnection> predicate)
	{
		this.activeConnections().filter(predicate).toList().forEach(this::closeConnection);
	}
	
	public void closeConnection(ActiveConnection connection)
	{
		this.closeConnection(connection, null, null);
	}
	
	void closeConnection(ActiveConnection connection, @Nullable ISburbComputer clientComputer, @Nullable ISburbComputer serverComputer)
	{
		if(clientComputer == null)
			clientComputer = clientComputerIfValid(connection).orElse(null);
		if(serverComputer == null)
			serverComputer = serverComputerIfValid(connection).orElse(null);
		
		this.activeConnections.remove(connection);
		skaianetData.sessionHandler.onDisconnect(connection.client(), connection.server());
		skaianetData.infoTracker.markDirty(connection);
		
		if(clientComputer != null)
		{
			clientComputer.putClientBoolean("connectedToServer", false);
			clientComputer.putClientMessage(CLOSED);
		}
		if(serverComputer != null)
		{
			serverComputer.clearConnectedClient();
			serverComputer.putServerMessage(CLOSED);
		}
		
		NeoForge.EVENT_BUS.post(new SburbEvent.ConnectionClosed(skaianetData.mcServer, connection));
	}
	
	public void setPrimaryConnection(ActiveConnection connection)
	{
		this.setPrimaryConnection(connection.client(), connection.server());
	}
	
	/**
	 * Makes sure that the given client and server players has a primary connection.
	 * It may close any incompatible active connections.
	 * This function will avoid replacing any existing primary partner,
	 * throwing an exception if either player already has a different primary partner.
	 * If you want to replace an existing primary partner,
	 * you should first call {@link #unlinkClientPlayer(PlayerIdentifier)} or {@link #unlinkServerPlayer(PlayerIdentifier)}.
	 */
	public void setPrimaryConnection(PlayerIdentifier client, PlayerIdentifier server)
	{
		Objects.requireNonNull(client);
		Objects.requireNonNull(server);
		
		if(this.isPrimaryPair(client, server))
			return;
		
		if(this.primaryPartnerForClient(client).isPresent())
			throw new IllegalStateException("Client player " + client.getUsername() + " already has a primary partner");
		
		if(this.hasPrimaryConnectionForServer(server))
			throw new IllegalStateException("Server player " + server.getUsername() + " already has a primary partner");
		
		this.closeConnectionIf(connection -> !connection.client().equals(client) && connection.server().equals(server)
						&& !this.hasPrimaryConnectionForClient(connection.client()));
		
		LOGGER.debug("Connecting players {} and {}", client, server);
		primaryClientToServerMap.put(client, Optional.of(server));
		
		skaianetData.sessionHandler.onConnect(client, server);
		
		skaianetData.infoTracker.markDirty(client);
		skaianetData.infoTracker.markDirty(server);
		if(skaianetData.getOrCreateData(client).hasEntered())
			skaianetData.infoTracker.markLandChainDirty();
	}
	
	public void unlinkClientPlayer(PlayerIdentifier clientPlayer)
	{
		this.getActiveConnection(clientPlayer).ifPresent(this::closeConnection);
		
		if(!primaryClientToServerMap.containsKey(clientPlayer))
			return;
		Optional<PlayerIdentifier> oldServerPlayer = primaryClientToServerMap.get(clientPlayer);
		if(oldServerPlayer.isEmpty())
			return;
		
		LOGGER.debug("Disconnecting server {} from client {}", oldServerPlayer.get(), clientPlayer);
		primaryClientToServerMap.put(clientPlayer, Optional.empty());
		
		skaianetData.sessionHandler.onDisconnect(clientPlayer, oldServerPlayer.get());
		
		skaianetData.infoTracker.markDirty(oldServerPlayer.get());
		if(skaianetData.getOrCreateData(clientPlayer).hasEntered())
			skaianetData.infoTracker.markLandChainDirty();
	}
	
	public void unlinkServerPlayer(PlayerIdentifier serverPlayer)
	{
		this.primaryPartnerForServer(serverPlayer).ifPresent(this::unlinkClientPlayer);
		this.closeConnectionIf(connection -> connection.server().equals(serverPlayer)
						&& !this.hasPrimaryConnectionForClient(connection.client()));
	}
	
	/**
	 * Prepares the primary connection needed for after entry.
	 * Should only be called by the cruxite artifact on trigger before teleportation
	 *
	 * @param player   the identifier of the player that is entering
	 */
	public void setPrimaryConnectionForEntry(PlayerIdentifier player)
	{
		if(this.hasPrimaryConnectionForClient(player))
			return;
		
		Optional<ActiveConnection> connection = this.getActiveConnection(player);
		if(connection.isPresent())
			this.setPrimaryConnection(connection.get());
		else
		{
			LOGGER.info("Player {} entered without connection.", player.getUsername());
			
			primaryClientToServerMap.put(player, Optional.empty());
			skaianetData.infoTracker.markDirty(player);
		}
	}
	
	public Iterable<PlayerIdentifier> iterateClientPartners(PlayerIdentifier player)
	{
		return () -> new AbstractIterator<>()
		{
			private PlayerIdentifier serverPlayer = player;
			@Nullable
			@Override
			protected PlayerIdentifier computeNext()
			{
				Optional<PlayerIdentifier> clientPlayer = SburbConnections.this.primaryPartnerForServer(serverPlayer);
				if(clientPlayer.isEmpty())
					return endOfData();
				
				serverPlayer = clientPlayer.get();
				return clientPlayer.get();
			}
		};
	}
	
	public Iterable<PlayerIdentifier> iterateServerPartners(PlayerIdentifier player)
	{
		return () -> new AbstractIterator<>()
		{
			private PlayerIdentifier clientPlayer = player;
			@Nullable
			@Override
			protected PlayerIdentifier computeNext()
			{
				Optional<PlayerIdentifier> serverPlayer = SburbConnections.this.primaryPartnerForClient(clientPlayer);
				if(serverPlayer.isEmpty())
					return endOfData();
				
				clientPlayer = serverPlayer.get();
				return serverPlayer.get();
			}
		};
	}
}
