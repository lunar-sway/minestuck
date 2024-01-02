package com.mraof.minestuck.network.data;

import com.mraof.minestuck.network.MSPacket;
import com.mraof.minestuck.player.ClientPlayerData;
import net.minecraft.network.FriendlyByteBuf;

public record EditmodeCacheLimitPacket(long limit) implements MSPacket.PlayToClient
{
	@Override
	public void encode(FriendlyByteBuf buffer)
	{
		buffer.writeLong(this.limit);
	}
	
	public static EditmodeCacheLimitPacket decode(FriendlyByteBuf buffer)
	{
		return new EditmodeCacheLimitPacket(buffer.readLong());
	}
	
	@Override
	public void execute()
	{
		ClientPlayerData.handleDataPacket(this);
	}
}
