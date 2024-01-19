package com.mraof.minestuck.skaianet;

import com.mraof.minestuck.blockentity.ComputerBlockEntity;
import com.mraof.minestuck.computer.ComputerReference;
import com.mraof.minestuck.computer.ISburbComputer;
import com.mraof.minestuck.event.SburbEvent;
import com.mraof.minestuck.player.PlayerIdentifier;
import net.minecraft.server.MinecraftServer;

import java.util.Optional;
import java.util.function.Consumer;

public final class SkaianetComputerInteractions
{
	public static final String STOP_RESUME = "minestuck.stop_resume_message";
	
	public static void connectToServerPlayer(ISburbComputer computer, PlayerIdentifier serverPlayer, MinecraftServer mcServer)
	{
		SkaianetHandler skaianet = SkaianetHandler.get(mcServer);
		PlayerIdentifier player = computer.getOwner();
		
		if(computer.createReference().isInNether())
			return;
		
		if(skaianet.isClientActive(player))
			return;
		
		if(!skaianet.openedServers.contains(serverPlayer))
			return;
		
		SburbPlayerData playerData = skaianet.getOrCreateData(player);
		if(!playerData.hasPrimaryConnection())
		{
			if(!skaianet.canMakeNewRegularConnectionAsServer(serverPlayer))
				return;
			
			removeAndUseOpenServerComputer(skaianet, serverPlayer, serverComputer ->
					skaianet.setActive(computer, serverComputer, SburbEvent.ConnectionType.REGULAR));
			return;
		}
		
		Optional<PlayerIdentifier> primaryServer = playerData.primaryServerPlayer();
		if(primaryServer.isEmpty())
		{
			if(!skaianet.canMakeNewRegularConnectionAsServer(serverPlayer))
				return;
			
			removeAndUseOpenServerComputer(skaianet, serverPlayer, serverComputer -> {
				skaianet.newServerForClient(player, serverPlayer);
				skaianet.setActive(computer, serverComputer, SburbEvent.ConnectionType.NEW_SERVER);
			});
			return;
		}
		
		if(primaryServer.get().equals(serverPlayer))
		{
			removeAndUseOpenServerComputer(skaianet, serverPlayer, serverComputer ->
					skaianet.setActive(computer, serverComputer, SburbEvent.ConnectionType.RESUME));
		} else if(skaianet.sessionHandler.canMakeSecondaryConnection(player, serverPlayer))
		{
			removeAndUseOpenServerComputer(skaianet, serverPlayer, serverComputer ->
					skaianet.setActive(computer, serverComputer, SburbEvent.ConnectionType.SECONDARY));
		}
	}
	
	public static void resumeClientConnection(ISburbComputer computer, MinecraftServer mcServer)
	{
		SkaianetHandler skaianet = SkaianetHandler.get(mcServer);
		PlayerIdentifier player = computer.getOwner();
		if(computer.createReference().isInNether() || skaianet.isClientActive(player))
			return;
		
		if(!skaianet.hasPrimaryConnectionForClient(player))
			return;
		
		Optional<PlayerIdentifier> server = skaianet.primaryPartnerForClient(player);
		if(server.isPresent() && skaianet.resumingServers.contains(server.get()))
		{
			removeAndUseResumingServerComputer(skaianet, server.get(), otherComputer ->
					skaianet.setActive(computer, otherComputer, SburbEvent.ConnectionType.RESUME));
			return;
		}
		if(server.isPresent() && skaianet.openedServers.contains(server.get()))
		{
			removeAndUseOpenServerComputer(skaianet, server.get(), otherComputer ->
					skaianet.setActive(computer, otherComputer, SburbEvent.ConnectionType.RESUME));
			return;
		}
		
		skaianet.resumingClients.put(player, computer);
	}
	
	public static void resumeServerConnection(ISburbComputer computer, MinecraftServer mcServer)
	{
		SkaianetHandler skaianet = SkaianetHandler.get(mcServer);
		PlayerIdentifier player = computer.getOwner();
		if(computer.createReference().isInNether() || skaianet.hasResumingServer(player))
			return;
		
		Optional<PlayerIdentifier> client = skaianet.primaryPartnerForServer(player);
		if(client.isEmpty())
			return;
		
		if(skaianet.resumingClients.contains(client.get()))
		{
			removeAndUseResumingClientComputer(skaianet, client.get(), otherComputer ->
					skaianet.setActive(otherComputer, computer, SburbEvent.ConnectionType.RESUME));
			return;
		}
		
		skaianet.resumingServers.put(player, computer);
	}
	
