package com.mraof.minestuck.network;

import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.handling.DirectionalPayloadHandler;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.neoforged.neoforge.network.handling.IPlayPayloadHandler;
import net.neoforged.neoforge.network.registration.IDirectionAwarePayloadHandlerBuilder;

public interface MSPacket
{
	@Deprecated	// Generally bad design to write multi-purpose packets. Such a packet should *generally* be written as several types of packets instead.
	interface PlayToBoth extends PlayToClient, PlayToServer
	{
		void execute();
		
		void execute(ServerPlayer player);
		
		static <P extends PlayToBoth> void handlerBoth(IDirectionAwarePayloadHandlerBuilder<P, IPlayPayloadHandler<P>> builder)
		{
			PlayToClient.handler(builder);
			PlayToServer.handler(builder);
		}
	}
	
	interface PlayToClient extends CustomPacketPayload
	{
		void execute();
		
		static <P extends PlayToClient> void handler(DirectionalPayloadHandler<P, IPlayPayloadHandler<P>> builder)
		{
			builder.client((payload, context) -> context.workHandler().execute(payload::execute));
		}
	}
	
	interface PlayToServer extends CustomPacketPayload
	{
		void execute(ServerPlayer player);
		
		static <P extends PlayToServer> void handler(IDirectionAwarePayloadHandlerBuilder<P, IPlayPayloadHandler<P>> builder)
		{
			builder.server((payload, context) -> context.workHandler().execute(() -> {
				if(context.player().isPresent() && context.player().get() instanceof ServerPlayer serverPlayer)
					payload.execute(serverPlayer);
			}));
		}
	}
}
