package com.mraof.minestuck.network.data;

import com.mraof.minestuck.network.PlayToClientPacket;
import com.mraof.minestuck.player.Title;
import com.mraof.minestuck.world.storage.ClientPlayerData;
import net.minecraft.network.PacketBuffer;

import java.util.Objects;

public class TitleDataPacket implements PlayToClientPacket
{
	private final Title title;
	
	private TitleDataPacket(Title title)
	{
		this.title = Objects.requireNonNull(title);
	}
	
	public static TitleDataPacket create(Title title)
	{
		return new TitleDataPacket(title);
	}
	
	@Override
	public void encode(PacketBuffer buffer)
	{
		title.write(buffer);
	}
	
	public static TitleDataPacket decode(PacketBuffer buffer)
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