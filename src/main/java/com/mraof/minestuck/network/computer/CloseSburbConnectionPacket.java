package com.mraof.minestuck.network.computer;

import com.mraof.minestuck.network.PlayToServerPacket;
import com.mraof.minestuck.skaianet.SkaianetHandler;
import com.mraof.minestuck.tileentity.ComputerTileEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;

public class CloseSburbConnectionPacket implements PlayToServerPacket
{
	private final BlockPos pos;
	private final boolean isClient;
	
	private CloseSburbConnectionPacket(BlockPos pos, boolean isClient)
	{
		this.pos = pos;
		this.isClient = isClient;
	}
	
	public static CloseSburbConnectionPacket asClient(ComputerTileEntity te)
	{
		return new CloseSburbConnectionPacket(te.getBlockPos(), true);
	}
	
	public static CloseSburbConnectionPacket asServer(ComputerTileEntity te)
	{
		return new CloseSburbConnectionPacket(te.getBlockPos(), false);
	}
	
	@Override
	public void encode(PacketBuffer buffer)
	{
		buffer.writeBlockPos(pos);
		buffer.writeBoolean(isClient);
	}
	
	public static CloseSburbConnectionPacket decode(PacketBuffer buffer)
	{
		BlockPos computer = buffer.readBlockPos();
		boolean isClient = buffer.readBoolean();
		return new CloseSburbConnectionPacket(computer, isClient);
	}
	
	@Override
	public void execute(ServerPlayerEntity player)
	{
		ComputerTileEntity.forNetworkIfPresent(player, pos, computer -> {
			if(isClient)
				SkaianetHandler.get(player.server).closeClientConnection(computer);
			else SkaianetHandler.get(player.server).closeServerConnection(computer);
		});
	}
}