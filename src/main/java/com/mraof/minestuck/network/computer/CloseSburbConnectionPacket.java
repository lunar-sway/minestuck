package com.mraof.minestuck.network.computer;

import com.mraof.minestuck.blockentity.ComputerBlockEntity;
import com.mraof.minestuck.network.MSPacket;
import com.mraof.minestuck.skaianet.SkaianetComputerInteractions;
import com.mraof.minestuck.skaianet.SkaianetData;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;

public class CloseSburbConnectionPacket implements MSPacket.PlayToServer
{
	private final BlockPos pos;
	private final boolean isClient;
	
	private CloseSburbConnectionPacket(BlockPos pos, boolean isClient)
	{
		this.pos = pos;
		this.isClient = isClient;
	}
	
	public static CloseSburbConnectionPacket asClient(ComputerBlockEntity be)
	{
		return new CloseSburbConnectionPacket(be.getBlockPos(), true);
	}
	
	public static CloseSburbConnectionPacket asServer(ComputerBlockEntity be)
	{
		return new CloseSburbConnectionPacket(be.getBlockPos(), false);
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
				SkaianetComputerInteractions.closeClientConnection(computer, SkaianetData.get(player.server));
			else SkaianetComputerInteractions.closeServerConnection(computer, SkaianetData.get(player.server));
		});
	}
}