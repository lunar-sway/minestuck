package com.mraof.minestuck.network;

import com.mraof.minestuck.player.PlayerSavedData;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;

public class RGBColorSelectPacket implements MSPacket.PlayToServer
{
	private final int color;
	
	public RGBColorSelectPacket(int color)
	{
		this.color = color;
	}
	
	@Override
	public void encode(FriendlyByteBuf buffer)
	{
		buffer.writeInt(color);
	}
	
	public static RGBColorSelectPacket decode(FriendlyByteBuf buffer)
	{
		return new RGBColorSelectPacket(buffer.readInt());
	}
	
	@Override
	public void execute(ServerPlayer player)
	{
		PlayerSavedData.getData(player).trySetColor(color);
	}
}