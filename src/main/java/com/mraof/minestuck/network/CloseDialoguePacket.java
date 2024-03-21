package com.mraof.minestuck.network;

import com.mraof.minestuck.client.gui.MSScreenFactories;
import net.minecraft.network.FriendlyByteBuf;

public record CloseDialoguePacket() implements MSPacket.PlayToClient
{
	@Override
	public void encode(FriendlyByteBuf buffer)
	{
	}
	
	public static CloseDialoguePacket decode(FriendlyByteBuf ignored)
	{
		return new CloseDialoguePacket();
	}
	
	@Override
	public void execute()
	{
		MSScreenFactories.closeDialogueScreen();
	}
}
