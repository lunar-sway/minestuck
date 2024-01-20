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
	
	public static boolean canMakeNewRegularConnectionAsServer(PlayerIdentifier serverPlayer, SkaianetHandler skaianet)
	{
		return !skaianet.hasPrimaryConnectionForServer(serverPlayer)
				&& skaianet.activeConnections().filter(connection -> connection.server().equals(serverPlayer))
				.allMatch(connection -> skaianet.getOrCreateData(connection.client()).hasPrimaryConnection());
	}
	
	static boolean canMakeSecondaryConnection(PlayerIdentifier client, PlayerIdentifier server, SkaianetHandler skaianet)
	{
		return MinestuckConfig.SERVER.allowSecondaryConnections.get()
				&& skaianet.primaryPartnerForClient(client).isPresent()
				&& skaianet.sessionHandler.getPlayerSession(client) == skaianet.sessionHandler.getPlayerSession(server);
	}
	
	static boolean tryConnect(ISburbComputer clientComputer, ISburbComputer serverComputer, SkaianetHandler skaianet)
	{
		PlayerIdentifier clientPlayer = clientComputer.getOwner(), serverPlayer = serverComputer.getOwner();
		
		if(skaianet.getActiveConnection(clientPlayer).isPresent())
			return false;
		
		SburbPlayerData playerData = skaianet.getOrCreateData(clientPlayer);
		if(!playerData.hasPrimaryConnection())
		{
			if(!canMakeNewRegularConnectionAsServer(serverPlayer, skaianet))
				return false;
			
			newActiveConnection(clientComputer, serverComputer, SburbEvent.ConnectionType.REGULAR, skaianet);
			return true;
		}
		
		Optional<PlayerIdentifier> primaryServer = playerData.primaryServerPlayer();
		if(primaryServer.isEmpty())
		{
			if(!canMakeNewRegularConnectionAsServer(serverPlayer, skaianet))
				return false;
			
			newServerForClient(clientPlayer, serverPlayer, skaianet);
			newActiveConnection(clientComputer, serverComputer, SburbEvent.ConnectionType.NEW_SERVER, skaianet);
			return true;
		}
		
		if(primaryServer.get().equals(serverPlayer))
		{
			newActiveConnection(clientComputer, serverComputer, SburbEvent.ConnectionType.RESUME, skaianet);
			return true;
		}
		if(canMakeSecondaryConnection(clientPlayer, serverPlayer, skaianet))
		{
			newActiveConnection(clientComputer, serverComputer, SburbEvent.ConnectionType.SECONDARY, skaianet);
			return true;
		}
		return false;
	}
	
	private static void newActiveConnection(ISburbComputer client, ISburbComputer server, SburbEvent.ConnectionType type, SkaianetHandler skaianet)
	{
		Objects.requireNonNull(client);
		Objects.requireNonNull(server);
		
		if(skaianet.getActiveConnection(client.getOwner()).isPresent())
			throw new IllegalStateException("Should not activate sburb connection when already active");
		
		ActiveConnection activeConnection = new ActiveConnection(client, server);
		skaianet.activeConnections.add(activeConnection);
		skaianet.sessionHandler.onConnect(activeConnection.client(), activeConnection.server());
		skaianet.infoTracker.markDirty(activeConnection);
		
		client.connected(server.getOwner(), true);
		server.connected(client.getOwner(), false);
		
		MinecraftForge.EVENT_BUS.post(new SburbEvent.ConnectionCreated(skaianet.mcServer, activeConnection, type));
	}
	
	static void closeConnection(ActiveConnection connection, SkaianetHandler skaianet)
	{
		closeConnection(connection, null, null, skaianet);
	}
	
	static void closeConnection(ActiveConnection connection, @Nullable ISburbComputer clientComputer, @Nullable ISburbComputer serverComputer, SkaianetHandler skaianet)
	{
		if(clientComputer == null)
			clientComputer = connection.clientComputer().getComputer(skaianet.mcServer);
		if(serverComputer == null)
			serverComputer = connection.serverComputer().getComputer(skaianet.mcServer);
		
		skaianet.activeConnections.remove(connection);
		skaianet.sessionHandler.onDisconnect(connection.client(), connection.server());
		skaianet.infoTracker.markDirty(connection);
		
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
		
		MinecraftForge.EVENT_BUS.post(new SburbEvent.ConnectionClosed(skaianet.mcServer, connection));
	}
	
	static void trySetPrimaryConnection(ActiveConnection connection, SkaianetHandler skaianet)
	{
		trySetPrimaryConnection(connection.client(), connection.server(), skaianet);
	}
	
	static void trySetPrimaryConnection(PlayerIdentifier client, PlayerIdentifier server, SkaianetHandler skaianet)
	{
		if(skaianet.hasPrimaryConnectionForClient(client) || skaianet.hasPrimaryConnectionForServer(server))
			throw new IllegalStateException();
		
		Optional<ActiveConnection> activeConnection = skaianet.getActiveConnection(client);
		if(activeConnection.isPresent() && !activeConnection.get().server().equals(server))
			throw new IllegalStateException();
		
		if(activeConnection.isEmpty()
				&& !skaianet.activeConnections().filter(connection -> connection.server().equals(server))
				.allMatch(connection -> skaianet.getOrCreateData(connection.client()).hasPrimaryConnection()))
			throw new IllegalStateException();
		
		skaianet.getOrCreateData(client).setHasPrimaryConnection(server);
	}
	
	static void unlinkClientPlayer(PlayerIdentifier clientPlayer, SkaianetHandler skaianet)
	{
		skaianet.getActiveConnection(clientPlayer).ifPresent(activeConnection -> closeConnection(activeConnection, skaianet));
		skaianet.getOrCreateData(clientPlayer).removeServerPlayer();
	}
	
	static void unlinkServerPlayer(PlayerIdentifier serverPlayer, SkaianetHandler skaianet)
	{
		skaianet.primaryPartnerForServer(serverPlayer).ifPresent(clientPlayer -> unlinkClientPlayer(clientPlayer, skaianet));
		skaianet.activeConnections().filter(connection -> connection.server().equals(serverPlayer) && !skaianet.getOrCreateData(connection.client()).hasPrimaryConnection())
				.forEach(activeConnection -> closeConnection(activeConnection, skaianet));
	}
	
	static void newServerForClient(PlayerIdentifier clientPlayer, PlayerIdentifier serverPlayer, SkaianetHandler skaianet)
	{
		skaianet.getOrCreateData(clientPlayer).setNewServerPlayer(serverPlayer);
	}
	
	/**
	 * Prepares the sburb connection and data needed for after entry.
	 * Should only be called by the cruxite artifact on trigger before teleportation
	 *
	 * @param target   the identifier of the player that is entering
	 * @return The dimension type of the new land created, or null if the player can't enter at this time.
	 */
	public static ResourceKey<Level> prepareEntry(PlayerIdentifier target, SkaianetHandler skaianet)
	{
		SburbPlayerData playerData = skaianet.getOrCreateData(target);
		if(!playerData.hasPrimaryConnection())
		{
			Optional<ActiveConnection> connection = skaianet.getActiveConnection(target);
			if(connection.isPresent())
				trySetPrimaryConnection(connection.get(), skaianet);
			else
			{
				LOGGER.info("Player {} entered without connection.", target.getUsername());
				
				trySetPrimaryConnection(target, IdentifierHandler.NULL_IDENTIFIER, skaianet);
			}
		}
		
		if(playerData.getLandDimension() == null)
			SburbHandler.prepareEntry(playerData, skaianet.mcServer);
		
		return playerData.getLandDimension();
	}
}
