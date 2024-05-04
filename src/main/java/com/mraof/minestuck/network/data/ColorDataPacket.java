package com.mraof.minestuck.network.data;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.network.MSPacket;
import com.mraof.minestuck.player.ClientPlayerData;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

public class ColorDataPacket implements MSPacket.PlayToClient
{
	public static final ResourceLocation ID = Minestuck.id("color_data");
	
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
	public ResourceLocation id()
	{
		return ID;
	}
	
	@Override
	public void write(FriendlyByteBuf buffer)
	{
		if(color != NO_COLOR)
			buffer.writeInt(color);
	}
	
	public static ColorDataPacket read(FriendlyByteBuf buffer)
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