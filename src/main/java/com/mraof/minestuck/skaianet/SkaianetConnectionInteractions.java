package com.mraof.minestuck.skaianet;

import com.mraof.minestuck.MinestuckConfig;
import com.mraof.minestuck.computer.ISburbComputer;
import com.mraof.minestuck.event.SburbEvent;
import com.mraof.minestuck.player.IdentifierHandler;
import com.mraof.minestuck.player.PlayerIdentifier;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.common.MinecraftForge;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;
import java.util.*;
import java.util.stream.Stream;

public final class SkaianetConnectionInteractions
{
	private static final Logger LOGGER = LogManager.getLogger();
	
	public static final String CLOSED = "minestuck.closed_message";
	
	private final SkaianetData skaianetData;
	private final List<ActiveConnection> activeConnections = new ArrayList<>();
	private final Map<PlayerIdentifier, PlayerIdentifier> primaryClientToServerMap = new HashMap<>();
	
	SkaianetConnectionInteractions(SkaianetData skaianetData)
	{
		this.skaianetData = skaianetData;
	}
	
	SkaianetConnectionInteractions(SkaianetData skaianetData, CompoundTag tag)
	{
		this(skaianetData);
		
		ListTag activeConnectionList = tag.getList("connections", Tag.TAG_COMPOUND);
		for(int i = 0; i < activeConnectionList.size(); i++)
			this.activeConnections.add(ActiveConnection.read(activeConnectionList.getCompound(i)));
		
		ListTag primaryConnectionList = tag.getList("primary_connections", Tag.TAG_COMPOUND);
		for(int i = 0; i < primaryConnectionList.size(); i++)
		{
			CompoundTag connectionTag = primaryConnectionList.getCompound(i);
			PlayerIdentifier client = IdentifierHandler.load(connectionTag, "client");
			PlayerIdentifier server = IdentifierHandler.load(connectionTag, "server");
			this.primaryClientToServerMap.put(client, server);
		}
	}
	
	void write(CompoundTag tag)
	{
		ListTag activeConnectionList = new ListTag();
		for(ActiveConnection connection : this.activeConnections)
			activeConnectionList.add(connection.write());
		tag.put("connections", activeConnectionList);
		
		ListTag primaryConnectionList = new ListTag();
		for(Map.Entry<PlayerIdentifier, PlayerIdentifier> entry : this.primaryClientToServerMap.entrySet())
		{
			CompoundTag connectionTag = new CompoundTag();
			entry.getKey().saveToNBT(connectionTag, "client");
			entry.getValue().saveToNBT(connectionTag, "server");
			primaryConnectionList.add(connectionTag);
		}
		tag.put("primary_connections", primaryConnectionList);
	}
	
	public static SkaianetConnectionInteractions get(MinecraftServer mcServer)
	{
		return SkaianetData.get(mcServer).connectionInteractions;
	}
	
