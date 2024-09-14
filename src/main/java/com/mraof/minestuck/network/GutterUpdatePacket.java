package com.mraof.minestuck.network;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.api.alchemy.GristSet;
import com.mraof.minestuck.api.alchemy.ImmutableGristSet;
import com.mraof.minestuck.player.ClientPlayerData;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

public record GutterUpdatePacket(GristSet gristValue, long remainingCapacity) implements MSPacket.PlayToClient
{
	public static final ResourceLocation ID = Minestuck.id("gutter_update");
	
	@Override
	public ResourceLocation id()
	{
		return ID;
	}
	
	@Override
	public void write(FriendlyByteBuf buffer)
	{
		GristSet.write(gristValue, buffer);
		buffer.writeLong(remainingCapacity);
	}
	
	public static GutterUpdatePacket read(FriendlyByteBuf buffer)
	{
		ImmutableGristSet gristValue = GristSet.read(buffer);
		long remainingCapacity = buffer.readLong();
		return new GutterUpdatePacket(gristValue, remainingCapacity);
	}
	
	@Override
	public void execute()
	{
		ClientPlayerData.handleDataPacket(this);
	}
}