package com.mraof.minestuck.network;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.client.gui.playerStats.DataCheckerScreen;
import com.mraof.minestuck.player.ClientPlayerData;
import com.mraof.minestuck.skaianet.DataCheckerManager;
import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public final class DataCheckerPackets
{
	private static int index = 0;
	
	public record Request(int packetIndex) implements MSPacket.PlayToServer
	{
		
		public static final Type<Request> ID = new Type<>(Minestuck.id("data_checker/request"));
		public static final StreamCodec<ByteBuf, Request> STREAM_CODEC = ByteBufCodecs.INT.map(Request::new, Request::packetIndex);
		
		public static Request create()
		{
			DataCheckerPackets.index = (DataCheckerPackets.index + 1) % 100;
			return new Request(DataCheckerPackets.index);
		}
		
		@Override
		public Type<? extends CustomPacketPayload> type()
		{
			return ID;
		}
		
		@Override
		public void execute(IPayloadContext context, ServerPlayer player)
		{
			DataCheckerManager.onDataRequest(player, this.packetIndex);
		}
	}
	
	public record Data(int packetIndex, CompoundTag nbtData) implements MSPacket.PlayToClient
	{
		
		public static final Type<Data> ID = new Type<>(Minestuck.id("data_checker/data"));
		public static final StreamCodec<FriendlyByteBuf, Data> STREAM_CODEC = StreamCodec.composite(
				ByteBufCodecs.INT,
				Data::packetIndex,
				ByteBufCodecs.COMPOUND_TAG,
				Data::nbtData,
				Data::new
		);
		
		@Override
		public Type<? extends CustomPacketPayload> type()
		{
			return ID;
		}
		
		@Override
		public void execute(IPayloadContext context)
		{
			if(packetIndex == DataCheckerPackets.index)
				DataCheckerScreen.activeComponent = new DataCheckerScreen.MainComponent(nbtData);
		}
	}
	
	public record Permission(boolean isAvailable) implements MSPacket.PlayToClient
	{
		
		public static final Type<Permission> ID = new Type<>(Minestuck.id("data_checker/permission"));
		public static final StreamCodec<ByteBuf, Permission> STREAM_CODEC = ByteBufCodecs.BOOL.map(Permission::new, Permission::isAvailable);
		
		@Override
		public Type<? extends CustomPacketPayload> type()
		{
			return ID;
		}
		
		
		@Override
		public void execute(IPayloadContext context)
		{
			ClientPlayerData.handleDataPacket(this);
		}
	}
}
