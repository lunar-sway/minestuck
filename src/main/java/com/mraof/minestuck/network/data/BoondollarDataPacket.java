package com.mraof.minestuck.network.data;

import com.mraof.minestuck.network.PlayToClientPacket;
import com.mraof.minestuck.player.ClientPlayerData;
import net.minecraft.network.FriendlyByteBuf;

public class BoondollarDataPacket implements PlayToClientPacket
{
	private final long count;
	
	private BoondollarDataPacket(long count)
	{
		this.count = count;
	}
	
	public static BoondollarDataPacket create(long count)
	{
		return new BoondollarDataPacket(count);
	}
	
	@Override
	public void encode(FriendlyByteBuf buffer)
	{
		buffer.writeLong(count);
	}
	
	public static BoondollarDataPacket decode(FriendlyByteBuf buffer)
	{
		long count = buffer.readLong();
		return create(count);
	}
	
	@Override
	public void execute()
	{
		ClientPlayerData.handleDataPacket(this);
	}
	
	public long getBoondollars()
	{
		return count;
	}
}