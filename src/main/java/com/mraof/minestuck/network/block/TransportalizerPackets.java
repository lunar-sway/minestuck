package com.mraof.minestuck.network.block;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.blockentity.TransportalizerBlockEntity;
import com.mraof.minestuck.network.MSPacket;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public final class TransportalizerPackets
{
	public record SetId(BlockPos pos, String selfId) implements MSPacket.PlayToServer
	{
		
		public static final Type<SetId> ID = new Type<>(Minestuck.id("transportalizer/set_id"));
		public static final StreamCodec<FriendlyByteBuf, SetId> STREAM_CODEC = StreamCodec.composite(
				BlockPos.STREAM_CODEC,
				SetId::pos,
				ByteBufCodecs.STRING_UTF8,
				SetId::selfId,
				SetId::new
		);
		
		@Override
		public Type<? extends CustomPacketPayload> type()
		{
			return ID;
		}
		
		@Override
		public void execute(IPayloadContext context, ServerPlayer player)
		{
			if(selfId.length() != 4)
				return;
			
			MSPacket.getAccessibleBlockEntity(player, this.pos, TransportalizerBlockEntity.class)
					.ifPresent(transportalizer -> transportalizer.trySetId(selfId, player));
		}
	}
	
	public record SetDestId(BlockPos pos, String destId) implements MSPacket.PlayToServer
	{
		public static final Type<SetDestId> ID = new Type<>(Minestuck.id("transportalizer/set_dest_id"));
		public static final StreamCodec<FriendlyByteBuf, SetDestId> STREAM_CODEC = StreamCodec.composite(
				BlockPos.STREAM_CODEC,
				SetDestId::pos,
				ByteBufCodecs.STRING_UTF8,
				SetDestId::destId,
				SetDestId::new
		);
		
		@Override
		public Type<? extends CustomPacketPayload> type()
		{
			return ID;
		}
		
		@Override
		public void execute(IPayloadContext context, ServerPlayer player)
		{
			if(destId.length() != 4)
				return;
			
			MSPacket.getAccessibleBlockEntity(player, this.pos, TransportalizerBlockEntity.class)
					.ifPresent(transportalizer -> transportalizer.setDestId(destId));
		}
	}
}
