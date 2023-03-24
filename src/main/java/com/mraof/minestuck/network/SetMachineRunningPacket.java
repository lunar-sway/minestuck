package com.mraof.minestuck.network;

import com.mraof.minestuck.inventory.MachineContainerMenu;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;

public record SetMachineRunningPacket(boolean shouldRun) implements PlayToServerPacket
{
	@Override
	public void encode(FriendlyByteBuf buffer)
	{
		buffer.writeBoolean(shouldRun);
	}
	
	public static SetMachineRunningPacket decode(FriendlyByteBuf buffer)
	{
		return new SetMachineRunningPacket(buffer.readBoolean());
	}
	
	@Override
	public void execute(ServerPlayer player)
	{
		if(player.containerMenu instanceof MachineContainerMenu container)
		{
			container.setIsLooping(false);
			container.setShouldRun(shouldRun);
		}
	}
}