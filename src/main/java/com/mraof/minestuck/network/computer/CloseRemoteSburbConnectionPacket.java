package com.mraof.minestuck.network.computer;

import com.mraof.minestuck.network.PlayToServerPacket;
import com.mraof.minestuck.skaianet.SkaianetHandler;
import com.mraof.minestuck.tileentity.ComputerTileEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;

public class CloseRemoteSburbConnectionPacket implements PlayToServerPacket
{
	private final BlockPos pos;
	
	private CloseRemoteSburbConnectionPacket(BlockPos pos)
	{
		this.pos = pos;
	}
	
	public static CloseRemoteSburbConnectionPacket asClient(ComputerTileEntity te)
	{
		return new CloseRemoteSburbConnectionPacket(te.getPos());
	}
	
	@Override
	public void encode(PacketBuffer buffer)
	{
		buffer.writeBlockPos(pos);
	}
	
	public static CloseRemoteSburbConnectionPacket decode(PacketBuffer buffer)
	{
		BlockPos computer = buffer.readBlockPos();
		return new CloseRemoteSburbConnectionPacket(computer);
	}
	
	@Override
	public void execute(ServerPlayerEntity player)
	{
		ComputerTileEntity.forNetworkIfPresent(player, pos,
				computer -> SkaianetHandler.get(player.server).closeClientConnectionRemotely(computer.getOwner()));
	}
}