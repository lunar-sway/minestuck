package com.mraof.minestuck.network;

import net.minecraft.core.BlockPos;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.network.handling.IPlayPayloadHandler;
import net.neoforged.neoforge.network.registration.IDirectionAwarePayloadHandlerBuilder;

import java.util.Optional;

public final class MSPacket
{
	@SuppressWarnings("resource")
	public static <T> Optional<T> getAccessibleBlockEntity(ServerPlayer player, BlockPos pos, Class<T> castClass)
	{
		if(!player.level().isAreaLoaded(pos, 0) || player.distanceToSqr(Vec3.atCenterOf(pos)) > 8 * 8)
			return Optional.empty();
		
		BlockEntity blockEntity = player.level().getBlockEntity(pos);
		if(!castClass.isInstance(blockEntity))
			return Optional.empty();
		
		return Optional.of(castClass.cast(blockEntity));
	}
	
	public interface PlayToClient extends CustomPacketPayload
	{
		void execute();
		
		static <P extends PlayToClient> void handler(IDirectionAwarePayloadHandlerBuilder<P, IPlayPayloadHandler<P>> builder)
		{
			builder.client((payload, context) -> context.workHandler().execute(payload::execute));
		}
	}
	
	public interface PlayToServer extends CustomPacketPayload
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
