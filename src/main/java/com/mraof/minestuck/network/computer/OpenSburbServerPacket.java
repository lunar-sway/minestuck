package com.mraof.minestuck.network.computer;

import com.mraof.minestuck.network.PlayToServerPacket;
import com.mraof.minestuck.skaianet.SkaianetHandler;
import com.mraof.minestuck.tileentity.ComputerTileEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;

public class OpenSburbServerPacket implements PlayToServerPacket
{
	private final BlockPos pos;
	
	private OpenSburbServerPacket(BlockPos pos)
	{
		this.pos = pos;
	}
	
	public static OpenSburbServerPacket create(ComputerTileEntity te)
	{
		return new OpenSburbServerPacket(te.getBlockPos());
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
		ComputerTileEntity.forNetworkIfPresent(player, pos,
				computer -> SkaianetHandler.get(player.server).openServer(computer));
	}
}