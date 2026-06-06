package com.mraof.minestuck.network;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.strife.StrifePortfolioHandler;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record SetActiveStrifePacket(int specibusIndex) implements MSPacket.PlayToServer
{
	public static final Type<SetActiveStrifePacket> ID = new Type<>(Minestuck.id("set_active_strife"));
	
	public static final StreamCodec<ByteBuf, SetActiveStrifePacket> STREAM_CODEC = ByteBufCodecs.INT.map(SetActiveStrifePacket::new, SetActiveStrifePacket::specibusIndex);
	
	@Override
	public Type<? extends CustomPacketPayload> type()
	{
		return ID;
	}
	
	@Override
	public void execute(IPayloadContext context, ServerPlayer player)
	{
		if(specibusIndex() < 0 || specibusIndex() >= StrifePortfolioHandler.getData(player).getPortfolio().length)
			return;
		StrifePortfolioHandler.setSelectedSpecibus(player, specibusIndex());
	}
}