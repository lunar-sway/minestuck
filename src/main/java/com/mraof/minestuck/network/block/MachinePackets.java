package com.mraof.minestuck.network.block;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.inventory.MachineContainerMenu;
import com.mraof.minestuck.network.MSPacket;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public final class MachinePackets
{
	public record SetRunning(boolean shouldRun) implements MSPacket.PlayToServer
	{
		public static final Type<SetRunning> ID = new Type<>(Minestuck.id("machine/set_running"));
		public static final StreamCodec<ByteBuf, SetRunning> STREAM_CODEC = ByteBufCodecs.BOOL.map(SetRunning::new, SetRunning::shouldRun);
		
		@Override
		public Type<? extends CustomPacketPayload> type()
		{
			return ID;
		}
		
		@Override
		public void execute(IPayloadContext context, ServerPlayer player)
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
		public static final Type<SetLooping> ID = new Type<>(Minestuck.id("machine/set_looping"));
		public static final StreamCodec<ByteBuf, SetLooping> STREAM_CODEC = ByteBufCodecs.BOOL.map(SetLooping::new, SetLooping::shouldLoop);
		
		@Override
		public Type<? extends CustomPacketPayload> type()
		{
			return ID;
		}
		
		@Override
		public void execute(IPayloadContext context, ServerPlayer player)
		{
			if(player.containerMenu instanceof MachineContainerMenu container)
				container.setIsLooping(shouldLoop);
		}
	}
}
