package com.mraof.minestuck.network.computer;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.blockentity.ComputerBlockEntity;
import com.mraof.minestuck.network.MSPacket;
import com.mraof.minestuck.skaianet.ComputerInteractions;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

public class CloseRemoteSburbConnectionPacket implements MSPacket.PlayToServer
{
	public static final ResourceLocation ID = Minestuck.id("close_remote_sburb_connection");
	
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
	public ResourceLocation id()
	{
		return ID;
	}
	
	@Override
	public void write(FriendlyByteBuf buffer)
	{
		buffer.writeBlockPos(pos);
	}
	
	public static CloseRemoteSburbConnectionPacket read(FriendlyByteBuf buffer)
	{
		BlockPos computer = buffer.readBlockPos();
		return new CloseRemoteSburbConnectionPacket(computer);
	}
	
	@Override
	public void execute(ServerPlayer player)
	{
		ComputerBlockEntity.forNetworkIfPresent(player, pos,
				computer -> ComputerInteractions.get(player.server).closeClientConnectionRemotely(computer.getOwner()));
	}
}