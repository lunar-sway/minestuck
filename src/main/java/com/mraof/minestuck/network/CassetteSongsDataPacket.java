package com.mraof.minestuck.network;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.inventory.musicplayer.CassetteSong;
import com.mraof.minestuck.inventory.musicplayer.CassetteSongs;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import java.util.List;

public record CassetteSongsDataPacket(List<ResourceLocation> locations,
									  List<CassetteSong> songs) implements MSPacket.PlayToClient
{
	public static final Type<CassetteSongsDataPacket> ID = new Type<>(Minestuck.id("cassette_songs_data"));
	public static final StreamCodec<RegistryFriendlyByteBuf, CassetteSongsDataPacket> STREAM_CODEC = StreamCodec.composite(
			ResourceLocation.STREAM_CODEC.apply(ByteBufCodecs.list()), CassetteSongsDataPacket::locations,
			CassetteSong.STREAM_CODEC.apply(ByteBufCodecs.list()), CassetteSongsDataPacket::songs,
			CassetteSongsDataPacket::new
	);
	
	@Override
	public Type<? extends CustomPacketPayload> type()
	{
		return ID;
	}
	
	@Override
	public void execute(IPayloadContext context)
	{
		CassetteSongs.createfromLists(locations, songs);
	}
}
