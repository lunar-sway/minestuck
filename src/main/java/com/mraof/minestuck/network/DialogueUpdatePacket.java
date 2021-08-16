package com.mraof.minestuck.network;

import com.mraof.minestuck.client.gui.DialogueScreen;
import com.mraof.minestuck.entity.consort.DialogueCard;
import net.minecraft.client.Minecraft;
import net.minecraft.network.PacketBuffer;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class DialogueUpdatePacket implements PlayToClientPacket
{
	private final List<DialogueCard> dialogueCards;
	private final List<String> localizedOptions;
	
	public DialogueUpdatePacket(List<DialogueCard> dialogueCards, List<String> localizedOptions)
	{
		this.dialogueCards = dialogueCards != null ? dialogueCards : new ArrayList<>();
		this.localizedOptions = localizedOptions != null ? localizedOptions : new ArrayList<>();
	}
	
	@Override
	public void encode(PacketBuffer buffer)
	{
		buffer.writeInt(dialogueCards.size());
		for(DialogueCard item : dialogueCards)
		{
			buffer.writeString(item.getText());
			buffer.writeString(item.getPortraitResourcePath());
			buffer.writeInt(item.getTextColor());
		}
		
		buffer.writeInt(localizedOptions.size());
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
		
		List<String> localizedOptions = readStringList(buffer);
		
		return new DialogueUpdatePacket(cards, localizedOptions);
	}
	
	private static List<String> readStringList(PacketBuffer buffer)
	{
		int length = buffer.readInt();
		LinkedList<String> strings = new LinkedList<>();
		
		for(int i = 0; i < length; i++)
		{
			strings.add(buffer.readString());
		}
		
		return strings;
	}
	
	@Override
	public void execute()
	{
		Minecraft.getInstance().displayGuiScreen(new DialogueScreen(dialogueCards, localizedOptions));
	}
}