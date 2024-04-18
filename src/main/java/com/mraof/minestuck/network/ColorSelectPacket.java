package com.mraof.minestuck.network;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.player.PlayerSavedData;
import com.mraof.minestuck.util.ColorHandler;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

public class ColorSelectPacket implements MSPacket.PlayToServer
{
	public static final ResourceLocation ID = Minestuck.id("color_select");
	
	private final int colorIndex;
	
	public ColorSelectPacket(int colorIndex)
	{
		this.colorIndex = colorIndex;
	}
	
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
	
	public static ColorSelectPacket read(FriendlyByteBuf buffer)
	{
		int color = buffer.readInt();
		
		return new ColorSelectPacket(color);
	}
	
	@Override
	public void execute(ServerPlayer player)
	{
		PlayerSavedData.getData(player).trySetColor(ColorHandler.getColor(colorIndex));
	}
}