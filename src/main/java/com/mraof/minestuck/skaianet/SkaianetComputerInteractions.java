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
		SkaianetData skaianetData = SkaianetData.get(mcServer);
		PlayerIdentifier player = computer.getOwner();
		
		if(computer.createReference().isInNether())
			return;
		
		if(isClientActive(skaianetData, player))
			return;
		
		skaianetData.openedServers.useComputerAndRemoveOnSuccess(serverPlayer, mcServer,
				serverComputer -> SkaianetConnectionInteractions.tryConnect(computer, serverComputer, skaianetData));
	}
	
	public static void resumeClientConnection(ISburbComputer computer, MinecraftServer mcServer)
	{
		SkaianetData skaianetData = SkaianetData.get(mcServer);
		PlayerIdentifier player = computer.getOwner();
		if(computer.createReference().isInNether() || isClientActive(skaianetData, player))
			return;
		
		if(!skaianetData.hasPrimaryConnectionForClient(player))
			return;
		
		Optional<PlayerIdentifier> server = skaianetData.primaryPartnerForClient(player);
		if(server.isPresent() && skaianetData.resumingServers.contains(server.get()))
		{
			skaianetData.resumingServers.useComputerAndRemoveOnSuccess(server.get(), mcServer, otherComputer ->
					SkaianetConnectionInteractions.tryConnect(computer, otherComputer, skaianetData));
			return;
		}
		if(server.isPresent() && skaianetData.openedServers.contains(server.get()))
		{
			skaianetData.openedServers.useComputerAndRemoveOnSuccess(server.get(), mcServer, otherComputer ->
					SkaianetConnectionInteractions.tryConnect(computer, otherComputer, skaianetData));
			return;
		}
		
		skaianetData.resumingClients.put(player, computer);
	}
	
	public static void resumeServerConnection(ISburbComputer computer, MinecraftServer mcServer)
	{
		SkaianetData skaianetData = SkaianetData.get(mcServer);
		PlayerIdentifier player = computer.getOwner();
		if(computer.createReference().isInNether() || skaianetData.hasResumingServer(player))
			return;
		
		Optional<PlayerIdentifier> client = skaianetData.primaryPartnerForServer(player);
		if(client.isEmpty())
			return;
		
		if(skaianetData.resumingClients.contains(client.get()))
		{
			skaianetData.resumingClients.useComputerAndRemoveOnSuccess(client.get(), mcServer, otherComputer ->
					SkaianetConnectionInteractions.tryConnect(otherComputer, computer, skaianetData));
			return;
		}
		
		skaianetData.resumingServers.put(player, computer);
	}
	
	public static void openServer(ISburbComputer computer, MinecraftServer mcServer)
	{
		SkaianetData skaianetData = SkaianetData.get(mcServer);
		PlayerIdentifier player = computer.getOwner();
		if(computer.createReference().isInNether() || skaianetData.hasResumingServer(player))
			return;
		
		Optional<PlayerIdentifier> primaryClient = skaianetData.primaryPartnerForServer(player);
		if(primaryClient.isPresent() && skaianetData.resumingClients.contains(primaryClient.get()))
		{
			skaianetData.resumingClients.useComputerAndRemoveOnSuccess(primaryClient.get(), mcServer, clientComputer ->
					SkaianetConnectionInteractions.tryConnect(clientComputer, computer, skaianetData));
			return;
		}
		
		skaianetData.openedServers.put(player, computer);
	}
	
	public static void closeClientConnectionRemotely(PlayerIdentifier player, MinecraftServer mcServer)
	{
		SkaianetData skaianetData = SkaianetData.get(mcServer);
		if(skaianetData.resumingClients.contains(player))
		{
			skaianetData.resumingClients.useComputerAndRemoveOnSuccess(player, mcServer, computer -> {
				computer.putClientBoolean("isResuming", false);
				computer.putClientMessage(STOP_RESUME);
				return true;
			});
		} else
		{
			skaianetData.getActiveConnection(player).ifPresent(activeConnection -> SkaianetConnectionInteractions.closeConnection(activeConnection, skaianetData));
		}
	}
	
	public static void closeClientConnection(ISburbComputer computer, SkaianetData skaianetData)
	{
		PlayerIdentifier owner = computer.getOwner();
		if(skaianetData.resumingClients.contains(computer))
		{
			skaianetData.resumingClients.remove(owner);
			computer.putClientBoolean("isResuming", false);
			computer.putClientMessage(STOP_RESUME);
		} else
		{
			skaianetData.getClientConnection(computer).ifPresent(connection ->
					SkaianetConnectionInteractions.closeConnection(connection, computer, null, skaianetData));
		}
	}
	
	public static void closeServerConnection(ISburbComputer computer, SkaianetData skaianetData)
	{
		checkAndCloseFromServerList(computer, skaianetData.openedServers);
		checkAndCloseFromServerList(computer, skaianetData.resumingServers);
		
		skaianetData.getServerConnection(computer).ifPresent(connection ->
				SkaianetConnectionInteractions.closeConnection(connection, null, computer, skaianetData));
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
	
	public static void movingComputer(ComputerBlockEntity oldBE, ComputerBlockEntity newBE, SkaianetData skaianetData)
	{
		ComputerReference oldRef = ComputerReference.of(oldBE), newRef = ComputerReference.of(newBE);
		if(!oldBE.owner.equals(newBE.owner))
			throw new IllegalStateException("Moving computers with different owners! (" + oldBE.owner + " and " + newBE.owner + ")");
		
		skaianetData.activeConnections().forEach(c -> c.updateComputer(oldBE, newRef));
		
		skaianetData.resumingClients.replace(oldBE.owner, oldRef, newRef);
		skaianetData.resumingServers.replace(oldBE.owner, oldRef, newRef);
		skaianetData.openedServers.replace(oldBE.owner, oldRef, newRef);
	}
	
	private static boolean isClientActive(SkaianetData skaianetData, PlayerIdentifier player)
	{
		return skaianetData.getActiveConnection(player).isPresent() || skaianetData.hasResumingClient(player);
	}
}
