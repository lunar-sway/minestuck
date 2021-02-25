package com.mraof.minestuck.network.computer;

import com.mraof.minestuck.network.PlayToServerPacket;
import com.mraof.minestuck.player.IdentifierHandler;
import com.mraof.minestuck.skaianet.SkaianetHandler;
import com.mraof.minestuck.tileentity.ComputerTileEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;

public class ConnectToSburbServerPacket implements PlayToServerPacket
{
	private final BlockPos pos;
	private final int server;
	
	private ConnectToSburbServerPacket(BlockPos pos, int server)
	{
		this.pos = pos;
		this.server = server;
	}
	
	public static ConnectToSburbServerPacket create(ComputerTileEntity te, int server)
	{
		return new ConnectToSburbServerPacket(te.getPos(), server);
	}
	
	@Override
	public void encode(PacketBuffer buffer)
	{
		buffer.writeBlockPos(pos);
		buffer.writeInt(server);
	}
	
	public static ConnectToSburbServerPacket decode(PacketBuffer buffer)
	{
		BlockPos computer = buffer.readBlockPos();
		int server = buffer.readInt();
		return new ConnectToSburbServerPacket(computer, server);
	}
	
	@Override
	public void execute(ServerPlayerEntity player)
	{
		ComputerTileEntity.forNetworkIfPresent(player, pos,
				computer -> SkaianetHandler.get(player.server).connectToServer(computer, IdentifierHandler.getById(server)));
	}
}