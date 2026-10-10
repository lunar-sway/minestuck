package com.mraof.minestuck.network;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.client.MeteorClientHandler;
import com.mraof.minestuck.client.MeteorScreenShake;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public final class MeteorPackets
{
	
	/**
	 * Sent to all clients when a new countdown starts.
	 */
	public record CountdownStart(String playerKey, BlockPos cruxtruderPos, ResourceKey<Level> levelKey,
								 float meteorSize, int meteorEntityId,
								 int ticksElapsed) implements MSPacket.PlayToClient
	{
		public static final Type<CountdownStart> ID = new Type<>(Minestuck.id("meteor/countdown_start"));
		
		public static final StreamCodec<RegistryFriendlyByteBuf, CountdownStart> STREAM_CODEC = StreamCodec.composite(ByteBufCodecs.STRING_UTF8, CountdownStart::playerKey, BlockPos.STREAM_CODEC, CountdownStart::cruxtruderPos, ResourceKey.streamCodec(Registries.DIMENSION), CountdownStart::levelKey, ByteBufCodecs.FLOAT, CountdownStart::meteorSize, ByteBufCodecs.INT, CountdownStart::meteorEntityId, ByteBufCodecs.INT, CountdownStart::ticksElapsed, CountdownStart::new);
		
		@Override
		public Type<? extends CustomPacketPayload> type()
		{
			return ID;
		}
		
		@Override
		public void execute(IPayloadContext context)
		{
			MeteorClientHandler.onCountdownStart(this);
		}
	}
	
	/**
	 * Sent every 10 ticks to update meteor visual position on clients.
	 */
	public record MeteorPosition(int entityId, int ticksElapsed) implements MSPacket.PlayToClient
	{
		
		public static final Type<MeteorPosition> ID = new Type<>(Minestuck.id("meteor/position"));
		
		public static final StreamCodec<FriendlyByteBuf, MeteorPosition> STREAM_CODEC = StreamCodec.composite(ByteBufCodecs.INT, MeteorPosition::entityId, ByteBufCodecs.INT, MeteorPosition::ticksElapsed, MeteorPosition::new);
		
		@Override
		public Type<? extends CustomPacketPayload> type()
		{
			return ID;
		}
		
		@Override
		public void execute(IPayloadContext context)
		{
			MeteorClientHandler.onMeteorPosition(this);
		}
	}
	
	public record MiniMeteorImpact(double x, double y, double z) implements MSPacket.PlayToClient
	{
		public static final Type<MiniMeteorImpact> ID = new Type<>(Minestuck.id("meteor/mini_impact"));
		
		public static final StreamCodec<FriendlyByteBuf, MiniMeteorImpact> STREAM_CODEC = StreamCodec.composite(ByteBufCodecs.DOUBLE, MiniMeteorImpact::x, ByteBufCodecs.DOUBLE, MiniMeteorImpact::y, ByteBufCodecs.DOUBLE, MiniMeteorImpact::z, MiniMeteorImpact::new);
		
		@Override
		public Type<? extends CustomPacketPayload> type()
		{
			return ID;
		}
		
		@Override
		public void execute(IPayloadContext context)
		{
			var player = context.player();
			if(player == null) return;
			
			double dx = player.getX() - x;
			double dy = player.getY() - y;
			double dz = player.getZ() - z;
			double distance = Math.sqrt(dx * dx + dy * dy + dz * dz);
			
			MeteorScreenShake.onMiniMeteorImpactNearby(distance);
		}
	}
	
	/**
	 * Sent when the meteor is removed (impact or cancelled).
	 */
	public record MeteorRemoved(int entityId) implements MSPacket.PlayToClient
	{
		
		public static final Type<MeteorRemoved> ID = new Type<>(Minestuck.id("meteor/removed"));
		
		public static final StreamCodec<ByteBuf, MeteorRemoved> STREAM_CODEC = ByteBufCodecs.INT.map(MeteorRemoved::new, MeteorRemoved::entityId);
		
		@Override
		public Type<? extends CustomPacketPayload> type()
		{
			return ID;
		}
		
		@Override
		public void execute(IPayloadContext context)
		{
			MeteorClientHandler.onMeteorRemoved(entityId);
		}
	}
	
	/**
	 * Sent to the owner player to start/stop the meteor music.
	 */
	public record PlayMeteorMusic(boolean play) implements MSPacket.PlayToClient
	{
		
		public static final Type<PlayMeteorMusic> ID = new Type<>(Minestuck.id("meteor/music"));
		
		public static final StreamCodec<ByteBuf, PlayMeteorMusic> STREAM_CODEC = ByteBufCodecs.BOOL.map(PlayMeteorMusic::new, PlayMeteorMusic::play);
		
		@Override
		public Type<? extends CustomPacketPayload> type()
		{
			return ID;
		}
		
		@Override
		public void execute(IPayloadContext context)
		{
			MeteorClientHandler.onMusicTrigger(play);
		}
	}
}