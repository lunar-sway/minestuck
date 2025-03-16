package com.mraof.minestuck.skaianet.client;

import it.unimi.dsi.fastutil.ints.Int2ObjectArrayMap;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

import java.util.HashMap;
import java.util.Map;

public record ReducedPlayerState(boolean isClientResuming, boolean isServerResuming,
								 boolean hasPrimaryConnectionAsClient, boolean hasPrimaryConnectionAsServer,
								 Map<Integer, String> openServers)
{
	public static final StreamCodec<FriendlyByteBuf, ReducedPlayerState> STREAM_CODEC = StreamCodec.composite(
			ByteBufCodecs.BOOL,
			ReducedPlayerState::isClientResuming,
			ByteBufCodecs.BOOL,
			ReducedPlayerState::isServerResuming,
			ByteBufCodecs.BOOL,
			ReducedPlayerState::hasPrimaryConnectionAsClient,
			ByteBufCodecs.BOOL,
			ReducedPlayerState::hasPrimaryConnectionAsServer,
			ByteBufCodecs.map(Int2ObjectArrayMap::new, ByteBufCodecs.INT, ByteBufCodecs.STRING_UTF8),
			ReducedPlayerState::openServers,
			ReducedPlayerState::new
	);
}
