package com.mraof.minestuck.skaianet;

import com.mraof.minestuck.blockentity.ComputerBlockEntity;
import com.mraof.minestuck.computer.ComputerReference;
import com.mraof.minestuck.computer.ISburbComputer;
import com.mraof.minestuck.player.PlayerIdentifier;
import net.minecraft.server.MinecraftServer;

import java.util.Optional;

public final class SkaianetComputerInteractions
{
	public static final String STOP_RESUME = "minestuck.stop_resume_message";
	
	public static void connectToServerPlayer(ISburbComputer computer, PlayerIdentifier serverPlayer, MinecraftServer mcServer)
	{
		SkaianetHandler skaianet = SkaianetHandler.get(mcServer);
		PlayerIdentifier player = computer.getOwner();
		
		if(computer.createReference().isInNether())
			return;
		
		if(isClientActive(skaianet, player))
			return;
		
		skaianet.openedServers.useComputerAndRemoveOnSuccess(serverPlayer, mcServer,
				serverComputer -> skaianet.tryConnect(computer, serverComputer));
	}
	
	public static void resumeClientConnection(ISburbComputer computer, MinecraftServer mcServer)
	{
		SkaianetHandler skaianet = SkaianetHandler.get(mcServer);
		PlayerIdentifier player = computer.getOwner();
		if(computer.createReference().isInNether() || isClientActive(skaianet, player))
			return;
		
		if(!skaianet.hasPrimaryConnectionForClient(player))
			return;
		
		Optional<PlayerIdentifier> server = skaianet.primaryPartnerForClient(player);
		if(server.isPresent() && skaianet.resumingServers.contains(server.get()))
		{
			skaianet.resumingServers.useComputerAndRemoveOnSuccess(server.get(), mcServer, otherComputer ->
					skaianet.tryConnect(computer, otherComputer));
			return;
		}
		if(server.isPresent() && skaianet.openedServers.contains(server.get()))
		{
			skaianet.openedServers.useComputerAndRemoveOnSuccess(server.get(), mcServer, otherComputer ->
					skaianet.tryConnect(computer, otherComputer));
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
			skaianet.resumingClients.useComputerAndRemoveOnSuccess(client.get(), mcServer, otherComputer ->
					skaianet.tryConnect(otherComputer, computer));
			return;
		}
		
		skaianet.resumingServers.put(player, computer);
	}
	
	public static void openServer(ISburbComputer computer, MinecraftServer mcServer)
	{
		SkaianetHandler skaianet = SkaianetHandler.get(mcServer);
		PlayerIdentifier player = computer.getOwner();
		if(computer.createReference().isInNether() || skaianet.hasResumingServer(player))
			return;
		
		Optional<PlayerIdentifier> primaryClient = skaianet.primaryPartnerForServer(player);
		if(primaryClient.isPresent() && skaianet.resumingClients.contains(primaryClient.get()))
		{
			skaianet.resumingClients.useComputerAndRemoveOnSuccess(primaryClient.get(), mcServer, clientComputer ->
					skaianet.tryConnect(clientComputer, computer));
			return;
		}
		
		skaianet.openedServers.put(player, computer);
	}
	
	public static void closeClientConnectionRemotely(PlayerIdentifier player, MinecraftServer mcServer)
	{
		SkaianetHandler skaianet = SkaianetHandler.get(mcServer);
		if(skaianet.resumingClients.contains(player))
		{
			skaianet.resumingClients.useComputerAndRemoveOnSuccess(player, mcServer, computer -> {
				computer.putClientBoolean("isResuming", false);
				computer.putClientMessage(STOP_RESUME);
				return true;
			});
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
			throw new IllegalStateException("Moving computers with different owners! (" + oldBE.owner + " and " + newBE.owner + ")");
		
		skaianet.activeConnections().forEach(c -> c.updateComputer(oldBE, newRef));
		
		skaianet.resumingClients.replace(oldBE.owner, oldRef, newRef);
		skaianet.resumingServers.replace(oldBE.owner, oldRef, newRef);
		skaianet.openedServers.replace(oldBE.owner, oldRef, newRef);
	}
	
	private static boolean isClientActive(SkaianetHandler skaianet, PlayerIdentifier player)
	{
		return skaianet.getActiveConnection(player).isPresent() || skaianet.hasResumingClient(player);
	}
}
