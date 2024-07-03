package com.mraof.minestuck.network;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.client.gui.playerStats.DataCheckerScreen;
import com.mraof.minestuck.player.ClientPlayerData;
import com.mraof.minestuck.skaianet.DataCheckerManager;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

public final class DataCheckerPackets
{
	private static int index = 0;
	
	public record Request(int packetIndex) implements MSPacket.PlayToServer
	{
		public static final ResourceLocation ID = Minestuck.id("data_checker/request");
		
		public static Request create()
		{
			DataCheckerPackets.index = (DataCheckerPackets.index + 1) % 100;
			return new Request(DataCheckerPackets.index);
		}
		
		@Override
		public ResourceLocation id()
		{
			return ID;
		}
		
		@Override
		public void write(FriendlyByteBuf buffer)
		{
			buffer.writeInt(this.packetIndex);
		}
		
		public static Request read(FriendlyByteBuf buffer)
		{
			int packetIndex = buffer.readInt();
			return new Request(packetIndex);
		}
		
		@Override
		public void execute(ServerPlayer player)
		{
			DataCheckerManager.onDataRequest(player, this.packetIndex);
		}
	}
	
	public record Data(int packetIndex, CompoundTag nbtData) implements MSPacket.PlayToClient
	{
		public static final ResourceLocation ID = Minestuck.id("data_checker/data");
		
		@Override
		public ResourceLocation id()
		{
			return ID;
		}
		
		@Override
		public void write(FriendlyByteBuf buffer)
		{
			buffer.writeInt(this.packetIndex);
			buffer.writeNbt(this.nbtData);
		}
		
		public static Data read(FriendlyByteBuf buffer)
		{
			int packetIndex = buffer.readInt();
			CompoundTag nbt = buffer.readNbt();
			return new Data(packetIndex, nbt);
		}
		
		@Override
		public void execute()
		{
			if(packetIndex == DataCheckerPackets.index)
				DataCheckerScreen.activeComponent = new DataCheckerScreen.MainComponent(nbtData);
		}
	}
	
	public record Permission(boolean isAvailable) implements MSPacket.PlayToClient
	{
		public static final ResourceLocation ID = Minestuck.id("data_checker/permission");
		
		@Override
		public ResourceLocation id()
		{
			return ID;
		}
		
		@Override
		public void write(FriendlyByteBuf buffer)
		{
			buffer.writeBoolean(isAvailable);
		}
		
		public static Permission read(FriendlyByteBuf buffer)
		{
			boolean isAvailable = buffer.readBoolean();
			
			return new Permission(isAvailable);
		}
		
		@Override
		public void execute()
		{
			ClientPlayerData.handleDataPacket(this);
		}
	}
}
