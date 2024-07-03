package com.mraof.minestuck.network;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.player.ClientPlayerData;
import com.mraof.minestuck.player.Title;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

public record TitleDataPacket(Title title) implements MSPacket.PlayToClient
{
	public static final ResourceLocation ID = Minestuck.id("title_data");
	
	public static TitleDataPacket create(Title title)
	{
		return new TitleDataPacket(title);
	}
	
	@Override
	public ResourceLocation id()
	{
		return ID;
	}
	
	@Override
	public void write(FriendlyByteBuf buffer)
	{
		title.write(buffer);
	}
	
	public static TitleDataPacket read(FriendlyByteBuf buffer)
	{
		Title title = Title.read(buffer);
		
		return new TitleDataPacket(title);
	}
	
	@Override
	public void execute()
	{
		ClientPlayerData.handleDataPacket(this);
	}
	
	public Title getTitle()
	{
		return title;
	}
}
