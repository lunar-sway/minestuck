package com.mraof.minestuck.network.computer;

import com.mraof.minestuck.blockentity.ComputerBlockEntity;
import com.mraof.minestuck.network.MSPacket;
import com.mraof.minestuck.skaianet.SkaianetComputerInteractions;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;

public class OpenSburbServerPacket implements MSPacket.PlayToServer
{
	private final BlockPos pos;
	
	private OpenSburbServerPacket(BlockPos pos)
	{
		this.pos = pos;
	}
	
	public static OpenSburbServerPacket create(ComputerBlockEntity be)
	{
		return new OpenSburbServerPacket(be.getBlockPos());
	}
	
	@Override
	public void encode(FriendlyByteBuf buffer)
	{
		buffer.writeBlockPos(pos);
	}
	
	public static OpenSburbServerPacket decode(FriendlyByteBuf buffer)
	{
		BlockPos computer = buffer.readBlockPos();
		return new OpenSburbServerPacket(computer);
	}
	
	@Override
	public void execute(ServerPlayer player)
	{
		ComputerBlockEntity.forNetworkIfPresent(player, pos,
				computer -> SkaianetComputerInteractions.openServer(computer, player.server));
	}
}