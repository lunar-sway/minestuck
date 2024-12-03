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

public record CloseRemoteSburbConnectionPacket(BlockPos computerPos) implements MSPacket.PlayToServer
{
	public static final Type<CloseRemoteSburbConnectionPacket> ID = new Type<>(Minestuck.id("close_remote_sburb_connection"));
	public static final StreamCodec<ByteBuf, CloseRemoteSburbConnectionPacket> STREAM_CODEC = BlockPos.STREAM_CODEC.map(CloseRemoteSburbConnectionPacket::new, CloseRemoteSburbConnectionPacket::computerPos);
	
	public static CloseRemoteSburbConnectionPacket asClient(ComputerBlockEntity be)
	{
		return new CloseRemoteSburbConnectionPacket(be.getBlockPos());
	}
	
	@Override
	public Type<? extends CustomPacketPayload> type()
	{
		return ID;
	}
	
	@Override
	public void execute(IPayloadContext context, ServerPlayer player)
	{
		ComputerBlockEntity.getAccessibleComputer(player, computerPos)
				.ifPresent(computer -> ComputerInteractions.get(player.server).closeClientConnectionRemotely(computer.getOwner()));
	}
}