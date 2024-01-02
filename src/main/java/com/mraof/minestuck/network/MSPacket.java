package com.mraof.minestuck.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public interface MSPacket
{
	void encode(FriendlyByteBuf buffer);
	
	void consume(Supplier<NetworkEvent.Context> ctx);
	
	@Deprecated	// Generally bad design to write multi-purpose packets. Such a packet should *generally* be written as several types of packets instead.
	interface PlayToBoth extends MSPacket
	{
		@Override
		default void consume(Supplier<NetworkEvent.Context> ctx)
		{
			if(ctx.get().getDirection() == NetworkDirection.PLAY_TO_SERVER)
				ctx.get().enqueueWork(() -> this.execute(ctx.get().getSender()));
			else if(ctx.get().getDirection() == NetworkDirection.PLAY_TO_CLIENT)
				ctx.get().enqueueWork(this::execute);
			
			ctx.get().setPacketHandled(true);
		}
		
		void execute();
		
		void execute(ServerPlayer player);
	}
	
	interface PlayToClient extends MSPacket
	{
		@Override
		default void consume(Supplier<NetworkEvent.Context> ctx)
		{
			if(ctx.get().getDirection() == NetworkDirection.PLAY_TO_CLIENT)
				ctx.get().enqueueWork(this::execute);
			
			ctx.get().setPacketHandled(true);
		}
		
		void execute();
	}
	
	interface PlayToServer extends MSPacket
	{
		@Override
		default void consume(Supplier<NetworkEvent.Context> ctx)
		{
			if(ctx.get().getDirection() == NetworkDirection.PLAY_TO_SERVER)
				ctx.get().enqueueWork(() -> this.execute(ctx.get().getSender()));
			
			ctx.get().setPacketHandled(true);
		}
		
		void execute(ServerPlayer player);
	}
}
