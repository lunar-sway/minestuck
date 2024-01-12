package com.mraof.minestuck.skaianet;

import com.mraof.minestuck.player.IdentifierHandler;
import com.mraof.minestuck.player.PlayerIdentifier;
import net.minecraft.nbt.CompoundTag;

import javax.annotation.Nonnull;
import java.util.Objects;

public final class SburbConnection
{
	final SkaianetHandler skaianet;
	
	@Nonnull
	private final PlayerIdentifier clientIdentifier;
	@Nonnull
	private PlayerIdentifier serverIdentifier;
	
	SburbConnection(PlayerIdentifier client, PlayerIdentifier server, SkaianetHandler skaianet)
	{
		clientIdentifier = Objects.requireNonNull(client);
		serverIdentifier = Objects.requireNonNull(server);
		this.skaianet = skaianet;
	}
	
	SburbConnection(CompoundTag nbt, SkaianetHandler skaianet)
	{
		this.skaianet = skaianet;
		clientIdentifier = IdentifierHandler.load(nbt, "client");
		serverIdentifier = IdentifierHandler.load(nbt, "server");
	}
	
	CompoundTag write()
	{
		CompoundTag nbt = new CompoundTag();
		
		getClientIdentifier().saveToNBT(nbt, "client");
		getServerIdentifier().saveToNBT(nbt, "server");
		
		return nbt;
	}
	
	@Nonnull
	public PlayerIdentifier getClientIdentifier()
	{
		return clientIdentifier;
	}
	
	@Nonnull
	public PlayerIdentifier getServerIdentifier()
	{
		return serverIdentifier;
	}
	
	public boolean hasServerPlayer()
	{
		return getServerIdentifier() != IdentifierHandler.NULL_IDENTIFIER;
	}
	
	void removeServerPlayer()
	{
		skaianet.getActiveConnection(this.getClientIdentifier()).ifPresent(skaianet::closeConnection);
		if(hasServerPlayer())
		{
			skaianet.infoTracker.markDirty(serverIdentifier);
			if(skaianet.getOrCreateData(this.clientIdentifier).hasEntered())
				skaianet.infoTracker.markLandChainDirty();
			
			Session session = skaianet.sessionHandler.getPlayerSession(this.getClientIdentifier());
			serverIdentifier = IdentifierHandler.NULL_IDENTIFIER;
			skaianet.sessionHandler.onConnectionChainBroken(session);
		}
	}
	
	void setNewServerPlayer(PlayerIdentifier server)
	{
		if(hasServerPlayer())
			throw new IllegalStateException("Connection already has a server player");
		if(skaianet.getPrimaryOrCandidateConnection(server, false).isPresent())
			throw new IllegalStateException("Server player already has a connection");
		skaianet.sessionHandler.prepareSessionFor(clientIdentifier, server);    //Make sure that it is fine to add the server here session-wise
		
		serverIdentifier = Objects.requireNonNull(server);
		skaianet.infoTracker.markDirty(serverIdentifier);
		if(skaianet.getOrCreateData(this.clientIdentifier).hasEntered())
			skaianet.infoTracker.markLandChainDirty();
	}
	
	@Deprecated
	public boolean isMain()
	{
		return skaianet.getOrCreateData(this.getClientIdentifier()).hasPrimaryConnection();
	}
}
