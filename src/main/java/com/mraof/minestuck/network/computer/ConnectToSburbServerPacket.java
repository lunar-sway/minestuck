package com.mraof.minestuck.network.computer;

import com.mraof.minestuck.network.PlayToServerPacket;
import com.mraof.minestuck.player.IdentifierHandler;
import com.mraof.minestuck.skaianet.SkaianetHandler;
import com.mraof.minestuck.blockentity.ComputerBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;

public class ConnectToSburbServerPacket implements PlayToServerPacket
{
	private final BlockPos pos;
	private final int server;
	
	private ConnectToSburbServerPacket(BlockPos pos, int server)
	{
		this.pos = pos;
		this.server = server;
	}
	
	public static ConnectToSburbServerPacket create(ComputerBlockEntity be, int server)
	{
		return new ConnectToSburbServerPacket(be.getBlockPos(), server);
	}
	
	@Override
	public void encode(FriendlyByteBuf buffer)
	{
		buffer.writeBlockPos(pos);
		buffer.writeInt(server);
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
				computer -> SkaianetHandler.get(player.server).connectToServer(computer, IdentifierHandler.getById(server)));
	}
}