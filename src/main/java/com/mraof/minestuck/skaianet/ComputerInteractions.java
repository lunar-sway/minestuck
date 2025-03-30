package com.mraof.minestuck.skaianet;

import com.mraof.minestuck.blockentity.ComputerBlockEntity;
import com.mraof.minestuck.computer.ComputerReference;
import com.mraof.minestuck.computer.ISburbComputer;
import com.mraof.minestuck.computer.SburbClientData;
import com.mraof.minestuck.computer.SburbServerData;
import com.mraof.minestuck.player.PlayerIdentifier;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;

import java.util.Objects;
import java.util.Optional;

/**
 * Forms the point of interaction for sburb computer programs, and maintains the lists of computers waiting to connect.

 * @author kirderf1
 */
public final class ComputerInteractions
{
	public static final String STOP_RESUME = "minestuck.stop_resume_message";
	
	private final SkaianetData skaianetData;
	private final ComputerWaitingList openedServers;
	private final ComputerWaitingList resumingClients;
	private final ComputerWaitingList resumingServers;
	
	ComputerInteractions(SkaianetData skaianetData)
	{
		this.skaianetData = skaianetData;
		openedServers = new ComputerWaitingList(skaianetData, ComputerInteractions::isValidServerOpenOrResuming, "opened server");
		resumingClients = new ComputerWaitingList(skaianetData, ComputerInteractions::isValidClientResuming, "resuming client");
		resumingServers = new ComputerWaitingList(skaianetData, ComputerInteractions::isValidServerOpenOrResuming, "resuming server");
	}
	
	ComputerInteractions(SkaianetData skaianetData, CompoundTag tag)
	{
		this(skaianetData);
		openedServers.read(tag.getList("serversOpen", Tag.TAG_COMPOUND));
		resumingClients.read(tag.getList("resumingClients", Tag.TAG_COMPOUND));
		resumingServers.read(tag.getList("resumingServers", Tag.TAG_COMPOUND));
	}
	
	void write(CompoundTag tag)
	{
		tag.put("serversOpen", openedServers.write());
		tag.put("resumingClients", resumingClients.write());
		tag.put("resumingServers", resumingServers.write());
	}
	
	private static boolean isValidClientResuming(ISburbComputer computer)
	{
		return computer.getSburbClientData().map(SburbClientData::isResuming).orElse(false);
	}
	
	private static boolean isValidServerOpenOrResuming(ISburbComputer computer)
	{
		return computer.getSburbServerData().map(SburbServerData::isOpen).orElse(false);
	}
	
	public static ComputerInteractions get(MinecraftServer mcServer)
	{
		return SkaianetData.get(mcServer).computerInteractions;
	}
	
	public boolean hasResumingClient(PlayerIdentifier identifier)
	{
		return resumingClients.contains(identifier);
	}
	
	public boolean hasResumingServer(PlayerIdentifier identifier)
	{
		return resumingServers.contains(identifier) || openedServers.contains(identifier);
	}
	
	Iterable<PlayerIdentifier> openServerPlayers()
	{
		return this.openedServers.getPlayers();
	}
	
	public void connectToServerPlayer(ISburbComputer computer, PlayerIdentifier serverPlayer)
	{
		Optional<ClientAccess> client = ClientAccess.tryGet(computer);
		if(client.isEmpty())
			return;
		
		PlayerIdentifier player = computer.getOwner();
		
		if(computer.createReference().isInNether())
			return;
		
		if(isClientActive(player))
			return;
		
		openedServers.useAsServerAndRemoveOnSuccess(serverPlayer, server ->
				skaianetData.connections.tryConnect(client.get(), server));
	}
	
	public void resumeClientConnection(ISburbComputer clientComputer)
	{
		Optional<ClientAccess> client = ClientAccess.tryGet(clientComputer);
		if(client.isEmpty())
			return;
		
		PlayerIdentifier player = clientComputer.getOwner();
		if(clientComputer.createReference().isInNether() || isClientActive(player))
			return;
		
		if(!skaianetData.connections.hasPrimaryConnectionForClient(player))
			return;
		
		Optional<PlayerIdentifier> serverPlayer = skaianetData.connections.primaryPartnerForClient(player);
		if(serverPlayer.isPresent() && resumingServers.contains(serverPlayer.get()))
		{
			resumingServers.useAsServerAndRemoveOnSuccess(serverPlayer.get(), server ->
					skaianetData.connections.tryConnect(client.get(), server));
			return;
		}
		if(serverPlayer.isPresent() && openedServers.contains(serverPlayer.get()))
		{
			openedServers.useAsServerAndRemoveOnSuccess(serverPlayer.get(), server ->
					skaianetData.connections.tryConnect(client.get(), server));
			return;
		}
		
		client.get().data().setIsResuming(true);
		resumingClients.put(player, clientComputer.createReference());
	}
	
