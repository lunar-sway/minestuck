package com.mraof.minestuck.network;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.client.ClientSpecibusData;
import com.mraof.minestuck.util.MSAttachments;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import java.util.ArrayList;

import java.util.List;

public record SyncSpecibusPacket(List<String> selected, int maxCount) implements MSPacket.PlayToClient
{
	public static final Type<SyncSpecibusPacket> ID = new Type<>(Minestuck.id("sync_specibus"));
	public static final StreamCodec<FriendlyByteBuf, SyncSpecibusPacket> STREAM_CODEC = StreamCodec.composite(
			ByteBufCodecs.collection(ArrayList::new, ByteBufCodecs.STRING_UTF8),
			SyncSpecibusPacket::selected,
			ByteBufCodecs.INT,
			SyncSpecibusPacket::maxCount,
			SyncSpecibusPacket::new
	);
	
	@Override
	public Type<? extends CustomPacketPayload> type() { return ID; }
	
	@Override
	public void execute(IPayloadContext context)
	{
		Minecraft mc = Minecraft.getInstance();
		if(mc.player != null)
		{
			mc.player.setData(MSAttachments.SELECTED_SPECIBUS, selected());
			ClientSpecibusData.maxCount = maxCount();
		}
	}
}