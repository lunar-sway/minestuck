package com.mraof.minestuck.network;

import com.mraof.minestuck.client.ClientProxy;
import com.mraof.minestuck.client.gui.MSScreenFactories;
import com.mraof.minestuck.entity.dialogue.Dialogue;
import com.mraof.minestuck.entity.dialogue.DialogueEntity;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

public record DialogueScreenPacket(int entityID, Dialogue.NodeReference nodeReference, Dialogue.DialogueData dialogueData) implements MSPacket.PlayToClient
{
	@Override
	public void encode(FriendlyByteBuf buffer)
	{
		buffer.writeInt(entityID);
		nodeReference.write(buffer);
		dialogueData.write(buffer);
	}
	
	public static DialogueScreenPacket decode(FriendlyByteBuf buffer)
	{
		int entityID = buffer.readInt();
		var nodeReference = Dialogue.NodeReference.read(buffer);
		var dialogueData = Dialogue.DialogueData.read(buffer);
		
		return new DialogueScreenPacket(entityID, nodeReference, dialogueData);
	}
	
	@Override
	public void execute()
	{
		Player playerEntity = ClientProxy.getClientPlayer();
		if(playerEntity != null)
		{
			Entity entity = playerEntity.level().getEntity(entityID);
			if(entity instanceof LivingEntity livingEntity && entity instanceof DialogueEntity)
				MSScreenFactories.displayDialogueScreen(livingEntity, nodeReference, dialogueData);
		}
	}
}