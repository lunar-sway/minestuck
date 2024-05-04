package com.mraof.minestuck.network.data;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.network.MSPacket;
import com.mraof.minestuck.player.ClientPlayerData;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

public class BoondollarDataPacket implements MSPacket.PlayToClient
{
	public static final ResourceLocation ID = Minestuck.id("boondollar_data");
	
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
	public ResourceLocation id()
	{
		return ID;
	}
	
	@Override
	public void write(FriendlyByteBuf buffer)
	{
		buffer.writeLong(count);
	}
	
	public static BoondollarDataPacket read(FriendlyByteBuf buffer)
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