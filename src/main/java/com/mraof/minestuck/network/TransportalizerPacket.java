package com.mraof.minestuck.network;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.blockentity.TransportalizerBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.phys.Vec3;

public final class TransportalizerPacket
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
		
		public static TransportalizerPacket.Id read(FriendlyByteBuf buffer)
		{
			BlockPos pos = buffer.readBlockPos();
			String id = buffer.readUtf(4);
			
			return new TransportalizerPacket.Id(pos, id);
		}
		
		@Override
		public void execute(ServerPlayer player)
		{
			if(player.getCommandSenderWorld().isAreaLoaded(pos, 0)
					&& player.distanceToSqr(Vec3.atCenterOf(pos)) <= 8 * 8
					&& selfId.length() == 4)
				if(player.level().getBlockEntity(pos) instanceof TransportalizerBlockEntity transportalizer)
					transportalizer.trySetId(selfId, player);
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
		
		public static TransportalizerPacket.DestId read(FriendlyByteBuf buffer)
		{
			BlockPos pos = buffer.readBlockPos();
			String destId = buffer.readUtf(4);
			
			return new TransportalizerPacket.DestId(pos, destId);
		}
		
		@Override
		public void execute(ServerPlayer player)
		{
			if(player.getCommandSenderWorld().isAreaLoaded(pos, 0)
					&& player.distanceToSqr(Vec3.atCenterOf(pos)) <= 8 * 8
					&& destId.length() == 4)
				if(player.level().getBlockEntity(pos) instanceof TransportalizerBlockEntity transportalizer)
					transportalizer.setDestId(destId);
		}
	}
}
