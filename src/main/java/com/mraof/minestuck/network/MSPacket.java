package com.mraof.minestuck.network;

import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.handling.IPlayPayloadHandler;
import net.neoforged.neoforge.network.registration.IDirectionAwarePayloadHandlerBuilder;

public interface MSPacket
{
	interface PlayToClient extends CustomPacketPayload
	{
		void execute();
		
		static <P extends PlayToClient> void handler(IDirectionAwarePayloadHandlerBuilder<P, IPlayPayloadHandler<P>> builder)
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
