package com.mraof.minestuck.network;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.blockentity.machine.AlchemiterBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

public record AlchemiterPacket(BlockPos pos, int quantity) implements MSPacket.PlayToServer
{
	public static final ResourceLocation ID = Minestuck.id("alchemiter");
	
	@Override
	public ResourceLocation id()
	{
		return ID;
	}
	
	@Override
	public void write(FriendlyByteBuf buffer)
	{
		buffer.writeBlockPos(pos);
		buffer.writeInt(quantity);
	}
	
	public static AlchemiterPacket read(FriendlyByteBuf buffer)
	{
		BlockPos pos = buffer.readBlockPos();
		int quantity = buffer.readInt();
		
		return new AlchemiterPacket(pos, quantity);
	}
	
	@Override
	public void execute(ServerPlayer player)
	{
		//todo also check distance
		if(player.getCommandSenderWorld().isAreaLoaded(pos, 0))
		{
			if(player.getCommandSenderWorld().getBlockEntity(pos) instanceof AlchemiterBlockEntity alchemiter)
			{
				alchemiter.processContents(quantity, player);
			}
		}
	}
}
