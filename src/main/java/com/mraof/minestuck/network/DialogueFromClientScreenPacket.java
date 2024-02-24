package com.mraof.minestuck.network;

import com.mraof.minestuck.entity.dialogue.Dialogue;
import com.mraof.minestuck.util.DialogueManager;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;

/**
 * Packet is sent server side to perform validation on Conditions before returning client side
 */
public class DialogueFromClientScreenPacket implements MSPacket.PlayToServer
{
	private final int entityID;
	private final ResourceLocation dialogueLocation;
	
	public static DialogueFromClientScreenPacket createPacket(LivingEntity entity, Dialogue dialogue)
	{
		return new DialogueFromClientScreenPacket(entity.getId(), dialogue.path());
	}
	
	public DialogueFromClientScreenPacket(int entityID, ResourceLocation dialogueLocation)
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
	
	public static DialogueFromClientScreenPacket decode(FriendlyByteBuf buffer)
	{
		int entityID = buffer.readInt();
		ResourceLocation dialogueLocation = ResourceLocation.tryParse(buffer.readUtf(500));
		return new DialogueFromClientScreenPacket(entityID, dialogueLocation);
	}
	
	@Override
	public void execute(ServerPlayer player)
	{
		if(player.level().getEntity(entityID) instanceof LivingEntity livingEntity)
			Dialogue.openScreen(livingEntity, player, DialogueManager.getInstance().getDialogue(dialogueLocation));
	}
}