package com.mraof.minestuck.network;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.player.PlayerSavedData;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

public class RGBColorSelectPacket implements MSPacket.PlayToServer
{
	public static final ResourceLocation ID = Minestuck.id("rgb_color_select");
	
	private final int color;
	
	public RGBColorSelectPacket(int color)
	{
		this.color = color;
	}
	
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
	
	public static RGBColorSelectPacket read(FriendlyByteBuf buffer)
	{
		return new RGBColorSelectPacket(buffer.readInt());
	}
	
	@Override
	public void execute(ServerPlayer player)
	{
		PlayerSavedData.getData(player).trySetColor(color);
	}
}