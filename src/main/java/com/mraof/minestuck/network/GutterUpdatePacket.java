package com.mraof.minestuck.network;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.api.alchemy.GristSet;
import com.mraof.minestuck.player.ClientPlayerData;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record GutterUpdatePacket(GristSet.Immutable gristValue, long remainingCapacity) implements MSPacket.PlayToClient
{
	public static final Type<GutterUpdatePacket> ID = new Type<>(Minestuck.id("gutter_update"));
	public static final StreamCodec<RegistryFriendlyByteBuf, GutterUpdatePacket> STREAM_CODEC = StreamCodec.composite(
			GristSet.Codecs.STREAM_CODEC,
			GutterUpdatePacket::gristValue,
			ByteBufCodecs.VAR_LONG,
			GutterUpdatePacket::remainingCapacity,
			GutterUpdatePacket::new
	);
	
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
}