	void validate()
	{
		activeConnections().forEach(activeConnection -> {
			
			ISburbComputer cc = activeConnection.clientComputer().getComputer(skaianetData.mcServer),
					sc = activeConnection.serverComputer().getComputer(skaianetData.mcServer);
			if(cc == null || sc == null
					|| activeConnection.clientComputer().isInNether() || activeConnection.serverComputer().isInNether()
					|| !activeConnection.client().equals(cc.getOwner()) || !activeConnection.server().equals(sc.getOwner())
					|| !cc.getClientBoolean("connectedToServer"))
			{
				LOGGER.warn("[SKAIANET] Invalid computer in connection between {} and {}.", activeConnection.client(), activeConnection.server());
				this.closeConnection(activeConnection, cc, sc);
			}
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
		return Optional.ofNullable(primaryClientToServerMap.get(player)).filter(server -> server != IdentifierHandler.NULL_IDENTIFIER);
	}
	
	public boolean hasPrimaryConnectionForServer(PlayerIdentifier player)
	{
		return primaryClientToServerMap.containsValue(player);
	}
	
	public Optional<PlayerIdentifier> primaryPartnerForServer(PlayerIdentifier player)
	{
		return primaryClientToServerMap.entrySet().stream().filter(entry -> player.equals(entry.getValue())).findAny().map(Map.Entry::getKey);
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
				&& skaianetData.sessionHandler.getPlayerSession(client) == skaianetData.sessionHandler.getPlayerSession(server);
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
			
			this.newServerForClient(clientPlayer, serverPlayer);
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
		
		MinecraftForge.EVENT_BUS.post(new SburbEvent.ConnectionCreated(skaianetData.mcServer, activeConnection, type));
	}
	
	void closeConnection(ActiveConnection connection)
	{
		this.closeConnection(connection, null, null);
	}
	
	void closeConnection(ActiveConnection connection, @Nullable ISburbComputer clientComputer, @Nullable ISburbComputer serverComputer)
	{
		if(clientComputer == null)
			clientComputer = connection.clientComputer().getComputer(skaianetData.mcServer);
		if(serverComputer == null)
			serverComputer = connection.serverComputer().getComputer(skaianetData.mcServer);
		
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
		
		MinecraftForge.EVENT_BUS.post(new SburbEvent.ConnectionClosed(skaianetData.mcServer, connection));
	}
	
	public void trySetPrimaryConnection(ActiveConnection connection)
	{
		this.trySetPrimaryConnection(connection.client(), connection.server());
	}
	
	public void trySetPrimaryConnection(PlayerIdentifier client, PlayerIdentifier server)
	{
		Objects.requireNonNull(client);
		Objects.requireNonNull(server);
		
		if(this.hasPrimaryConnectionForClient(client) || this.hasPrimaryConnectionForServer(server))
			throw new IllegalStateException();
		
		Optional<ActiveConnection> activeConnection = this.getActiveConnection(client);
		if(activeConnection.isPresent() && !activeConnection.get().server().equals(server))
			throw new IllegalStateException();
		
		if(activeConnection.isEmpty() && !this.canMakeNewRegularConnectionAsServer(server))
			throw new IllegalStateException();
		
		primaryClientToServerMap.put(client, server);
		
		skaianetData.sessionHandler.onConnect(client, server);
		
		skaianetData.infoTracker.markDirty(client);
		if(server != IdentifierHandler.NULL_IDENTIFIER)
			skaianetData.infoTracker.markDirty(server);
	}
	
	public void unlinkClientPlayer(PlayerIdentifier clientPlayer)
	{
		if(!primaryClientToServerMap.containsKey(clientPlayer))
			throw new IllegalStateException();
		PlayerIdentifier oldServerPlayer = primaryClientToServerMap.get(clientPlayer);
		
		if(oldServerPlayer == IdentifierHandler.NULL_IDENTIFIER)
			return;
		
		this.getActiveConnection(clientPlayer).ifPresent(this::closeConnection);
		primaryClientToServerMap.put(clientPlayer, IdentifierHandler.NULL_IDENTIFIER);
		
		skaianetData.sessionHandler.onDisconnect(clientPlayer, oldServerPlayer);
		
		skaianetData.infoTracker.markDirty(oldServerPlayer);
		if(skaianetData.getOrCreateData(clientPlayer).hasEntered())
			skaianetData.infoTracker.markLandChainDirty();
	}
	
	public void unlinkServerPlayer(PlayerIdentifier serverPlayer)
	{
		this.primaryPartnerForServer(serverPlayer).ifPresent(this::unlinkClientPlayer);
		this.activeConnections().filter(connection -> connection.server().equals(serverPlayer)
						&& !this.hasPrimaryConnectionForClient(connection.client()))
				.forEach(this::closeConnection);
	}
	
	public void newServerForClient(PlayerIdentifier clientPlayer, PlayerIdentifier serverPlayer)
	{
		Objects.requireNonNull(clientPlayer);
		Objects.requireNonNull(serverPlayer);
		
		if(!primaryClientToServerMap.containsKey(clientPlayer))
			throw new IllegalStateException();
		
		if(primaryClientToServerMap.get(clientPlayer) != IdentifierHandler.NULL_IDENTIFIER)
			throw new IllegalStateException("Connection already has a server player");
		
		if(!this.canMakeNewRegularConnectionAsServer(serverPlayer))
			throw new IllegalStateException("Server player already has a connection");
		
		primaryClientToServerMap.put(clientPlayer, serverPlayer);
		
		skaianetData.sessionHandler.onConnect(clientPlayer, serverPlayer);
		
		skaianetData.infoTracker.markDirty(serverPlayer);
		if(skaianetData.getOrCreateData(clientPlayer).hasEntered())
			skaianetData.infoTracker.markLandChainDirty();
	}
	
	/**
	 * Prepares the sburb connection and data needed for after entry.
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
			this.trySetPrimaryConnection(connection.get());
		else
		{
			LOGGER.info("Player {} entered without connection.", player.getUsername());
			this.trySetPrimaryConnection(player, IdentifierHandler.NULL_IDENTIFIER);
		}
	}
}
