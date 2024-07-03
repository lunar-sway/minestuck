package com.mraof.minestuck.network.computer;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.blockentity.ComputerBlockEntity;
import com.mraof.minestuck.network.MSPacket;
import com.mraof.minestuck.player.IdentifierHandler;
import com.mraof.minestuck.player.PlayerIdentifier;
import com.mraof.minestuck.skaianet.ComputerInteractions;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

public record ConnectToSburbServerPacket(BlockPos computerPos, int serverPlayerId) implements MSPacket.PlayToServer
{
	public static final ResourceLocation ID = Minestuck.id("connect_to_sburb_server");
	
	public static ConnectToSburbServerPacket create(ComputerBlockEntity be, int serverPlayerId)
	{
		return new ConnectToSburbServerPacket(be.getBlockPos(), serverPlayerId);
	}
	
	@Override
	public ResourceLocation id()
	{
		return ID;
	}
	
	@Override
	public void write(FriendlyByteBuf buffer)
	{
		buffer.writeBlockPos(computerPos);
		buffer.writeInt(serverPlayerId);
	}
	
	public static ConnectToSburbServerPacket read(FriendlyByteBuf buffer)
	{
		BlockPos computer = buffer.readBlockPos();
		int server = buffer.readInt();
		return new ConnectToSburbServerPacket(computer, server);
	}
	
	@Override
	public void execute(ServerPlayer player)
	{
		ComputerBlockEntity.getAccessibleComputer(player, computerPos)
				.ifPresent(computer -> {
					PlayerIdentifier serverPlayer = IdentifierHandler.getById(serverPlayerId);
					ComputerInteractions.get(player.server).connectToServerPlayer(computer, serverPlayer);
				});
	}
}