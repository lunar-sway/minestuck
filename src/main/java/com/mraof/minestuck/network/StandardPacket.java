package com.mraof.minestuck.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public interface StandardPacket
{
	void encode(FriendlyByteBuf buffer);
	
	void consume(Supplier<NetworkEvent.Context> ctx);
}