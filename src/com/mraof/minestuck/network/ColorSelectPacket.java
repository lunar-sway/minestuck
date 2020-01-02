package com.mraof.minestuck.network;

import com.mraof.minestuck.util.ColorCollector;
import com.mraof.minestuck.world.storage.PlayerSavedData;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;

public class ColorSelectPacket implements PlayToServerPacket
{
	private int colorIndex;
	
	public ColorSelectPacket(int colorIndex)
	{
		this.colorIndex = colorIndex;
	}
	
	@Override
	public void encode(PacketBuffer buffer)
	{
		buffer.writeInt(colorIndex);
	}
	
	public static ColorSelectPacket decode(PacketBuffer buffer)
	{
		int color = buffer.readInt();
		
		return new ColorSelectPacket(color);
	}
	
	@Override
	public void execute(ServerPlayerEntity player)
	{
		PlayerSavedData.getData(player).trySetColor(ColorCollector.getColor(colorIndex));
	}
}