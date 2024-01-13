package com.mraof.minestuck.skaianet.client;

import com.mraof.minestuck.player.NamedPlayerId;
import com.mraof.minestuck.skaianet.ActiveConnection;
import net.minecraft.network.FriendlyByteBuf;

/**
 * The client side version of {@link ActiveConnection}
 */
public record ReducedConnection(NamedPlayerId client, NamedPlayerId server)
{
	public ReducedConnection(ActiveConnection connection)
	{
		this(NamedPlayerId.of(connection.client()), NamedPlayerId.of(connection.server()));
	}
	
	public static ReducedConnection read(FriendlyByteBuf buffer)
	{
		NamedPlayerId client = NamedPlayerId.read(buffer);
		NamedPlayerId server = NamedPlayerId.read(buffer);
		return new ReducedConnection(client, server);
	}
	
	public void write(FriendlyByteBuf buffer)
	{
		client.write(buffer);
		server.write(buffer);
	}
}
