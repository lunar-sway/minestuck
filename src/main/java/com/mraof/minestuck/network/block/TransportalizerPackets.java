package com.mraof.minestuck.network.block;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.blockentity.TransportalizerBlockEntity;
import com.mraof.minestuck.network.MSPacket;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

public final class TransportalizerPackets
{
	public record Id(BlockPos pos, String selfId) implements MSPacket.PlayToServer
	{
		public static final ResourceLocation ID = Minestuck.id("transportalizer/id");
		
		@Override
		public ResourceLocation id()
		{
			return ID;
		}
		
		@Override
		public void write(FriendlyByteBuf buffer)
		{
			buffer.writeBlockPos(pos);
			buffer.writeUtf(selfId, 4);
		}
		
		public static TransportalizerPackets.Id read(FriendlyByteBuf buffer)
		{
			BlockPos pos = buffer.readBlockPos();
			String id = buffer.readUtf(4);
			
			return new TransportalizerPackets.Id(pos, id);
		}
		
		@Override
		public void execute(ServerPlayer player)
		{
			if(selfId.length() != 4)
				return;
			
			MSPacket.getAccessibleBlockEntity(player, this.pos, TransportalizerBlockEntity.class)
					.ifPresent(transportalizer -> transportalizer.trySetId(selfId, player));
		}
	}
	
	public record DestId(BlockPos pos, String destId) implements MSPacket.PlayToServer
	{
		public static final ResourceLocation ID = Minestuck.id("transportalizer/dest_id");
		
		@Override
		public ResourceLocation id()
		{
			return ID;
		}
		
		@Override
		public void write(FriendlyByteBuf buffer)
		{
			buffer.writeBlockPos(pos);
			buffer.writeUtf(destId, 4);
		}
		
		public static TransportalizerPackets.DestId read(FriendlyByteBuf buffer)
		{
			BlockPos pos = buffer.readBlockPos();
			String destId = buffer.readUtf(4);
			
			return new TransportalizerPackets.DestId(pos, destId);
		}
		
		@Override
		public void execute(ServerPlayer player)
		{
			if(destId.length() != 4)
				return;
			
			MSPacket.getAccessibleBlockEntity(player, this.pos, TransportalizerBlockEntity.class)
					.ifPresent(transportalizer -> transportalizer.setDestId(destId));
		}
	}
}