package com.mraof.minestuck.network.block;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.inventory.MachineContainerMenu;
import com.mraof.minestuck.network.MSPacket;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

public final class MachinePackets
{
	public record SetRunning(boolean shouldRun) implements MSPacket.PlayToServer
	{
		public static final ResourceLocation ID = Minestuck.id("machine/set_running");
		
		@Override
		public ResourceLocation id()
		{
			return ID;
		}
		
		@Override
		public void write(FriendlyByteBuf buffer)
		{
			buffer.writeBoolean(shouldRun);
		}
		
		public static SetRunning read(FriendlyByteBuf buffer)
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
		public static final ResourceLocation ID = Minestuck.id("machine/set_looping");
		
		@Override
		public ResourceLocation id()
		{
			return ID;
		}
		
		@Override
		public void write(FriendlyByteBuf buffer)
		{
			buffer.writeBoolean(this.shouldLoop);
		}
		
		public static SetLooping read(FriendlyByteBuf buffer)
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
