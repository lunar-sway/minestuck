package com.mraof.minestuck.network;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.strife.StrifePortfolioHandler;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record AssignStrifePacket(InteractionHand hand) implements MSPacket.PlayToServer
{
	public static final Type<AssignStrifePacket> ID = new Type<>(Minestuck.id("assign_strife"));
	
	public static final StreamCodec<FriendlyByteBuf, AssignStrifePacket> STREAM_CODEC = StreamCodec.composite(ByteBufCodecs.BOOL.map(b -> b ? InteractionHand.MAIN_HAND : InteractionHand.OFF_HAND, h -> h == InteractionHand.MAIN_HAND), AssignStrifePacket::hand, AssignStrifePacket::new);
	
	@Override
	public Type<? extends CustomPacketPayload> type()
	{
		return ID;
	}
	
	@Override
	public void execute(IPayloadContext context, ServerPlayer player)
	{
		StrifePortfolioHandler.assignStrife(player, hand());
	}
}