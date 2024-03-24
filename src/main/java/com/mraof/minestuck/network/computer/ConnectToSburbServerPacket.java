package com.mraof.minestuck.network.computer;

import com.mraof.minestuck.blockentity.ComputerBlockEntity;
import com.mraof.minestuck.network.MSPacket;
import com.mraof.minestuck.player.IdentifierHandler;
import com.mraof.minestuck.skaianet.ComputerInteractions;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;

public class ConnectToSburbServerPacket implements MSPacket.PlayToServer
{
	private final BlockPos pos;
	private final int serverPlayer;
	
	private ConnectToSburbServerPacket(BlockPos pos, int serverPlayer)
	{
		this.pos = pos;
		this.serverPlayer = serverPlayer;
	}
	
	public static ConnectToSburbServerPacket create(ComputerBlockEntity be, int serverPlayer)
	{
		return new ConnectToSburbServerPacket(be.getBlockPos(), serverPlayer);
	}
	
	@Override
	public void encode(FriendlyByteBuf buffer)
	{
		buffer.writeBlockPos(pos);
		buffer.writeInt(serverPlayer);
	}
	
	public static ConnectToSburbServerPacket decode(FriendlyByteBuf buffer)
	{
		BlockPos computer = buffer.readBlockPos();
		int server = buffer.readInt();
		return new ConnectToSburbServerPacket(computer, server);
	}
	
	@Override
	public void execute(ServerPlayer player)
	{
		ComputerBlockEntity.forNetworkIfPresent(player, pos,
				computer -> ComputerInteractions.get(player.server).connectToServerPlayer(computer, IdentifierHandler.getById(serverPlayer)));
	}
}