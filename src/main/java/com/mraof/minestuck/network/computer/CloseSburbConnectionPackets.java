package com.mraof.minestuck.network.computer;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.blockentity.ComputerBlockEntity;
import com.mraof.minestuck.network.MSPacket;
import com.mraof.minestuck.skaianet.ComputerInteractions;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public final class CloseSburbConnectionPackets
{
	public static CustomPacketPayload asClient(ComputerBlockEntity be)
	{
		return new AsClient(be.getBlockPos());
	}
	
	public static CustomPacketPayload asServer(ComputerBlockEntity be)
	{
		return new AsServer(be.getBlockPos());
	}
	
	public record AsClient(BlockPos pos) implements MSPacket.PlayToServer
	{
		public static final Type<AsClient> ID = new Type<>(Minestuck.id("close_sburb_connection/as_client"));
		public static final StreamCodec<ByteBuf, AsClient> STREAM_CODEC = BlockPos.STREAM_CODEC.map(AsClient::new, AsClient::pos);
		
		@Override
		public Type<? extends CustomPacketPayload> type()
		{
			return ID;
		}
		
		@Override
		public void execute(IPayloadContext context, ServerPlayer player)
		{
			ComputerBlockEntity.getAccessibleComputer(player, this.pos).ifPresent(computer ->
					ComputerInteractions.get(player.server).closeClientConnection(computer));
		}
	}
	
	public record AsServer(BlockPos pos) implements MSPacket.PlayToServer
	{
		public static final Type<AsServer> ID = new Type<>(Minestuck.id("close_sburb_connection/as_server"));
		public static final StreamCodec<ByteBuf, AsServer> STREAM_CODEC = BlockPos.STREAM_CODEC.map(AsServer::new, AsServer::pos);
		
		@Override
		public Type<? extends CustomPacketPayload> type()
		{
			return ID;
		}
		
		@Override
		public void execute(IPayloadContext context, ServerPlayer player)
		{
			ComputerBlockEntity.getAccessibleComputer(player, this.pos).ifPresent(computer ->
						ComputerInteractions.get(player.server).closeServerConnection(computer));
		}
	}
}
