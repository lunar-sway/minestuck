package com.mraof.minestuck.network.editmode;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.network.MSPacket;
import com.mraof.minestuck.player.ClientPlayerData;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record EditmodeCacheLimitPacket(long limit) implements MSPacket.PlayToClient
{
	
	public static final Type<EditmodeCacheLimitPacket> ID = new Type<>(Minestuck.id("editmode_cache_limit"));
	public static final StreamCodec<FriendlyByteBuf, EditmodeCacheLimitPacket> STREAM_CODEC = StreamCodec.composite(
			ByteBufCodecs.VAR_LONG,
			EditmodeCacheLimitPacket::limit,
			EditmodeCacheLimitPacket::new
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
