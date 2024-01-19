package com.mraof.minestuck.network.computer;

import com.mraof.minestuck.blockentity.ComputerBlockEntity;
import com.mraof.minestuck.network.MSPacket;
import com.mraof.minestuck.skaianet.SkaianetComputerInteractions;
import com.mraof.minestuck.skaianet.SkaianetHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;

public class CloseRemoteSburbConnectionPacket implements MSPacket.PlayToServer
{
	private final BlockPos pos;
	
	private CloseRemoteSburbConnectionPacket(BlockPos pos)
	{
		this.pos = pos;
	}
	
	public static CloseRemoteSburbConnectionPacket asClient(ComputerBlockEntity be)
	{
		return new CloseRemoteSburbConnectionPacket(be.getBlockPos());
	}
	
	@Override
	public void encode(FriendlyByteBuf buffer)
	{
		buffer.writeBlockPos(pos);
	}
	
	public static CloseRemoteSburbConnectionPacket decode(FriendlyByteBuf buffer)
	{
		BlockPos computer = buffer.readBlockPos();
		return new CloseRemoteSburbConnectionPacket(computer);
	}
	
	@Override
	public void execute(ServerPlayer player)
	{
		ComputerBlockEntity.forNetworkIfPresent(player, pos,
				computer -> SkaianetComputerInteractions.closeClientConnectionRemotely(computer.getOwner(), SkaianetHandler.get(player.server)));
	}
}