package com.mraof.minestuck.skaianet;

import com.mraof.minestuck.MinestuckConfig;
import com.mraof.minestuck.computer.ISburbComputer;
import com.mraof.minestuck.event.SburbEvent;
import com.mraof.minestuck.player.IdentifierHandler;
import com.mraof.minestuck.player.PlayerIdentifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.MinecraftForge;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;
import java.util.Objects;
import java.util.Optional;

public final class SkaianetConnectionInteractions
{
	private static final Logger LOGGER = LogManager.getLogger();
	
	public static final String CLOSED = "minestuck.closed_message";
	
	public static boolean canMakeNewRegularConnectionAsServer(PlayerIdentifier serverPlayer, SkaianetData skaianetData)
	{
		return !skaianetData.hasPrimaryConnectionForServer(serverPlayer)
				&& skaianetData.activeConnections().filter(connection -> connection.server().equals(serverPlayer))
				.allMatch(connection -> skaianetData.getOrCreateData(connection.client()).hasPrimaryConnection());
	}
	
	static boolean canMakeSecondaryConnection(PlayerIdentifier client, PlayerIdentifier server, SkaianetData skaianetData)
	{
		return MinestuckConfig.SERVER.allowSecondaryConnections.get()
				&& skaianetData.primaryPartnerForClient(client).isPresent()
				&& skaianetData.sessionHandler.getPlayerSession(client) == skaianetData.sessionHandler.getPlayerSession(server);
	}
	
	static boolean tryConnect(ISburbComputer clientComputer, ISburbComputer serverComputer, SkaianetData skaianetData)
	{
		PlayerIdentifier clientPlayer = clientComputer.getOwner(), serverPlayer = serverComputer.getOwner();
		
		if(skaianetData.getActiveConnection(clientPlayer).isPresent())
			return false;
		
		SburbPlayerData playerData = skaianetData.getOrCreateData(clientPlayer);
		if(!playerData.hasPrimaryConnection())
		{
			if(!canMakeNewRegularConnectionAsServer(serverPlayer, skaianetData))
				return false;
			
			newActiveConnection(clientComputer, serverComputer, SburbEvent.ConnectionType.REGULAR, skaianetData);
			return true;
		}
		
		Optional<PlayerIdentifier> primaryServer = playerData.primaryServerPlayer();
		if(primaryServer.isEmpty())
		{
			if(!canMakeNewRegularConnectionAsServer(serverPlayer, skaianetData))
				return false;
			
			newServerForClient(clientPlayer, serverPlayer, skaianetData);
			newActiveConnection(clientComputer, serverComputer, SburbEvent.ConnectionType.NEW_SERVER, skaianetData);
			return true;
		}
		
		if(primaryServer.get().equals(serverPlayer))
		{
			newActiveConnection(clientComputer, serverComputer, SburbEvent.ConnectionType.RESUME, skaianetData);
			return true;
		}
		if(canMakeSecondaryConnection(clientPlayer, serverPlayer, skaianetData))
		{
			newActiveConnection(clientComputer, serverComputer, SburbEvent.ConnectionType.SECONDARY, skaianetData);
			return true;
		}
		return false;
	}
	
	private static void newActiveConnection(ISburbComputer client, ISburbComputer server,
											SburbEvent.ConnectionType type, SkaianetData skaianetData)
	{
		Objects.requireNonNull(client);
		Objects.requireNonNull(server);
		
		if(skaianetData.getActiveConnection(client.getOwner()).isPresent())
			throw new IllegalStateException("Should not activate sburb connection when already active");
		
		ActiveConnection activeConnection = new ActiveConnection(client, server);
		skaianetData.activeConnections.add(activeConnection);
		skaianetData.sessionHandler.onConnect(activeConnection.client(), activeConnection.server());
		skaianetData.infoTracker.markDirty(activeConnection);
		
		client.connected(server.getOwner(), true);
		server.connected(client.getOwner(), false);
		
		MinecraftForge.EVENT_BUS.post(new SburbEvent.ConnectionCreated(skaianetData.mcServer, activeConnection, type));
	}
	
	static void closeConnection(ActiveConnection connection, SkaianetData skaianetData)
	{
		closeConnection(connection, null, null, skaianetData);
	}
	
	static void closeConnection(ActiveConnection connection, @Nullable ISburbComputer clientComputer, @Nullable ISburbComputer serverComputer, SkaianetData skaianetData)
	{
		if(clientComputer == null)
			clientComputer = connection.clientComputer().getComputer(skaianetData.mcServer);
		if(serverComputer == null)
			serverComputer = connection.serverComputer().getComputer(skaianetData.mcServer);
		
		skaianetData.activeConnections.remove(connection);
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
	
	static void trySetPrimaryConnection(ActiveConnection connection, SkaianetData skaianetData)
	{
		trySetPrimaryConnection(connection.client(), connection.server(), skaianetData);
	}
	
	static void trySetPrimaryConnection(PlayerIdentifier client, PlayerIdentifier server, SkaianetData skaianetData)
	{
		if(skaianetData.hasPrimaryConnectionForClient(client) || skaianetData.hasPrimaryConnectionForServer(server))
			throw new IllegalStateException();
		
		Optional<ActiveConnection> activeConnection = skaianetData.getActiveConnection(client);
		if(activeConnection.isPresent() && !activeConnection.get().server().equals(server))
			throw new IllegalStateException();
		
		if(activeConnection.isEmpty()
				&& !skaianetData.activeConnections().filter(connection -> connection.server().equals(server))
				.allMatch(connection -> skaianetData.getOrCreateData(connection.client()).hasPrimaryConnection()))
			throw new IllegalStateException();
		
		skaianetData.getOrCreateData(client).setHasPrimaryConnection(server);
	}
	
	static void unlinkClientPlayer(PlayerIdentifier clientPlayer, SkaianetData skaianetData)
	{
		skaianetData.getActiveConnection(clientPlayer).ifPresent(activeConnection -> closeConnection(activeConnection, skaianetData));
		skaianetData.getOrCreateData(clientPlayer).removeServerPlayer();
	}
	
	static void unlinkServerPlayer(PlayerIdentifier serverPlayer, SkaianetData skaianetData)
	{
		skaianetData.primaryPartnerForServer(serverPlayer).ifPresent(clientPlayer -> unlinkClientPlayer(clientPlayer, skaianetData));
		skaianetData.activeConnections().filter(connection -> connection.server().equals(serverPlayer) && !skaianetData.getOrCreateData(connection.client()).hasPrimaryConnection())
				.forEach(activeConnection -> closeConnection(activeConnection, skaianetData));
	}
	
	static void newServerForClient(PlayerIdentifier clientPlayer, PlayerIdentifier serverPlayer, SkaianetData skaianetData)
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
	public static ResourceKey<Level> prepareEntry(PlayerIdentifier target, SkaianetData skaianetData)
	{
		SburbPlayerData playerData = skaianetData.getOrCreateData(target);
		if(!playerData.hasPrimaryConnection())
		{
			Optional<ActiveConnection> connection = skaianetData.getActiveConnection(target);
			if(connection.isPresent())
				trySetPrimaryConnection(connection.get(), skaianetData);
			else
			{
				LOGGER.info("Player {} entered without connection.", target.getUsername());
				
				trySetPrimaryConnection(target, IdentifierHandler.NULL_IDENTIFIER, skaianetData);
			}
		}
		
		if(playerData.getLandDimension() == null)
			SburbHandler.prepareEntry(playerData, skaianetData.mcServer);
		
		return playerData.getLandDimension();
	}
}
