package com.mraof.minestuck.network;

import com.mraof.minestuck.client.gui.DialogueScreen;
import com.mraof.minestuck.entity.consort.DialogueCard;
import net.minecraft.client.Minecraft;
import net.minecraft.network.PacketBuffer;

import java.util.LinkedList;

public class DialogueUpdatePacket implements PlayToClientPacket
{
	private final DialogueCard[] dialogueCards;
	private final String[] localizedOptions;
	
	public DialogueUpdatePacket(DialogueCard[] dialogueCards, String[] localizedOptions)
	{
		this.dialogueCards = dialogueCards != null ? dialogueCards : new DialogueCard[0];
		this.localizedOptions = localizedOptions != null ? localizedOptions : new String[0];
	}
	
	@Override
	public void encode(PacketBuffer buffer)
	{
		buffer.writeInt(dialogueCards.length);
		for(DialogueCard item : dialogueCards)
		{
			buffer.writeString(item.getText());
			buffer.writeString(item.getPortraitResourcePath());
			buffer.writeInt(item.getTextColor());
		}
		
		buffer.writeInt(localizedOptions.length);
		for(String item : localizedOptions)
		{
			buffer.writeString(item);
		}
	}
	
	public static DialogueUpdatePacket decode(PacketBuffer buffer)
	{
		int length = buffer.readInt();
		LinkedList<DialogueCard> cards = new LinkedList<>();
		for(int i = 0; i < length; i++)
		{
			cards.add(new DialogueCard(buffer.readString(), buffer.readString(), buffer.readInt()));
		}
		
		String[] localizedOptions = readStringArray(buffer);
		
		return new DialogueUpdatePacket(cards.toArray(new DialogueCard[0]), localizedOptions);
	}
	
	private static String[] readStringArray(PacketBuffer buffer)
	{
		int length = buffer.readInt();
		LinkedList<String> strings = new LinkedList<>();
		
		for(int i = 0; i < length; i++)
		{
			strings.add(buffer.readString());
		}
		
		return strings.toArray(new String[0]);
	}
	
	@Override
	public void execute()
	{
		Minecraft.getInstance().displayGuiScreen(new DialogueScreen(dialogueCards, localizedOptions));
	}
}