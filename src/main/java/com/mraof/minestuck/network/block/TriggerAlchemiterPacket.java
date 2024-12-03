package com.mraof.minestuck.network.block;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.blockentity.machine.AlchemiterBlockEntity;
import com.mraof.minestuck.network.MSPacket;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record TriggerAlchemiterPacket(int quantity, BlockPos pos) implements MSPacket.PlayToServer
{
	public static final Type<TriggerAlchemiterPacket> ID = new Type<>(Minestuck.id("trigger_alchemiter"));
	
	public static final StreamCodec<RegistryFriendlyByteBuf, TriggerAlchemiterPacket> STREAM_CODEC = StreamCodec.composite(
			ByteBufCodecs.INT,
			TriggerAlchemiterPacket::quantity,
			BlockPos.STREAM_CODEC,
			TriggerAlchemiterPacket::pos,
			TriggerAlchemiterPacket::new
	);
	
	@Override
	public void execute(IPayloadContext context, ServerPlayer player)
	{
		MSPacket.getAccessibleBlockEntity(player, this.pos, AlchemiterBlockEntity.class)
				.ifPresent(alchemiter -> alchemiter.processContents(quantity, player));
	}
	
	@Override
	public Type<? extends CustomPacketPayload> type()
	{
		return ID;
	}
}
