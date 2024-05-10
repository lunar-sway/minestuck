package com.mraof.minestuck.network;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.player.ClientPlayerData;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

public record BoondollarDataPacket(long amount) implements MSPacket.PlayToClient
{
	public static final ResourceLocation ID = Minestuck.id("boondollar_data");
	
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
		buffer.writeLong(amount);
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
}
