package com.mraof.minestuck.network.computer;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.blockentity.ComputerBlockEntity;
import com.mraof.minestuck.network.MSPacket;
import com.mraof.minestuck.player.IdentifierHandler;
import com.mraof.minestuck.player.PlayerIdentifier;
import com.mraof.minestuck.skaianet.ComputerInteractions;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record ConnectToSburbServerPacket(BlockPos computerPos, int serverPlayerId) implements MSPacket.PlayToServer
{
	public static final Type<ConnectToSburbServerPacket> ID = new Type<>(Minestuck.id("connect_to_sburb_server"));
	public static final StreamCodec<FriendlyByteBuf, ConnectToSburbServerPacket> STREAM_CODEC = StreamCodec.composite(
			BlockPos.STREAM_CODEC,
			ConnectToSburbServerPacket::computerPos,
			ByteBufCodecs.INT,
			ConnectToSburbServerPacket::serverPlayerId,
			ConnectToSburbServerPacket::new
	);
	
	public static ConnectToSburbServerPacket create(ComputerBlockEntity be, int serverPlayerId)
	{
		return new ConnectToSburbServerPacket(be.getBlockPos(), serverPlayerId);
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
				.ifPresent(computer -> {
					PlayerIdentifier serverPlayer = IdentifierHandler.getById(serverPlayerId);
					ComputerInteractions.get(player.server).connectToServerPlayer(computer, serverPlayer);
				});
	}
}