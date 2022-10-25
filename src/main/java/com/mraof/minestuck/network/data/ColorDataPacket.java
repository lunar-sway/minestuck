package com.mraof.minestuck.network.data;

import com.mraof.minestuck.network.PlayToClientPacket;
import com.mraof.minestuck.player.ClientPlayerData;
import net.minecraft.network.FriendlyByteBuf;

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
	public void encode(FriendlyByteBuf buffer)
	{
		if(color != NO_COLOR)
			buffer.writeInt(color);
	}
	
	public static ColorDataPacket decode(FriendlyByteBuf buffer)
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
		ClientPlayerData.handleDataPacket(this);
	}
	
	public boolean hasNoColor()
	{
		return color == NO_COLOR;
	}
	
	public int getColor()
	{
		return color;
	}
}