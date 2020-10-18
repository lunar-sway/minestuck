package com.mraof.minestuck.network.computer;

import com.mraof.minestuck.network.PlayToServerPacket;
import com.mraof.minestuck.skaianet.SkaianetHandler;
import com.mraof.minestuck.tileentity.ComputerTileEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;

public class OpenSburbServerPacket implements PlayToServerPacket
{
	private final BlockPos pos;
	
	private OpenSburbServerPacket(BlockPos pos)
	{
		this.pos = pos;
	}
	
	public static OpenSburbServerPacket create(ComputerTileEntity te)
	{
		return new OpenSburbServerPacket(te.getPos());
	}
	
	@Override
	public void encode(PacketBuffer buffer)
	{
		buffer.writeBlockPos(pos);
	}
	
	public static OpenSburbServerPacket decode(PacketBuffer buffer)
	{
		BlockPos computer = buffer.readBlockPos();
		return new OpenSburbServerPacket(computer);
	}
	
	@Override
	public void execute(ServerPlayerEntity player)
	{
		ComputerTileEntity.forNetworkIfPresent(player, pos,
				computer -> SkaianetHandler.get(player.server).openServer(computer));
	}
}