package com.mraof.minestuck.network;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.player.ClientPlayerData;
import com.mraof.minestuck.util.ColorHandler;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

@MethodsReturnNonnullByDefault
public final class PlayerColorPackets
{
	public record OpenSelection() implements MSPacket.PlayToClient
	{
		public static final ResourceLocation ID = Minestuck.id("player_color/open_selection");
		
		@Override
		public ResourceLocation id()
		{
			return ID;
		}
		
		@Override
		public void write(FriendlyByteBuf buffer)
		{
		}
		
		public static OpenSelection read(FriendlyByteBuf ignored)
		{
			return new OpenSelection();
		}
		
		@Override
		public void execute()
		{
			ClientPlayerData.handleDataPacket(this);
		}
	}
	
	public record Data(int color) implements MSPacket.PlayToClient
	{
		public static final ResourceLocation ID = Minestuck.id("player_color/data");
		
		@Override
		public ResourceLocation id()
		{
			return ID;
		}
		
		@Override
		public void write(FriendlyByteBuf buffer)
		{
			buffer.writeInt(color);
		}
		
		public static Data read(FriendlyByteBuf buffer)
		{
			int color = buffer.readInt();
			return new Data(color);
		}
		
		@Override
		public void execute()
		{
			ClientPlayerData.handleDataPacket(this);
		}
	}
	
	public record SelectIndex(int colorIndex) implements MSPacket.PlayToServer
	{
		public static final ResourceLocation ID = Minestuck.id("player_color/select_index");
		
		@Override
		public ResourceLocation id()
		{
			return ID;
		}
		
		@Override
		public void write(FriendlyByteBuf buffer)
		{
			buffer.writeInt(colorIndex);
		}
		
		public static SelectIndex read(FriendlyByteBuf buffer)
		{
			int color = buffer.readInt();
			
			return new SelectIndex(color);
		}
		
		@Override
		public void execute(ServerPlayer player)
		{
			ColorHandler.trySetPlayerColor(player, ColorHandler.BuiltinColors.getColor(colorIndex));
		}
	}
	
	public record SelectRGB(int color) implements MSPacket.PlayToServer
	{
		public static final ResourceLocation ID = Minestuck.id("player_color/select_rgb");
		
		@Override
		public ResourceLocation id()
		{
			return ID;
		}
		
		@Override
		public void write(FriendlyByteBuf buffer)
		{
			buffer.writeInt(color);
		}
		
		public static SelectRGB read(FriendlyByteBuf buffer)
		{
			return new SelectRGB(buffer.readInt());
		}
		
		@Override
		public void execute(ServerPlayer player)
		{
			ColorHandler.trySetPlayerColor(player, color);
		}
	}
}
