package com.mraof.minestuck.network;

import com.mraof.minestuck.inventory.MachineContainerMenu;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;

public record SetMachineLoopingPacket(boolean shouldLoop) implements PlayToServerPacket
{
	@Override
	public void encode(FriendlyByteBuf buffer)
	{
		buffer.writeBoolean(this.shouldLoop);
	}
	
	public static SetMachineLoopingPacket decode(FriendlyByteBuf buffer)
	{
		return new SetMachineLoopingPacket(buffer.readBoolean());
	}
	
	@Override
	public void execute(ServerPlayer player)
	{
		if(player.containerMenu instanceof MachineContainerMenu container)
			container.setIsLooping(shouldLoop);
	}
}