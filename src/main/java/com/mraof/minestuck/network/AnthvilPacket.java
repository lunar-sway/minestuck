package com.mraof.minestuck.network;

import com.mraof.minestuck.blockentity.machine.AnthvilBlockEntity;
import com.mraof.minestuck.inventory.AnthvilMenu;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.inventory.AbstractContainerMenu;

public class AnthvilPacket implements PlayToServerPacket
{
	@Override
	public void encode(FriendlyByteBuf buffer)
	{
	
	}
	
	public static AnthvilPacket decode(FriendlyByteBuf buffer)
	{
		return new AnthvilPacket();
	}
	
	@Override
	public void execute(ServerPlayer player)
	{
		AbstractContainerMenu playerContainer = player.containerMenu;
		if(playerContainer instanceof AnthvilMenu anthvilMenu)
		{
			anthvilMenu.getPosition().execute((level, machinePos) -> {
				if(level.getBlockEntity(machinePos) instanceof AnthvilBlockEntity anthvilBlockEntity)
				{
					AnthvilBlockEntity.attemptMendAndRefuel(anthvilBlockEntity, player);
				}
			});
		}
	}
}