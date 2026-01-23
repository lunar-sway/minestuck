package com.mraof.minestuck.network;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.client.ClientRungData;
import com.mraof.minestuck.player.Rung;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import java.util.List;

public record RungDisplayDataPacket(List<Rung.DisplayData> rungList) implements MSPacket.PlayToClient
{
	public static final Type<RungDisplayDataPacket> ID = new Type<>(Minestuck.id("rung_display_data"));
	public static final StreamCodec<FriendlyByteBuf, RungDisplayDataPacket> STREAM_CODEC = Rung.DisplayData.STREAM_CODEC.apply(ByteBufCodecs.list())
			.map(RungDisplayDataPacket::new, RungDisplayDataPacket::rungList);
	
	@Override
	public void execute(IPayloadContext context)
	{
		ClientRungData.onPacket(this);
	}
	
	@Override
	public Type<RungDisplayDataPacket> type()
	{
		return ID;
	}
}
