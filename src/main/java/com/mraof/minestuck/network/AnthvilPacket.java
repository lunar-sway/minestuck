package com.mraof.minestuck.network;

import com.mraof.minestuck.blockentity.machine.AnthvilBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;

public class AnthvilPacket implements PlayToServerPacket
{
	private final BlockPos pos;
	
	public AnthvilPacket(BlockPos pos)
	{
		this.pos = pos;
	}
	
	@Override
	public void encode(FriendlyByteBuf buffer)
	{
		buffer.writeBlockPos(pos);
	}
	
	public static AnthvilPacket decode(FriendlyByteBuf buffer)
	{
		BlockPos pos = buffer.readBlockPos();
		
		return new AnthvilPacket(pos);
	}
	
	@Override
	public void execute(ServerPlayer player)
	{
		if(player.getCommandSenderWorld().isAreaLoaded(pos, 0))
		{
			if(player.level.getBlockEntity(pos) instanceof AnthvilBlockEntity anthvilBlockEntity)
			{
				AnthvilBlockEntity.attemptMend(anthvilBlockEntity, player);
			}
		}
	}
}