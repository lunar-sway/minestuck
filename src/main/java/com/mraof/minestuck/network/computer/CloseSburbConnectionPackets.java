package com.mraof.minestuck.network.computer;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.blockentity.ComputerBlockEntity;
import com.mraof.minestuck.network.MSPacket;
import com.mraof.minestuck.skaianet.ComputerInteractions;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

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
		public static final ResourceLocation ID = Minestuck.id("close_sburb_connection/as_client");
		
		@Override
		public ResourceLocation id()
		{
			return ID;
		}
		
		@Override
		public void write(FriendlyByteBuf buffer)
		{
			buffer.writeBlockPos(this.pos);
		}
		
		public static AsClient read(FriendlyByteBuf buffer)
		{
			BlockPos pos = buffer.readBlockPos();
			return new AsClient(pos);
		}
		
		@Override
		public void execute(ServerPlayer player)
		{
			ComputerBlockEntity.getAccessibleComputer(player, this.pos).ifPresent(computer ->
					ComputerInteractions.get(player.server).closeClientConnection(computer));
		}
	}
	
	public record AsServer(BlockPos pos) implements MSPacket.PlayToServer
	{
		public static final ResourceLocation ID = Minestuck.id("close_sburb_connection/as_server");
		
		@Override
		public ResourceLocation id()
		{
			return ID;
		}
		
		@Override
		public void write(FriendlyByteBuf buffer)
		{
			buffer.writeBlockPos(this.pos);
		}
		
		public static AsServer read(FriendlyByteBuf buffer)
		{
			BlockPos pos = buffer.readBlockPos();
			return new AsServer(pos);
		}
		
		@Override
		public void execute(ServerPlayer player)
		{
			ComputerBlockEntity.getAccessibleComputer(player, this.pos).ifPresent(computer ->
						ComputerInteractions.get(player.server).closeServerConnection(computer));
		}
	}
}
