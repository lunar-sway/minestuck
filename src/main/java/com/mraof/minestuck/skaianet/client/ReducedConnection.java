package com.mraof.minestuck.skaianet.client;

import com.mraof.minestuck.player.NamedPlayerId;
import com.mraof.minestuck.skaianet.SburbConnection;
import net.minecraft.network.FriendlyByteBuf;

/**
 * The client side version of {@link SburbConnection}
 */
public record ReducedConnection(NamedPlayerId client, NamedPlayerId server, boolean isActive)
{
	public ReducedConnection(SburbConnection connection)
	{
		this(NamedPlayerId.of(connection.getClientIdentifier()), NamedPlayerId.of(connection.getServerIdentifier()),
				connection.isActive());
	}
	
	public static ReducedConnection read(FriendlyByteBuf buffer)
	{
		boolean isActive = buffer.readBoolean();
		
		NamedPlayerId client = NamedPlayerId.read(buffer);
		NamedPlayerId server = NamedPlayerId.read(buffer);
		return new ReducedConnection(client, server, isActive);
	}
	
	public void write(FriendlyByteBuf buffer)
	{
		buffer.writeBoolean(isActive);
		
		client.write(buffer);
		server.write(buffer);
	}
}
