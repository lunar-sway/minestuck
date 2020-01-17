package com.mraof.minestuck.network;

import com.mraof.minestuck.world.storage.ClientPlayerData;
import net.minecraft.network.PacketBuffer;

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
	public void encode(PacketBuffer buffer)
	{
		buffer.writeLong(count);
	}
	
	public static BoondollarDataPacket decode(PacketBuffer buffer)
	{
		long count = buffer.readLong();
		return create(count);
	}
	
	@Override
	public void execute()
	{
		ClientPlayerData.boondollars = count;
	}
}