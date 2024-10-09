package com.mraof.minestuck.network;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.player.ClientPlayerData;
import com.mraof.minestuck.player.Title;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record TitleDataPacket(Title title) implements MSPacket.PlayToClient
{
	public static final Type<TitleDataPacket> ID = new Type<>(Minestuck.id("title_data"));
	public static final StreamCodec<FriendlyByteBuf, TitleDataPacket> STREAM_CODEC = Title.STREAM_CODEC.map(TitleDataPacket::new, TitleDataPacket::title);
	
	public static TitleDataPacket create(Title title)
	{
		return new TitleDataPacket(title);
	}
	
	@Override
	public Type<? extends CustomPacketPayload> type()
	{
		return ID;
	}
	
	@Override
	public void execute(IPayloadContext context)
	{
		ClientPlayerData.handleDataPacket(this);
	}
	
	public Title getTitle()
	{
		return title;
	}
}
