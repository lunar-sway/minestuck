package com.mraof.minestuck.network;

import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public interface StandardPacket
{
	void encode(PacketBuffer buffer);
	
	void consume(Supplier<NetworkEvent.Context> ctx);
}