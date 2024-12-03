package com.mraof.minestuck.network;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.client.EntryEffect;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.codec.StreamEncoder;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public final class EntryEffectPackets
{
	public record Effect(ResourceKey<Level> level, BlockPos center, int range) implements MSPacket.PlayToClient
	{
		public static final Type<Effect> ID = new Type<>(Minestuck.id("entry_effect/effect"));
		
		public static final StreamCodec<RegistryFriendlyByteBuf, Effect> STREAM_CODEC = StreamCodec.composite(
				ResourceKey.streamCodec(Registries.DIMENSION),
				Effect::level,
				BlockPos.STREAM_CODEC,
				Effect::center,
				ByteBufCodecs.INT,
				Effect::range,
				Effect::new
		);
		
		@Override
		public Type<? extends CustomPacketPayload> type()
		{
			return ID;
		}
		
		@Override
		public void execute(IPayloadContext context)
		{
			EntryEffect.handlePacket(this);
		}
	}
	
	public record Clear() implements MSPacket.PlayToClient
	{
		public static final Type<Clear> ID = new Type<>(Minestuck.id("entry_effect/clear"));
		public static final StreamCodec<FriendlyByteBuf, Clear> STREAM_CODEC = StreamCodec.unit(new Clear());
		
		@Override
		public Type<? extends CustomPacketPayload> type()
		{
			return ID;
		}
		
		@Override
		public void execute(IPayloadContext context)
		{
			EntryEffect.reset();
		}
	}
}