	public void resumeServerConnection(ISburbComputer serverComputer)
	{
		Optional<ServerAccess> server = ServerAccess.tryGet(serverComputer);
		if(server.isEmpty())
			return;
		
		PlayerIdentifier player = serverComputer.getOwner();
		if(serverComputer.createReference().isInNether() || hasResumingServer(player))
			return;
		
		Optional<PlayerIdentifier> clientPlayer = skaianetData.connections.primaryPartnerForServer(player);
		if(clientPlayer.isEmpty())
			return;
		
		if(resumingClients.contains(clientPlayer.get()))
		{
			resumingClients.useAsClientAndRemoveOnSuccess(clientPlayer.get(), client ->
					skaianetData.connections.tryConnect(client, server.get()));
			return;
		}
		
		server.get().data().setIsOpen(true);
		resumingServers.put(player, serverComputer.createReference());
	}
	
	public void openServer(ISburbComputer serverComputer)
	{
		Optional<ServerAccess> server = ServerAccess.tryGet(serverComputer);
		if(server.isEmpty())
			return;
		
		PlayerIdentifier player = serverComputer.getOwner();
		if(serverComputer.createReference().isInNether() || hasResumingServer(player))
			return;
		
		Optional<PlayerIdentifier> primaryClient = skaianetData.connections.primaryPartnerForServer(player);
		if(primaryClient.isPresent() && resumingClients.contains(primaryClient.get()))
		{
			resumingClients.useAsClientAndRemoveOnSuccess(primaryClient.get(), client ->
					skaianetData.connections.tryConnect(client, server.get()));
			return;
		}
		
		server.get().data().setIsOpen(true);
		openedServers.put(player, serverComputer.createReference());
	}
	
	public void closeClientConnectionRemotely(PlayerIdentifier player)
	{
		if(resumingClients.contains(player))
		{
			resumingClients.useAsClientAndRemoveOnSuccess(player, client -> {
				client.data().setIsResuming(false);
				client.data().setEventMessage(STOP_RESUME);
				return true;
			});
		} else
		{
			skaianetData.connections.getActiveConnection(player)
					.ifPresent(skaianetData.connections::closeConnection);
		}
	}
	
	public void closeClientConnection(ISburbComputer computer)
	{
		PlayerIdentifier owner = computer.getOwner();
		if(resumingClients.contains(computer))
		{
			resumingClients.remove(owner);
			computer.getSburbClientData().ifPresent(sburbClientData -> {
				sburbClientData.setIsResuming(false);
				sburbClientData.setEventMessage(STOP_RESUME);
			});
		} else
		{
			skaianetData.connections.getClientConnection(computer).ifPresent(connection ->
					skaianetData.connections.closeConnection(connection, computer, null));
		}
	}
	
	public void closeServerConnection(ISburbComputer computer)
	{
		checkAndCloseFromServerList(computer, openedServers);
		checkAndCloseFromServerList(computer, resumingServers);
		
		skaianetData.connections.getServerConnection(computer).ifPresent(connection ->
				skaianetData.connections.closeConnection(connection, null, computer));
	}
	
	private static void checkAndCloseFromServerList(ISburbComputer computer, ComputerWaitingList map)
	{
		PlayerIdentifier owner = computer.getOwner();
		if(map.contains(computer))
		{
			map.remove(owner);
			computer.getSburbServerData().ifPresent(sburbServerData -> {
				sburbServerData.setIsOpen(false);
				sburbServerData.setEventMessage(STOP_RESUME);
			});
		}
	}
	
	public void movingComputer(ComputerBlockEntity oldBE, ComputerBlockEntity newBE)
	{
		ComputerReference oldRef = ComputerReference.of(oldBE), newRef = ComputerReference.of(newBE);
		if(!Objects.equals(oldBE.getOwner(), newBE.getOwner()))
			throw new IllegalStateException("Moving computers with different owners! (" + oldBE.getOwner() + " and " + newBE.getOwner() + ")");
		
		skaianetData.connections.activeConnections().forEach(c -> c.updateComputer(oldBE, newRef));
		
		resumingClients.replace(oldBE.getOwner(), oldRef, newRef);
		resumingServers.replace(oldBE.getOwner(), oldRef, newRef);
		openedServers.replace(oldBE.getOwner(), oldRef, newRef);
	}
	
	public boolean isClientActive(PlayerIdentifier player)
	{
		return skaianetData.connections.getActiveConnection(player).isPresent() || hasResumingClient(player);
	}
	
	public static void requestInfo(ServerPlayer requestingPlayer, PlayerIdentifier requestedPlayer)
	{
		SkaianetData.get(requestingPlayer.server).infoTracker.requestInfo(requestingPlayer, requestedPlayer);
	}
}
