package com.mraof.minestuck.network;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.strife.StrifePortfolioHandler;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record SwapOffhandStrifePacket(int specibusIndex, int weaponIndex) implements MSPacket.PlayToServer
{
	public static final Type<SwapOffhandStrifePacket> ID = new Type<>(Minestuck.id("swap_offhand_strife"));
	
	public static final StreamCodec<FriendlyByteBuf, SwapOffhandStrifePacket> STREAM_CODEC = StreamCodec.composite(ByteBufCodecs.INT, SwapOffhandStrifePacket::specibusIndex, ByteBufCodecs.INT, SwapOffhandStrifePacket::weaponIndex, SwapOffhandStrifePacket::new);
	
	@Override
	public Type<? extends CustomPacketPayload> type()
	{
		return ID;
	}
	
	@Override
	public void execute(IPayloadContext context, ServerPlayer player)
	{
		StrifePortfolioHandler.swapOffhandWeapon(player, specibusIndex(), weaponIndex());
	}
}