package com.mraof.minestuck.network.computer;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.blockentity.ComputerBlockEntity;
import com.mraof.minestuck.network.MSPacket;
import com.mraof.minestuck.skaianet.ComputerInteractions;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.BlockPos;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record OpenSburbServerPacket(BlockPos computerPos) implements MSPacket.PlayToServer
{
	public static final Type<OpenSburbServerPacket> ID = new Type<>(Minestuck.id("open_sburb_server"));
	public static final StreamCodec<ByteBuf, OpenSburbServerPacket> STREAM_CODEC = BlockPos.STREAM_CODEC.map(OpenSburbServerPacket::new, OpenSburbServerPacket::computerPos);
	
	public static OpenSburbServerPacket create(ComputerBlockEntity be)
	{
		return new OpenSburbServerPacket(be.getBlockPos());
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
				.ifPresent(computer -> ComputerInteractions.get(player.server).openServer(computer));
	}
}