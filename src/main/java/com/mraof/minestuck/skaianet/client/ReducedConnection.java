package com.mraof.minestuck.skaianet.client;

import com.mraof.minestuck.player.NamedPlayerId;
import com.mraof.minestuck.skaianet.ActiveConnection;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;

/**
 * The client side version of {@link ActiveConnection}
 */
public record ReducedConnection(NamedPlayerId client, NamedPlayerId server)
{
	public static final StreamCodec<FriendlyByteBuf, ReducedConnection> STREAM_CODEC = StreamCodec.composite(
			NamedPlayerId.STREAM_CODEC,
			ReducedConnection::client,
			NamedPlayerId.STREAM_CODEC,
			ReducedConnection::server,
			ReducedConnection::new
	);
	
	public ReducedConnection(ActiveConnection connection)
	{
		this(NamedPlayerId.of(connection.client()), NamedPlayerId.of(connection.server()));
	}
}
