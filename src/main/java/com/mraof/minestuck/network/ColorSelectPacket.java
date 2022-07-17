package com.mraof.minestuck.network;

import com.mraof.minestuck.util.ColorHandler;
import com.mraof.minestuck.world.storage.PlayerSavedData;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;

public class ColorSelectPacket implements PlayToServerPacket
{
	private final int colorIndex;
	
	public ColorSelectPacket(int colorIndex)
	{
		this.colorIndex = colorIndex;
	}
	
	@Override
	public void encode(FriendlyByteBuf buffer)
	{
		buffer.writeInt(colorIndex);
	}
	
	public static ColorSelectPacket decode(FriendlyByteBuf buffer)
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