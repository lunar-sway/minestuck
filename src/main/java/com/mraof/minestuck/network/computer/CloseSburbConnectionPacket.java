package com.mraof.minestuck.network.computer;

import com.mraof.minestuck.network.PlayToServerPacket;
import com.mraof.minestuck.skaianet.SkaianetHandler;
import com.mraof.minestuck.blockentity.ComputerBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;

public class CloseSburbConnectionPacket implements PlayToServerPacket
{
	private final BlockPos pos;
	private final boolean isClient;
	
	private CloseSburbConnectionPacket(BlockPos pos, boolean isClient)
	{
		this.pos = pos;
		this.isClient = isClient;
	}
	
	public static CloseSburbConnectionPacket asClient(ComputerBlockEntity te)
	{
		return new CloseSburbConnectionPacket(te.getBlockPos(), true);
	}
	
	public static CloseSburbConnectionPacket asServer(ComputerBlockEntity te)
	{
		return new CloseSburbConnectionPacket(te.getBlockPos(), false);
	}
	
	@Override
	public void encode(FriendlyByteBuf buffer)
	{
		buffer.writeBlockPos(pos);
		buffer.writeBoolean(isClient);
	}
	
	public static CloseSburbConnectionPacket decode(FriendlyByteBuf buffer)
	{
		BlockPos computer = buffer.readBlockPos();
		boolean isClient = buffer.readBoolean();
		return new CloseSburbConnectionPacket(computer, isClient);
	}
	
	@Override
	public void execute(ServerPlayer player)
	{
		ComputerBlockEntity.forNetworkIfPresent(player, pos, computer -> {
			if(isClient)
				SkaianetHandler.get(player.server).closeClientConnection(computer);
			else SkaianetHandler.get(player.server).closeServerConnection(computer);
		});
	}
}