package com.mraof.minestuck.network;

import com.mraof.minestuck.computer.ComputerReference;
import com.mraof.minestuck.player.IdentifierHandler;
import com.mraof.minestuck.skaianet.SkaianetHandler;
import com.mraof.minestuck.tileentity.ComputerTileEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;

public class SburbConnectPacket implements PlayToServerPacket
{
	
	private final BlockPos pos;
	private final int otherPlayer;
	private final boolean isClient;
	
	public SburbConnectPacket(BlockPos pos, int otherPlayer, boolean isClient)
	{
		this.pos = pos;
		this.otherPlayer = otherPlayer;
		this.isClient = isClient;
	}
	
	@Override
	public void encode(PacketBuffer buffer)
	{
		buffer.writeBlockPos(pos);
		buffer.writeInt(otherPlayer);
		buffer.writeBoolean(isClient);
	}
	
	public static SburbConnectPacket decode(PacketBuffer buffer)
	{
		BlockPos computer = buffer.readBlockPos();
		int otherPlayer = buffer.readInt();
		boolean isClient = buffer.readBoolean();
		
		return new SburbConnectPacket(computer, otherPlayer, isClient);
	}
	
	@Override
	public void execute(ServerPlayerEntity player)
	{
		ComputerTileEntity.forNetworkIfPresent(player, pos, computer ->
				SkaianetHandler.get(player.server).requestConnection(computer.owner, ComputerReference.of(computer), otherPlayer != -1 ? IdentifierHandler.getById(otherPlayer) : null, isClient));
	}
}