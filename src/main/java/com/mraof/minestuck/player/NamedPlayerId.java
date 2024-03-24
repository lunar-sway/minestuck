package com.mraof.minestuck.player;

import net.minecraft.network.FriendlyByteBuf;

/**
 * References a {@link PlayerIdentifier} by number id paired with the username of the related player.
 * The number id is a suitable reference over network/at client-side,
 * but should not persist across game sessions as the id mapping is rebuilt with every new game session.
 */
public record NamedPlayerId(int id, String name)
{
	public static NamedPlayerId of(PlayerIdentifier player)
	{
		return new NamedPlayerId(player.getId(), player.getUsername());
	}
	
	public void write(FriendlyByteBuf buffer)
	{
		buffer.writeInt(this.id);
		buffer.writeUtf(this.name, 16);
	}
	
	public static NamedPlayerId read(FriendlyByteBuf buffer)
	{
		int id = buffer.readInt();
		String name = buffer.readUtf(16);
		return new NamedPlayerId(id, name);
	}
}
