package com.mraof.minestuck.skaianet;

import com.mraof.minestuck.MinestuckConfig;
import com.mraof.minestuck.computer.ISburbComputer;
import com.mraof.minestuck.event.SburbEvent;
import com.mraof.minestuck.player.IdentifierHandler;
import com.mraof.minestuck.player.PlayerIdentifier;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.MinecraftForge;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

public final class SkaianetConnectionInteractions
{
	private static final Logger LOGGER = LogManager.getLogger();
	
	public static final String CLOSED = "minestuck.closed_message";
	
	private final SkaianetData skaianetData;
	private final List<ActiveConnection> activeConnections = new ArrayList<>();
	
	SkaianetConnectionInteractions(SkaianetData skaianetData)
	{
		this.skaianetData = skaianetData;
	}
	
	SkaianetConnectionInteractions(SkaianetData skaianetData, CompoundTag tag)
	{
		this(skaianetData);
		
		ListTag connectionList = tag.getList("connections", Tag.TAG_COMPOUND);
		for(int i = 0; i < connectionList.size(); i++)
			activeConnections.add(ActiveConnection.read(connectionList.getCompound(i)));
	}
	
	void write(CompoundTag tag)
	{
		ListTag connectionList = new ListTag();
		for(ActiveConnection connection : this.activeConnections)
			connectionList.add(connection.write());
		tag.put("connections", connectionList);
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
	
	public boolean canMakeNewRegularConnectionAsServer(PlayerIdentifier serverPlayer)
	{
		return !skaianetData.hasPrimaryConnectionForServer(serverPlayer)
				&& this.activeConnections().filter(connection -> connection.server().equals(serverPlayer))
				.allMatch(connection -> skaianetData.getOrCreateData(connection.client()).hasPrimaryConnection());
	}
	
	boolean canMakeSecondaryConnection(PlayerIdentifier client, PlayerIdentifier server)
	{
		return MinestuckConfig.SERVER.allowSecondaryConnections.get()
				&& skaianetData.primaryPartnerForClient(client).isPresent()
				&& skaianetData.sessionHandler.getPlayerSession(client) == skaianetData.sessionHandler.getPlayerSession(server);
	}
	
	boolean tryConnect(ISburbComputer clientComputer, ISburbComputer serverComputer)
	{
		PlayerIdentifier clientPlayer = clientComputer.getOwner(), serverPlayer = serverComputer.getOwner();
		
		if(this.getActiveConnection(clientPlayer).isPresent())
			return false;
		
		SburbPlayerData playerData = skaianetData.getOrCreateData(clientPlayer);
		if(!playerData.hasPrimaryConnection())
		{
			if(!this.canMakeNewRegularConnectionAsServer(serverPlayer))
				return false;
			
			this.newActiveConnection(clientComputer, serverComputer, SburbEvent.ConnectionType.REGULAR);
			return true;
		}
		
		Optional<PlayerIdentifier> primaryServer = playerData.primaryServerPlayer();
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
	
	void trySetPrimaryConnection(ActiveConnection connection)
	{
		this.trySetPrimaryConnection(connection.client(), connection.server());
	}
	
	void trySetPrimaryConnection(PlayerIdentifier client, PlayerIdentifier server)
	{
		if(skaianetData.hasPrimaryConnectionForClient(client) || skaianetData.hasPrimaryConnectionForServer(server))
			throw new IllegalStateException();
		
		Optional<ActiveConnection> activeConnection = this.getActiveConnection(client);
		if(activeConnection.isPresent() && !activeConnection.get().server().equals(server))
			throw new IllegalStateException();
		
		if(activeConnection.isEmpty()
				&& !this.activeConnections().filter(connection -> connection.server().equals(server))
				.allMatch(connection -> skaianetData.getOrCreateData(connection.client()).hasPrimaryConnection()))
			throw new IllegalStateException();
		
		skaianetData.getOrCreateData(client).setHasPrimaryConnection(server);
	}
	
	void unlinkClientPlayer(PlayerIdentifier clientPlayer)
	{
		this.getActiveConnection(clientPlayer).ifPresent(this::closeConnection);
		skaianetData.getOrCreateData(clientPlayer).removeServerPlayer();
	}
	
	void unlinkServerPlayer(PlayerIdentifier serverPlayer)
	{
		skaianetData.primaryPartnerForServer(serverPlayer).ifPresent(this::unlinkClientPlayer);
		this.activeConnections().filter(connection -> connection.server().equals(serverPlayer) && !skaianetData.getOrCreateData(connection.client()).hasPrimaryConnection())
				.forEach(this::closeConnection);
	}
	
	void newServerForClient(PlayerIdentifier clientPlayer, PlayerIdentifier serverPlayer)
	{
		skaianetData.getOrCreateData(clientPlayer).setNewServerPlayer(serverPlayer);
	}
	
	/**
	 * Prepares the sburb connection and data needed for after entry.
	 * Should only be called by the cruxite artifact on trigger before teleportation
	 *
	 * @param target   the identifier of the player that is entering
	 * @return The dimension type of the new land created, or null if the player can't enter at this time.
	 */
	public ResourceKey<Level> prepareEntry(PlayerIdentifier target)
	{
		SburbPlayerData playerData = skaianetData.getOrCreateData(target);
		if(!playerData.hasPrimaryConnection())
		{
			Optional<ActiveConnection> connection = this.getActiveConnection(target);
			if(connection.isPresent())
				this.trySetPrimaryConnection(connection.get());
			else
			{
				LOGGER.info("Player {} entered without connection.", target.getUsername());
				
				this.trySetPrimaryConnection(target, IdentifierHandler.NULL_IDENTIFIER);
			}
		}
		
		if(playerData.getLandDimension() == null)
			SburbHandler.prepareEntry(playerData, skaianetData.mcServer);
		
		return playerData.getLandDimension();
	}
}
