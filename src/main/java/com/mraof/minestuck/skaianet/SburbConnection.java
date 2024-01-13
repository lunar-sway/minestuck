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
	
	SburbConnection(PlayerIdentifier client, SkaianetHandler skaianet)
	{
		clientIdentifier = Objects.requireNonNull(client);
		this.skaianet = skaianet;
	}
	
	SburbConnection(CompoundTag nbt, SkaianetHandler skaianet)
	{
		this.skaianet = skaianet;
		clientIdentifier = IdentifierHandler.load(nbt, "client");
	}
	
	CompoundTag write()
	{
		CompoundTag nbt = new CompoundTag();
		
		getClientIdentifier().saveToNBT(nbt, "client");
		
		return nbt;
	}
	
	@Nonnull
	public PlayerIdentifier getClientIdentifier()
	{
		return clientIdentifier;
	}
}
