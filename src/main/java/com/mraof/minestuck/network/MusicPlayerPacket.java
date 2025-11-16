package com.mraof.minestuck.network;

import java.util.Optional;

import javax.annotation.Nullable;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.client.sounds.PlayerMusicClientHandler;
import com.mraof.minestuck.inventory.musicplayer.CassetteSong;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.codec.NeoForgeStreamCodecs;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record MusicPlayerPacket(int entityID, Optional<CassetteSong> cassetteType, float volume, float pitch) implements MSPacket.PlayToClient
{
	public static final Type<MusicPlayerPacket> ID = new Type<>(Minestuck.id("music_player"));
	public static final StreamCodec<RegistryFriendlyByteBuf, MusicPlayerPacket> STREAM_CODEC = StreamCodec.composite(
			ByteBufCodecs.INT,
			MusicPlayerPacket::entityID,
			ByteBufCodecs.optional(CassetteSong.STREAM_CODEC),
			MusicPlayerPacket::cassetteType,
			ByteBufCodecs.FLOAT,
			MusicPlayerPacket::volume,
			ByteBufCodecs.FLOAT,
			MusicPlayerPacket::pitch,
			MusicPlayerPacket::new
	);
	
	/**
	 * Creates a packet to the client for starting a cassette music track played by a specified player, and stopping the previous music track played by that player if there was any.
	 * If the cassetteType NONE is given, no new track is played.
	 *
	 * @param entity     The player starting the music.
	 * @param cassetteType The cassette that is going to be played.
	 * @param volume The volume of the music. Default is 4.0F.
	 * @param pitch The pitch of the music, how fast should it be. Default is 1.0F.
	 * @see PlayerMusicClientHandler
	 */
	
	public static MusicPlayerPacket createPacket(Player entity, @Nullable CassetteSong cassetteType, float volume, float pitch)
	{
		return new MusicPlayerPacket(entity.getId(), Optional.ofNullable(cassetteType), volume, pitch);
	}
	
	@Override
	public Type<? extends CustomPacketPayload> type()
	{
		return ID;
	}
	
	
	@Override
	public void execute(IPayloadContext context)
	{
		PlayerMusicClientHandler.handlePacket(entityID, cassetteType, volume, pitch);
	}
}
