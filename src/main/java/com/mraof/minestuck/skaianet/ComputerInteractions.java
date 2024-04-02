package com.mraof.minestuck.skaianet;

import com.mraof.minestuck.blockentity.ComputerBlockEntity;
import com.mraof.minestuck.computer.ComputerReference;
import com.mraof.minestuck.computer.ISburbComputer;
import com.mraof.minestuck.player.PlayerIdentifier;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;

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
		return computer.getClientBoolean("isResuming");
	}
	
	private static boolean isValidServerOpenOrResuming(ISburbComputer computer)
	{
		return computer.getServerBoolean("isOpen");
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
		PlayerIdentifier player = computer.getOwner();
		
		if(computer.createReference().isInNether())
			return;
		
		if(isClientActive(player))
			return;
		
		openedServers.useComputerAndRemoveOnSuccess(serverPlayer, serverComputer ->
				skaianetData.connections.tryConnect(computer, serverComputer));
	}
	
	public void resumeClientConnection(ISburbComputer computer)
	{
		PlayerIdentifier player = computer.getOwner();
		if(computer.createReference().isInNether() || isClientActive(player))
			return;
		
		if(!skaianetData.connections.hasPrimaryConnectionForClient(player))
			return;
		
		Optional<PlayerIdentifier> server = skaianetData.connections.primaryPartnerForClient(player);
		if(server.isPresent() && resumingServers.contains(server.get()))
		{
			resumingServers.useComputerAndRemoveOnSuccess(server.get(), otherComputer ->
					skaianetData.connections.tryConnect(computer, otherComputer));
			return;
		}
		if(server.isPresent() && openedServers.contains(server.get()))
		{
			openedServers.useComputerAndRemoveOnSuccess(server.get(), otherComputer ->
					skaianetData.connections.tryConnect(computer, otherComputer));
			return;
		}
		
		computer.putClientBoolean("isResuming", true);
		resumingClients.put(player, computer.createReference());
	}
	
	public void resumeServerConnection(ISburbComputer computer)
	{
		PlayerIdentifier player = computer.getOwner();
		if(computer.createReference().isInNether() || hasResumingServer(player))
			return;
		
		Optional<PlayerIdentifier> client = skaianetData.connections.primaryPartnerForServer(player);
		if(client.isEmpty())
			return;
		
		if(resumingClients.contains(client.get()))
		{
			resumingClients.useComputerAndRemoveOnSuccess(client.get(), otherComputer ->
					skaianetData.connections.tryConnect(otherComputer, computer));
			return;
		}
		
		computer.putServerBoolean("isOpen", true);
		resumingServers.put(player, computer.createReference());
	}
	
	public void openServer(ISburbComputer computer)
	{
		PlayerIdentifier player = computer.getOwner();
		if(computer.createReference().isInNether() || hasResumingServer(player))
			return;
		
		Optional<PlayerIdentifier> primaryClient = skaianetData.connections.primaryPartnerForServer(player);
		if(primaryClient.isPresent() && resumingClients.contains(primaryClient.get()))
		{
			resumingClients.useComputerAndRemoveOnSuccess(primaryClient.get(), clientComputer ->
					skaianetData.connections.tryConnect(clientComputer, computer));
			return;
		}
		
		computer.putServerBoolean("isOpen", true);
		openedServers.put(player, computer.createReference());
	}
	
	public void closeClientConnectionRemotely(PlayerIdentifier player)
	{
		if(resumingClients.contains(player))
		{
			resumingClients.useComputerAndRemoveOnSuccess(player, computer -> {
				computer.putClientBoolean("isResuming", false);
				computer.putClientMessage(STOP_RESUME);
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
			computer.putClientBoolean("isResuming", false);
			computer.putClientMessage(STOP_RESUME);
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
			computer.putServerBoolean("isOpen", false);
			computer.putServerMessage(STOP_RESUME);
		}
	}
	
	public void movingComputer(ComputerBlockEntity oldBE, ComputerBlockEntity newBE)
	{
		ComputerReference oldRef = ComputerReference.of(oldBE), newRef = ComputerReference.of(newBE);
		if(!oldBE.owner.equals(newBE.owner))
			throw new IllegalStateException("Moving computers with different owners! (" + oldBE.owner + " and " + newBE.owner + ")");
		
		skaianetData.connections.activeConnections().forEach(c -> c.updateComputer(oldBE, newRef));
		
		resumingClients.replace(oldBE.owner, oldRef, newRef);
		resumingServers.replace(oldBE.owner, oldRef, newRef);
		openedServers.replace(oldBE.owner, oldRef, newRef);
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
