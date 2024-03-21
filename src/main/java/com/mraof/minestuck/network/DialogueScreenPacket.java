package com.mraof.minestuck.network;

import com.mraof.minestuck.client.gui.MSScreenFactories;
import com.mraof.minestuck.entity.dialogue.Dialogue;
import net.minecraft.network.FriendlyByteBuf;

public record DialogueScreenPacket(int dialogueId, Dialogue.DialogueData dialogueData) implements MSPacket.PlayToClient
{
	@Override
	public void encode(FriendlyByteBuf buffer)
	{
		buffer.writeInt(this.dialogueId);
		this.dialogueData.write(buffer);
	}
	
	public static DialogueScreenPacket decode(FriendlyByteBuf buffer)
	{
		int dialogueId = buffer.readInt();
		var dialogueData = Dialogue.DialogueData.read(buffer);
		
		return new DialogueScreenPacket(dialogueId, dialogueData);
	}
	
	@Override
	public void execute()
	{
		MSScreenFactories.displayDialogueScreen(this.dialogueId, this.dialogueData);
	}
}