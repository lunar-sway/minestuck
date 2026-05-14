package com.mraof.minestuck.network;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.MinestuckConfig;
import com.mraof.minestuck.player.KindAbstratusList;
import com.mraof.minestuck.util.MSAttachments;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import java.util.ArrayList;
import java.util.List;

public record SpecibusPacket(String specibusName) implements MSPacket.PlayToServer
{
	public static final Type<SpecibusPacket> ID = new Type<>(Minestuck.id("select_specibus"));
	public static final StreamCodec<FriendlyByteBuf, SpecibusPacket> STREAM_CODEC = StreamCodec.composite(
			ByteBufCodecs.STRING_UTF8, SpecibusPacket::specibusName,
			SpecibusPacket::new
	);
	
	@Override
	public Type<? extends CustomPacketPayload> type() { return ID; }
	
	@Override
	public void execute(IPayloadContext context, ServerPlayer player)
	{
		List<String> selected = new ArrayList<>(player.getData(MSAttachments.SELECTED_SPECIBUS));
		
		if(selected.size() >= MinestuckConfig.SERVER.maxSpecibusCount.get()) return;
		if (selected.contains(specibusName())) return;
		if (KindAbstratusList.getTypeFromName(specibusName()) == null) return;
		
		selected.add(specibusName());
		player.setData(MSAttachments.SELECTED_SPECIBUS, selected);
		
		context.reply(new SyncSpecibusPacket(selected, MinestuckConfig.SERVER.maxSpecibusCount.get()));
	}
}