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
	public record SetId(BlockPos pos, String selfId) implements MSPacket.PlayToServer
	{
		public static final ResourceLocation ID = Minestuck.id("transportalizer/set_id");
		
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
		
		public static SetId read(FriendlyByteBuf buffer)
		{
			BlockPos pos = buffer.readBlockPos();
			String id = buffer.readUtf(4);
			
			return new SetId(pos, id);
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
	
	public record SetDestId(BlockPos pos, String destId) implements MSPacket.PlayToServer
	{
		public static final ResourceLocation ID = Minestuck.id("transportalizer/set_dest_id");
		
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
		
		public static SetDestId read(FriendlyByteBuf buffer)
		{
			BlockPos pos = buffer.readBlockPos();
			String destId = buffer.readUtf(4);
			
			return new SetDestId(pos, destId);
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
