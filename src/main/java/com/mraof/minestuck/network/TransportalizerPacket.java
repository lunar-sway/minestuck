package com.mraof.minestuck.network;

import com.mraof.minestuck.blockentity.TransportalizerBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.phys.Vec3;

public class TransportalizerPacket
{
	public record Id(BlockPos pos, String id) implements MSPacket.PlayToServer
	{
		@Override
		public void encode(FriendlyByteBuf buffer)
		{
			buffer.writeBlockPos(pos);
			buffer.writeUtf(id, 4);
		}
		
		public static TransportalizerPacket.Id decode(FriendlyByteBuf buffer)
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
					&& id.length() == 4)
				if(player.level().getBlockEntity(pos) instanceof TransportalizerBlockEntity transportalizer)
					transportalizer.trySetId(id, player);
		}
	}
	
	public record DestId(BlockPos pos, String destId) implements MSPacket.PlayToServer
	{
		@Override
		public void encode(FriendlyByteBuf buffer)
		{
			buffer.writeBlockPos(pos);
			buffer.writeUtf(destId, 4);
		}
		
		public static TransportalizerPacket.DestId decode(FriendlyByteBuf buffer)
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