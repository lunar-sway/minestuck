package com.mraof.minestuck.network;

import com.mraof.minestuck.entity.dialogue.Dialogue;
import com.mraof.minestuck.entity.dialogue.DialogueEntity;
import com.mraof.minestuck.entity.dialogue.Trigger;
import com.mraof.minestuck.util.DialogueManager;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

public record DialogueTriggerPacket(Trigger trigger, ResourceLocation dialogueLocation, int entityID) implements MSPacket.PlayToServer
{
	public static DialogueTriggerPacket createPacket(Trigger trigger, ResourceLocation dialogueLocation, LivingEntity entity)
	{
		return new DialogueTriggerPacket(trigger, dialogueLocation, entity.getId());
	}
	
	@Override
	public void encode(FriendlyByteBuf buffer)
	{
		trigger.write(buffer);
		buffer.writeResourceLocation(dialogueLocation);
		buffer.writeInt(entityID);
	}
	
	public static DialogueTriggerPacket decode(FriendlyByteBuf buffer)
	{
		Trigger trigger = Trigger.read(buffer);
		ResourceLocation dialogueLocation = buffer.readResourceLocation();
		int entityID = buffer.readInt();
		return new DialogueTriggerPacket(trigger, dialogueLocation, entityID);
	}
	
	@Override
	public void execute(ServerPlayer player)
	{
		if(trigger == null)
			return;
		
		Entity entity = player.level().getEntity(entityID);
		if(entity instanceof LivingEntity livingEntity && entity instanceof DialogueEntity)
		{
			validateThenTriggerEffect(player, livingEntity);
		}
	}
	
	/**
	 * Validation for the Trigger the client is trying to activate.
	 * A corresponding Trigger must be found within the Dialogue and then the Conditions of that Response must match.
	 */
	private void validateThenTriggerEffect(ServerPlayer player, LivingEntity livingEntity)
	{
		Dialogue dialogue = DialogueManager.getInstance().getDialogue(dialogueLocation);
		if(dialogue != null)
		{
			//TODO it is possible for an unrelated Trigger of the same type to be matched up and validated instead
			for(Dialogue.Response response : dialogue.responses())
				for(Trigger triggerIterate : response.triggers())
					if(triggerIterate.equals(trigger))
						if(response.conditions().testWithContext(livingEntity, player))
							trigger.triggerEffect(livingEntity, player);
		}
	}
}