package com.mraof.minestuck.player;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

/**
 * References a {@link PlayerIdentifier} by number id paired with the username of the related player.
 * The number id is a suitable reference over network/at client-side,
 * but should not persist across game sessions as the id mapping is rebuilt with every new game session.
 */
public record NamedPlayerId(int id, String name)
{
	public static final StreamCodec<FriendlyByteBuf, NamedPlayerId> STREAM_CODEC = StreamCodec.composite(
			ByteBufCodecs.INT,
			NamedPlayerId::id,
			ByteBufCodecs.STRING_UTF8,
			NamedPlayerId::name,
			NamedPlayerId::new
	);
	
	public static NamedPlayerId of(PlayerIdentifier player)
	{
		return new NamedPlayerId(player.getId(), player.getUsername());
	}
}
