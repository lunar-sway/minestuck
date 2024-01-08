package com.mraof.minestuck.skaianet;

import com.mraof.minestuck.player.IdentifierHandler;
import com.mraof.minestuck.player.PlayerIdentifier;
import net.minecraft.nbt.CompoundTag;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Objects;

public final class SburbConnection
{
	final SkaianetHandler skaianet;
	
	@Nonnull
	private final PlayerIdentifier clientIdentifier;
	@Nonnull
	private PlayerIdentifier serverIdentifier;
	private boolean isMain;
	
	SburbConnection(PlayerIdentifier client, PlayerIdentifier server, SkaianetHandler skaianet)
	{
		clientIdentifier = Objects.requireNonNull(client);
		serverIdentifier = Objects.requireNonNull(server);
		this.skaianet = skaianet;
	}
	
	SburbConnection(CompoundTag nbt, SkaianetHandler skaianet)
	{
		this.skaianet = skaianet;
		isMain = nbt.getBoolean("IsMain");
		clientIdentifier = IdentifierHandler.load(nbt, "client");
		serverIdentifier = IdentifierHandler.load(nbt, "server");
	}
	
	CompoundTag write()
	{
		CompoundTag nbt = new CompoundTag();
		nbt.putBoolean("IsMain", isMain);
		
		getClientIdentifier().saveToNBT(nbt, "client");
		getServerIdentifier().saveToNBT(nbt, "server");
		
		return nbt;
	}
	
	public Session getSession()
	{
		return skaianet.sessionHandler.getPlayerSession(this.getClientIdentifier());
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
	
	void closeIfActive()
	{
		ActiveConnection connection = this.getActiveConnection();
		if(connection != null)
			skaianet.closeConnection(connection);
	}
	
	void removeServerPlayer()
	{
		if(hasServerPlayer())
		{
			skaianet.infoTracker.markDirty(serverIdentifier);
			if(skaianet.getOrCreateData(this.clientIdentifier).hasEntered())
				skaianet.infoTracker.markLandChainDirty();
			
			Session session = this.getSession();
			serverIdentifier = IdentifierHandler.NULL_IDENTIFIER;
			skaianet.sessionHandler.onConnectionChainBroken(session);
		}
	}
	
	void setNewServerPlayer(PlayerIdentifier server) throws MergeResult.SessionMergeException
	{
		if(hasServerPlayer())
			throw new IllegalStateException("Connection already has server player");
		if(skaianet.getPrimaryOrCandidateConnection(server, false).isPresent())
			throw MergeResult.GENERIC_FAIL.exception();
		skaianet.sessionHandler.prepareSessionFor(clientIdentifier, server);    //Make sure that it is fine to add the server here session-wise
		
		serverIdentifier = Objects.requireNonNull(server);
		skaianet.infoTracker.markDirty(serverIdentifier);
		if(skaianet.getOrCreateData(this.clientIdentifier).hasEntered())
			skaianet.infoTracker.markLandChainDirty();
	}
	
	@Nullable
	public ActiveConnection getActiveConnection()
	{
		return skaianet.activeConnections.stream()
				.filter(c -> c.client().equals(this.getClientIdentifier()) && c.server().equals(this.getServerIdentifier()))
				.findAny().orElse(null);
	}
	
	public boolean isMain()
	{
		return isMain;
	}
	
	@Deprecated
	public boolean isActive()
	{
		return getActiveConnection() != null;
	}
	
	void setIsMain()
	{
		if(!isMain)
		{
			isMain = true;
			skaianet.infoTracker.markDirty(this);
		}
	}
	
}
