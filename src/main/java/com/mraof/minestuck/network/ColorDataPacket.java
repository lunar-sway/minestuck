package com.mraof.minestuck.network;

import com.mraof.minestuck.util.ColorCollector;
import com.mraof.minestuck.world.storage.ClientPlayerData;
import net.minecraft.network.PacketBuffer;

public class ColorDataPacket implements PlayToClientPacket
{
	private static final int NO_COLOR = -2;    //Can be removed if we remove -1 as a default color when colors are made to hexes
	
	private final int color;
	
	private ColorDataPacket(int color)
	{
		this.color = color;
	}
	
	public static ColorDataPacket selector()
	{
		return new ColorDataPacket(NO_COLOR);
	}
	
	public static ColorDataPacket data(int color)
	{
		return new ColorDataPacket(color);
	}
	
	@Override
	public void encode(PacketBuffer buffer)
	{
		if(color != NO_COLOR)
			buffer.writeInt(color);
	}
	
	public static ColorDataPacket decode(PacketBuffer buffer)
	{
		int color;
		if(buffer.readableBytes() > 0)
			color = buffer.readInt();
		else color = NO_COLOR;
		
		return new ColorDataPacket(color);
	}
	
	@Override
	public void execute()
	{
		if(color == -2)
		{
			ClientPlayerData.playerColor = ColorCollector.DEFAULT_COLOR;
			ClientPlayerData.displaySelectionGui = true;
		} else ClientPlayerData.playerColor = color;
	}
}