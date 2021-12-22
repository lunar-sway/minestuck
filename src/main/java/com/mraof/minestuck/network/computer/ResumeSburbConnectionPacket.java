package com.mraof.minestuck.network.computer;

import com.mraof.minestuck.network.PlayToServerPacket;
import com.mraof.minestuck.skaianet.SkaianetHandler;
import com.mraof.minestuck.tileentity.ComputerTileEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;

public class ResumeSburbConnectionPacket implements PlayToServerPacket
{
	private final BlockPos pos;
	private final boolean isClient;
	
	private ResumeSburbConnectionPacket(BlockPos pos, boolean isClient)
	{
		this.pos = pos;
		this.isClient = isClient;
	}
	
	public static ResumeSburbConnectionPacket asClient(ComputerTileEntity te)
	{
		return new ResumeSburbConnectionPacket(te.getBlockPos(), true);
	}
	
	public static ResumeSburbConnectionPacket asServer(ComputerTileEntity te)
	{
		return new ResumeSburbConnectionPacket(te.getBlockPos(), false);
	}
	
	@Override
	public void encode(PacketBuffer buffer)
	{
		buffer.writeBlockPos(pos);
		buffer.writeBoolean(isClient);
	}
	
	public static ResumeSburbConnectionPacket decode(PacketBuffer buffer)
	{
		BlockPos computer = buffer.readBlockPos();
		boolean isClient = buffer.readBoolean();
		return new ResumeSburbConnectionPacket(computer, isClient);
	}
	
	@Override
	public void execute(ServerPlayerEntity player)
	{
		ComputerTileEntity.forNetworkIfPresent(player, pos,
				computer -> SkaianetHandler.get(player.server).resumeConnection(computer, isClient));
	}
}