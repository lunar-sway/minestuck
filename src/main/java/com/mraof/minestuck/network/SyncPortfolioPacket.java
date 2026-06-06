package com.mraof.minestuck.network;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.player.StrifePortfolioData;
import com.mraof.minestuck.util.MSAttachments;
import net.minecraft.client.Minecraft;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record SyncPortfolioPacket(StrifePortfolioData portfolioData) implements MSPacket.PlayToClient
{
	public static final Type<SyncPortfolioPacket> ID = new Type<>(Minestuck.id("sync_portfolio"));
	
	public static final StreamCodec<RegistryFriendlyByteBuf, SyncPortfolioPacket> STREAM_CODEC = StrifePortfolioData.STREAM_CODEC.map(SyncPortfolioPacket::new, SyncPortfolioPacket::portfolioData);
	
	@Override
	public Type<? extends CustomPacketPayload> type()
	{
		return ID;
	}
	
	@Override
	public void execute(IPayloadContext context)
	{
		Minecraft mc = Minecraft.getInstance();
		if(mc.player != null) mc.player.setData(MSAttachments.STRIFE_PORTFOLIO.get(), portfolioData());
	}
}