package com.mraof.minestuck.network;

import com.mraof.minestuck.client.gui.DialogueScreen;
import com.mraof.minestuck.entity.consort.DialogueCard;
import net.minecraft.client.Minecraft;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class DialogueUpdatePacket implements PlayToClientPacket
{
	private final List<DialogueCard> dialogueCards;
	private final List<ITextComponent> localizedOptions;
	
	public DialogueUpdatePacket(List<DialogueCard> dialogueCards, List<ITextComponent> localizedOptions)
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
			buffer.writeUtf(item.getText().getString());
			buffer.writeUtf(item.getPortraitResourcePath());
			buffer.writeInt(item.getTextColor());
		}
		
		buffer.writeInt(localizedOptions.size());
		for(ITextComponent item : localizedOptions)
		{
			buffer.writeUtf(item.getString());
		}
	}
	
	public static DialogueUpdatePacket decode(PacketBuffer buffer)
	{
		int length = buffer.readInt();
		LinkedList<DialogueCard> cards = new LinkedList<>();
		for(int i = 0; i < length; i++)
		{
			cards.add(new DialogueCard(buffer.readUtf(), buffer.readUtf(), buffer.readInt()));
		}
		
		List<ITextComponent> localizedOptions = readStringList(buffer).stream().map(StringTextComponent::new).collect(Collectors.toList());
		
		return new DialogueUpdatePacket(cards, localizedOptions);
	}
	
	private static List<String> readStringList(PacketBuffer buffer)
	{
		int length = buffer.readInt();
		LinkedList<String> strings = new LinkedList<>();
		
		for(int i = 0; i < length; i++)
		{
			strings.add(buffer.readUtf());
		}
		
		return strings;
	}
	
	@Override
	public void execute()
	{
		Minecraft.getInstance().setScreen(new DialogueScreen(dialogueCards, localizedOptions));
	}
}