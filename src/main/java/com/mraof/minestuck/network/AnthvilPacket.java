package com.mraof.minestuck.network;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.blockentity.machine.AnthvilBlockEntity;
import com.mraof.minestuck.inventory.AnthvilMenu;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.inventory.AbstractContainerMenu;

public record AnthvilPacket() implements MSPacket.PlayToServer
{
	public static final ResourceLocation ID = Minestuck.id("anthvil");
	
	@Override
	public ResourceLocation id()
	{
		return ID;
	}
	
	@Override
	public void write(FriendlyByteBuf buffer)
	{
	}
	
	public static AnthvilPacket read(FriendlyByteBuf ignored)
	{
		return new AnthvilPacket();
	}
	
	@Override
	public void execute(ServerPlayer player)
	{
		AbstractContainerMenu playerContainer = player.containerMenu;
		if(!(playerContainer instanceof AnthvilMenu anthvilMenu))
			return;
		
		anthvilMenu.getPosition().execute((level, machinePos) -> {
			if(level.getBlockEntity(machinePos) instanceof AnthvilBlockEntity anthvilBlockEntity)
			{
				AnthvilBlockEntity.attemptMendAndRefuel(anthvilBlockEntity, player);
			}
		});
	}
}
