package com.mraof.minestuck.network;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.client.gui.MSScreenFactories;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.InteractionHand;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record OpenStrifeCardGuiPacket(InteractionHand hand) implements MSPacket.PlayToClient
{
	public static final Type<OpenStrifeCardGuiPacket> ID = new Type<>(Minestuck.id("open_strife_card_gui"));
	
	public static final StreamCodec<FriendlyByteBuf, OpenStrifeCardGuiPacket> STREAM_CODEC = StreamCodec.composite(ByteBufCodecs.BOOL.map(b -> b ? InteractionHand.MAIN_HAND : InteractionHand.OFF_HAND, h -> h == InteractionHand.MAIN_HAND), OpenStrifeCardGuiPacket::hand, OpenStrifeCardGuiPacket::new);
	
	@Override
	public Type<? extends CustomPacketPayload> type()
	{
		return ID;
	}
	
	@Override
	public void execute(IPayloadContext context)
	{
		MSScreenFactories.displayStrifeCardScreen(hand());
	}
}