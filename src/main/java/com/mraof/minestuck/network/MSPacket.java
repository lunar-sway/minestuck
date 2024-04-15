package com.mraof.minestuck.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.NetworkEvent;
import net.neoforged.neoforge.network.PlayNetworkDirection;

public interface MSPacket
{
	void encode(FriendlyByteBuf buffer);
	
	void consume(NetworkEvent.Context ctx);
	
	@Deprecated	// Generally bad design to write multi-purpose packets. Such a packet should *generally* be written as several types of packets instead.
	interface PlayToBoth extends MSPacket
	{
		@Override
		default void consume(NetworkEvent.Context ctx)
		{
			if(ctx.getDirection() == PlayNetworkDirection.PLAY_TO_SERVER)
				ctx.enqueueWork(() -> this.execute(ctx.getSender()));
			else if(ctx.getDirection() == PlayNetworkDirection.PLAY_TO_CLIENT)
				ctx.enqueueWork(this::execute);
			
			ctx.setPacketHandled(true);
		}
		
		void execute();
		
		void execute(ServerPlayer player);
	}
	
	interface PlayToClient extends MSPacket
	{
		@Override
		default void consume(NetworkEvent.Context ctx)
		{
			if(ctx.getDirection() == PlayNetworkDirection.PLAY_TO_CLIENT)
				ctx.enqueueWork(this::execute);
			
			ctx.setPacketHandled(true);
		}
		
		void execute();
	}
	
	interface PlayToServer extends MSPacket
	{
		@Override
		default void consume(NetworkEvent.Context ctx)
		{
			if(ctx.getDirection() == PlayNetworkDirection.PLAY_TO_SERVER)
				ctx.enqueueWork(() -> this.execute(ctx.getSender()));
			
			ctx.setPacketHandled(true);
		}
		
		void execute(ServerPlayer player);
	}
}
