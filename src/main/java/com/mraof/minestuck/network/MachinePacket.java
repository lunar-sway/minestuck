package com.mraof.minestuck.network;

import com.mraof.minestuck.inventory.MachineContainerMenu;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;

public final class MachinePacket
{
	public record SetRunning(boolean shouldRun) implements MSPacket.PlayToServer
	{
		@Override
		public void encode(FriendlyByteBuf buffer)
		{
			buffer.writeBoolean(shouldRun);
		}
		
		public static SetRunning decode(FriendlyByteBuf buffer)
		{
			return new SetRunning(buffer.readBoolean());
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
	
	public record SetLooping(boolean shouldLoop) implements MSPacket.PlayToServer
	{
		@Override
		public void encode(FriendlyByteBuf buffer)
		{
			buffer.writeBoolean(this.shouldLoop);
		}
		
		public static SetLooping decode(FriendlyByteBuf buffer)
		{
			return new SetLooping(buffer.readBoolean());
		}
		
		@Override
		public void execute(ServerPlayer player)
		{
			if(player.containerMenu instanceof MachineContainerMenu container)
				container.setIsLooping(shouldLoop);
		}
	}
}
