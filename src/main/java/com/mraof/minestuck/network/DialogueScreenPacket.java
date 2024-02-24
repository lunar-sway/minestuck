package com.mraof.minestuck.network;

import com.mraof.minestuck.client.ClientProxy;
import com.mraof.minestuck.client.gui.MSScreenFactories;
import com.mraof.minestuck.entity.dialogue.DialogueEntity;
import com.mraof.minestuck.entity.dialogue.Dialogue;
import com.mraof.minestuck.util.DialogueManager;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

public class DialogueScreenPacket implements MSPacket.PlayToClient
{
	private final int entityID;
	private final ResourceLocation dialogueLocation;
	
	public static DialogueScreenPacket createPacket(LivingEntity entity, Dialogue dialogue)
	{
		return new DialogueScreenPacket(entity.getId(), dialogue.path());
	}
	
	public DialogueScreenPacket(int entityID, ResourceLocation dialogueLocation)
	{
		this.entityID = entityID;
		this.dialogueLocation = dialogueLocation;
	}
	
	@Override
	public void encode(FriendlyByteBuf buffer)
	{
		buffer.writeInt(entityID);
		buffer.writeUtf(dialogueLocation.toString(), 500);
	}
	
	public static DialogueScreenPacket decode(FriendlyByteBuf buffer)
	{
		return new DialogueScreenPacket(buffer.readInt(), ResourceLocation.tryParse(buffer.readUtf(500)));
	}
	
	@Override
	public void execute()
	{
		Player playerEntity = ClientProxy.getClientPlayer();
		if(playerEntity != null)
		{
			Entity entity = playerEntity.level().getEntity(entityID);
			if(entity instanceof LivingEntity livingEntity && entity instanceof DialogueEntity)
				MSScreenFactories.displayDialogueScreen(livingEntity, playerEntity, DialogueManager.getInstance().getDialogue(dialogueLocation));
		}
	}
}