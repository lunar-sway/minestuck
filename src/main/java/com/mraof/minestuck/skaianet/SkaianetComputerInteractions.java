package com.mraof.minestuck.skaianet;

import com.mraof.minestuck.blockentity.ComputerBlockEntity;
import com.mraof.minestuck.computer.ComputerReference;
import com.mraof.minestuck.computer.ISburbComputer;
import com.mraof.minestuck.player.PlayerIdentifier;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.MinecraftServer;

import java.util.Optional;

public final class SkaianetComputerInteractions
{
	public static final String STOP_RESUME = "minestuck.stop_resume_message";
	
	private final SkaianetData skaianetData;
	private final ComputerWaitingList openedServers;
	private final ComputerWaitingList resumingClients;
	private final ComputerWaitingList resumingServers;
	
	SkaianetComputerInteractions(SkaianetData skaianetData)
	{
		this.skaianetData = skaianetData;
		openedServers = new ComputerWaitingList(skaianetData.infoTracker, false, "opened server");
		resumingClients = new ComputerWaitingList(skaianetData.infoTracker, true, "resuming client");
		resumingServers = new ComputerWaitingList(skaianetData.infoTracker, false, "resuming server");
	}
	
	SkaianetComputerInteractions(SkaianetData skaianetData, CompoundTag tag)
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
	
	void validate()
	{
		openedServers.validate(skaianetData.mcServer);
		resumingClients.validate(skaianetData.mcServer);
		resumingServers.validate(skaianetData.mcServer);
	}
	
	public static SkaianetComputerInteractions get(MinecraftServer mcServer)
	{
		return SkaianetData.get(mcServer).computerInteractions;
	}
	
	boolean hasResumingClient(PlayerIdentifier identifier)
	{
		return resumingClients.contains(identifier);
	}
	
	boolean hasResumingServer(PlayerIdentifier identifier)
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
		
		openedServers.useComputerAndRemoveOnSuccess(serverPlayer, skaianetData.mcServer,
				serverComputer -> skaianetData.connectionInteractions.tryConnect(computer, serverComputer));
	}
	
	public void resumeClientConnection(ISburbComputer computer)
	{
		PlayerIdentifier player = computer.getOwner();
		if(computer.createReference().isInNether() || isClientActive(player))
			return;
		
		if(!skaianetData.hasPrimaryConnectionForClient(player))
			return;
		
		Optional<PlayerIdentifier> server = skaianetData.primaryPartnerForClient(player);
		if(server.isPresent() && resumingServers.contains(server.get()))
		{
			resumingServers.useComputerAndRemoveOnSuccess(server.get(), skaianetData.mcServer, otherComputer ->
					skaianetData.connectionInteractions.tryConnect(computer, otherComputer));
			return;
		}
		if(server.isPresent() && openedServers.contains(server.get()))
		{
			openedServers.useComputerAndRemoveOnSuccess(server.get(), skaianetData.mcServer, otherComputer ->
					skaianetData.connectionInteractions.tryConnect(computer, otherComputer));
			return;
		}
		
		resumingClients.put(player, computer);
	}
	
	public void resumeServerConnection(ISburbComputer computer)
	{
		PlayerIdentifier player = computer.getOwner();
		if(computer.createReference().isInNether() || hasResumingServer(player))
			return;
		
		Optional<PlayerIdentifier> client = skaianetData.primaryPartnerForServer(player);
		if(client.isEmpty())
			return;
		
		if(resumingClients.contains(client.get()))
		{
			resumingClients.useComputerAndRemoveOnSuccess(client.get(), skaianetData.mcServer, otherComputer ->
					skaianetData.connectionInteractions.tryConnect(otherComputer, computer));
			return;
		}
		
		resumingServers.put(player, computer);
	}
	
	public void openServer(ISburbComputer computer)
	{
		PlayerIdentifier player = computer.getOwner();
		if(computer.createReference().isInNether() || hasResumingServer(player))
			return;
		
		Optional<PlayerIdentifier> primaryClient = skaianetData.primaryPartnerForServer(player);
		if(primaryClient.isPresent() && resumingClients.contains(primaryClient.get()))
		{
			resumingClients.useComputerAndRemoveOnSuccess(primaryClient.get(), skaianetData.mcServer, clientComputer ->
					skaianetData.connectionInteractions.tryConnect(clientComputer, computer));
			return;
		}
		
		openedServers.put(player, computer);
	}
	
	public void closeClientConnectionRemotely(PlayerIdentifier player)
	{
		if(resumingClients.contains(player))
		{
			resumingClients.useComputerAndRemoveOnSuccess(player, skaianetData.mcServer, computer -> {
				computer.putClientBoolean("isResuming", false);
				computer.putClientMessage(STOP_RESUME);
				return true;
			});
		} else
		{
			skaianetData.connectionInteractions.getActiveConnection(player).ifPresent(skaianetData.connectionInteractions::closeConnection);
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
			skaianetData.connectionInteractions.getClientConnection(computer).ifPresent(connection ->
					skaianetData.connectionInteractions.closeConnection(connection, computer, null));
		}
	}
	
	public void closeServerConnection(ISburbComputer computer)
	{
		checkAndCloseFromServerList(computer, openedServers);
		checkAndCloseFromServerList(computer, resumingServers);
		
		skaianetData.connectionInteractions.getServerConnection(computer).ifPresent(connection ->
				skaianetData.connectionInteractions.closeConnection(connection, null, computer));
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
		
		skaianetData.connectionInteractions.activeConnections().forEach(c -> c.updateComputer(oldBE, newRef));
		
		resumingClients.replace(oldBE.owner, oldRef, newRef);
		resumingServers.replace(oldBE.owner, oldRef, newRef);
		openedServers.replace(oldBE.owner, oldRef, newRef);
	}
	
	private boolean isClientActive(PlayerIdentifier player)
	{
		return skaianetData.connectionInteractions.getActiveConnection(player).isPresent() || hasResumingClient(player);
	}
}
