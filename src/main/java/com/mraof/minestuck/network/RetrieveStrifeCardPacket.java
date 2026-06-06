package com.mraof.minestuck.network;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.strife.StrifePortfolioHandler;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record RetrieveStrifeCardPacket(int index) implements MSPacket.PlayToServer
{
	public static final Type<RetrieveStrifeCardPacket> ID = new Type<>(Minestuck.id("retrieve_strife_card"));
	
	public static final StreamCodec<ByteBuf, RetrieveStrifeCardPacket> STREAM_CODEC = ByteBufCodecs.INT.map(RetrieveStrifeCardPacket::new, RetrieveStrifeCardPacket::index);
	
	@Override
	public Type<? extends CustomPacketPayload> type()
	{
		return ID;
	}
	
	@Override
	public void execute(IPayloadContext context, ServerPlayer player)
	{
		StrifePortfolioHandler.retrieveCard(player, index());
	}
}