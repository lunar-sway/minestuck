package com.mraof.minestuck.network;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public interface PlayToServerPacket
{
	default void consume(Supplier<NetworkEvent.Context> ctx)
	{
		if(ctx.get().getDirection() == NetworkDirection.PLAY_TO_SERVER)
			ctx.get().enqueueWork(() -> this.execute(ctx.get().getSender()));
		
		ctx.get().setPacketHandled(true);
	}
	
	void execute(ServerPlayerEntity player);
}