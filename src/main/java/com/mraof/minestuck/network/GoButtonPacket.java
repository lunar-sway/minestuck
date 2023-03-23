package com.mraof.minestuck.network;

import com.mraof.minestuck.inventory.MachineContainerMenu;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;

public class GoButtonPacket implements PlayToServerPacket
{
	private final boolean shouldRun;
	private final boolean shouldLoop;
	
	public GoButtonPacket(boolean shouldRun, boolean shouldLoop)
	{
		this.shouldRun = shouldRun;
		this.shouldLoop = shouldLoop;
	}
	
	@Override
	public void encode(FriendlyByteBuf buffer)
	{
		buffer.writeBoolean(shouldRun);
		buffer.writeBoolean(shouldLoop);
	}
	
	public static GoButtonPacket decode(FriendlyByteBuf buffer)
	{
		boolean shouldRun = buffer.readBoolean();
		boolean shouldLoop = buffer.readBoolean();
		
		return new GoButtonPacket(shouldRun, shouldLoop);
	}
	
	@Override
	public void execute(ServerPlayer player)
	{
		if(player.containerMenu instanceof MachineContainerMenu container)
		{
			container.setIsLooping(shouldLoop);
			container.setShouldRun(shouldRun);
		}
	}
}