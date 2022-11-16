package com.mraof.minestuck.network;

import com.mraof.minestuck.client.gui.MSScreenFactories;
import com.mraof.minestuck.player.Title;
import com.mraof.minestuck.skaianet.TitleSelectionHook;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;

public class TitleSelectPacket implements PlayToBothPacket
{
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
	public void encode(FriendlyByteBuf buffer)
	{
		if(title != null)
		{
			title.write(buffer);
		}
	}
	
	public static TitleSelectPacket decode(FriendlyByteBuf buffer)
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