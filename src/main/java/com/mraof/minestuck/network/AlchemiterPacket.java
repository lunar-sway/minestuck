package com.mraof.minestuck.network;

import com.mraof.minestuck.blockentity.machine.AlchemiterBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;

public class AlchemiterPacket implements MSPacket.PlayToServer
{
	private final BlockPos pos;
	private final int quantity;
	
	public AlchemiterPacket(BlockPos pos, int quantity)
	{
		this.pos = pos;
		this.quantity = quantity;
	}
	
	@Override
	public void encode(FriendlyByteBuf buffer)
	{
		buffer.writeBlockPos(pos);
		buffer.writeInt(quantity);
	}
	
	public static AlchemiterPacket decode(FriendlyByteBuf buffer)
	{
		BlockPos pos = buffer.readBlockPos();
		int quantity = buffer.readInt();
		
		return new AlchemiterPacket(pos, quantity);
	}
	
	@Override
	public void execute(ServerPlayer player)
	{
		if(player.getCommandSenderWorld().isAreaLoaded(pos, 0))
		{
			if(player.getCommandSenderWorld().getBlockEntity(pos) instanceof AlchemiterBlockEntity alchemiter)
			{
				alchemiter.processContents(quantity, player);
			}
		}
	}
}