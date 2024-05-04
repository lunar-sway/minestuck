package com.mraof.minestuck.network;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.client.gui.MSScreenFactories;
import com.mraof.minestuck.player.Title;
import com.mraof.minestuck.skaianet.TitleSelectionHook;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

public class TitleSelectPacket implements MSPacket.PlayToBoth
{
	public static final ResourceLocation ID = Minestuck.id("title_select");
	
	private final Title title;
	
	public TitleSelectPacket()
	{
		title = null;
	}
	
	public TitleSelectPacket(Title title)
	{
		this.title = title;
	}
	
	@Override
	public ResourceLocation id()
	{
		return ID;
	}
	
	@Override
	public void write(FriendlyByteBuf buffer)
	{
		if(title != null)
		{
			title.write(buffer);
		}
	}
	
	public static TitleSelectPacket read(FriendlyByteBuf buffer)
	{
		if(buffer.readableBytes() > 0)
		{
			Title title = Title.read(buffer);
			return new TitleSelectPacket(title);
		} else return new TitleSelectPacket();
	}
	
	@Override
	public void execute()
	{
		MSScreenFactories.displayTitleSelectScreen(title);
	}
	
	@Override
	public void execute(ServerPlayer player)
	{
		TitleSelectionHook.handleTitleSelection(player, title);
	}
}