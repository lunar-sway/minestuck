package com.mraof.minestuck.network;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.api.alchemy.GristSet;
import com.mraof.minestuck.api.alchemy.ImmutableGristSet;
import com.mraof.minestuck.player.ClientPlayerData;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.codec.NeoForgeStreamCodecs;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record GristCachePacket(ImmutableGristSet gristCache, ClientPlayerData.CacheSource cacheSource) implements MSPacket.PlayToClient
{
	
	public static final Type<GristCachePacket> ID = new Type<>(Minestuck.id("grist_cache"));
	public static final StreamCodec<RegistryFriendlyByteBuf, GristCachePacket> STREAM_CODEC = StreamCodec.composite(
			GristSet.IMMUTABLE_STREAM_CODEC,
			GristCachePacket::gristCache,
			NeoForgeStreamCodecs.enumCodec(ClientPlayerData.CacheSource.class),
			GristCachePacket::cacheSource,
			GristCachePacket::new
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
