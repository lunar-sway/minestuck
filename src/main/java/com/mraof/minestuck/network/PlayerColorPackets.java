package com.mraof.minestuck.network;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.player.ClientPlayerData;
import com.mraof.minestuck.util.ColorHandler;
import io.netty.buffer.ByteBuf;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.handling.IPayloadContext;

@MethodsReturnNonnullByDefault
public final class PlayerColorPackets
{
	public record OpenSelection() implements MSPacket.PlayToClient
	{
		
		public static final Type<OpenSelection> ID = new Type<>(Minestuck.id("player_color/open_selection"));
		public static final StreamCodec<FriendlyByteBuf, OpenSelection> STREAM_CODEC = StreamCodec.unit(new OpenSelection());
		
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
	
	public record Data(int color) implements MSPacket.PlayToClient
	{
		
		public static final Type<Data> ID = new Type<>(Minestuck.id("player_color/data"));
		public static final StreamCodec<ByteBuf, Data> STREAM_CODEC = ByteBufCodecs.INT.map(Data::new, Data::color);
		
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
	
	public record SelectIndex(int colorIndex) implements MSPacket.PlayToServer
	{
		
		public static final Type<SelectIndex> ID = new Type<>(Minestuck.id("player_color/select_index"));
		public static final StreamCodec<ByteBuf, SelectIndex> STREAM_CODEC = ByteBufCodecs.INT.map(SelectIndex::new, SelectIndex::colorIndex);
		
		@Override
		public Type<? extends CustomPacketPayload> type()
		{
			return ID;
		}
		
		@Override
		public void execute(IPayloadContext context, ServerPlayer player)
		{
			ColorHandler.trySetPlayerColor(player, ColorHandler.BuiltinColors.getColor(colorIndex));
		}
	}
	
	public record SelectRGB(int color) implements MSPacket.PlayToServer
	{
		
		public static final Type<SelectRGB> ID = new Type<>(Minestuck.id("player_color/select_rgb"));
		public static final StreamCodec<ByteBuf, SelectRGB> STREAM_CODEC = ByteBufCodecs.INT.map(SelectRGB::new, SelectRGB::color);
		
		@Override
		public Type<? extends CustomPacketPayload> type()
		{
			return ID;
		}
		
		@Override
		public void execute(IPayloadContext context, ServerPlayer player)
		{
			ColorHandler.trySetPlayerColor(player, color);
		}
	}
}