	public static void openServer(ISburbComputer computer, MinecraftServer mcServer)
	{
		SkaianetHandler skaianet = SkaianetHandler.get(mcServer);
		PlayerIdentifier player = computer.getOwner();
		ComputerReference reference = computer.createReference();
		if(reference.isInNether() || skaianet.hasResumingServer(player))
			return;
		
		Optional<PlayerIdentifier> primaryClient = skaianet.primaryPartnerForServer(player);
		if(primaryClient.isPresent() && skaianet.getActiveConnection(primaryClient.get()).isEmpty()
				&& skaianet.resumingClients.contains(primaryClient.get()))
		{
			ISburbComputer clientComputer = skaianet.resumingClients.getComputer(skaianet.mcServer, primaryClient.get());
			
			if(clientComputer != null)
			{
				skaianet.resumingClients.remove(primaryClient.get());
				skaianet.setActive(clientComputer, computer, SburbEvent.ConnectionType.RESUME);
			}
		} else
		{
			skaianet.openedServers.put(player, computer);
		}
	}
	
	private static void removeAndUseResumingClientComputer(SkaianetHandler skaianet, PlayerIdentifier client, Consumer<ISburbComputer> computerConsumer)
	{
		ISburbComputer clientComputer = skaianet.resumingClients.getComputer(skaianet.mcServer, client);
		if(clientComputer == null)
			return;
		computerConsumer.accept(clientComputer);
		skaianet.resumingClients.remove(client);
	}
	
	private static void removeAndUseOpenServerComputer(SkaianetHandler skaianet, PlayerIdentifier server, Consumer<ISburbComputer> computerConsumer)
	{
		ISburbComputer serverComputer = skaianet.openedServers.getComputer(skaianet.mcServer, server);
		if(serverComputer == null)
			return;
		computerConsumer.accept(serverComputer);
		skaianet.openedServers.remove(server);
	}
	
	private static void removeAndUseResumingServerComputer(SkaianetHandler skaianet, PlayerIdentifier server, Consumer<ISburbComputer> computerConsumer)
	{
		ISburbComputer serverComputer = skaianet.resumingServers.getComputer(skaianet.mcServer, server);
		if(serverComputer == null)
			return;
		computerConsumer.accept(serverComputer);
		skaianet.resumingServers.remove(server);
	}
	
	public static void closeClientConnectionRemotely(PlayerIdentifier player, SkaianetHandler skaianet)
	{
		if(skaianet.resumingClients.contains(player))
		{
			ISburbComputer computer = skaianet.resumingClients.getComputer(skaianet.mcServer, player);
			skaianet.resumingClients.remove(player);
			if(computer != null)
			{
				computer.putClientBoolean("isResuming", false);
				computer.putClientMessage(STOP_RESUME);
			}
		} else
		{
			skaianet.getActiveConnection(player).ifPresent(skaianet::closeConnection);
		}
	}
	
	public static void closeClientConnection(ISburbComputer computer, SkaianetHandler skaianet)
	{
		PlayerIdentifier owner = computer.getOwner();
		if(skaianet.resumingClients.contains(computer))
		{
			skaianet.resumingClients.remove(owner);
			computer.putClientBoolean("isResuming", false);
			computer.putClientMessage(STOP_RESUME);
		} else
		{
			skaianet.getClientConnection(computer).ifPresent(connection ->
					skaianet.closeConnection(connection, computer, null));
		}
	}
	
	public static void closeServerConnection(ISburbComputer computer, SkaianetHandler skaianet)
	{
		checkAndCloseFromServerList(computer, skaianet.openedServers);
		checkAndCloseFromServerList(computer, skaianet.resumingServers);
		
		skaianet.getServerConnection(computer).ifPresent(connection ->
				skaianet.closeConnection(connection, null, computer));
	}
	
	private static void checkAndCloseFromServerList(ISburbComputer computer, ComputerWaitingList map)
	{
		PlayerIdentifier owner = computer.getOwner();
		if(map.contains(computer))
		{
			map.remove(owner);
			computer.putServerBoolean("isOpen", false);
			computer.putServerMessage(STOP_RESUME);
		}
	}
	
	public static void movingComputer(ComputerBlockEntity oldBE, ComputerBlockEntity newBE, SkaianetHandler skaianet)
	{
		ComputerReference oldRef = ComputerReference.of(oldBE), newRef = ComputerReference.of(newBE);
		if(!oldBE.owner.equals(newBE.owner))
			throw new IllegalStateException("Moving computers with different owners! ("+oldBE.owner+" and "+newBE.owner+")");
		
		skaianet.activeConnections().forEach(c -> c.updateComputer(oldBE, newRef));
		
		skaianet.resumingClients.replace(oldBE.owner, oldRef, newRef);
		skaianet.resumingServers.replace(oldBE.owner, oldRef, newRef);
		skaianet.openedServers.replace(oldBE.owner, oldRef, newRef);
	}
}
