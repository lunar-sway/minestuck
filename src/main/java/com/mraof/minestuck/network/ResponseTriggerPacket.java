package com.mraof.minestuck.network;

import com.mraof.minestuck.data.DialogueProvider;
import com.mraof.minestuck.entity.dialogue.Dialogue;
import com.mraof.minestuck.entity.dialogue.DialogueEntity;
import com.mraof.minestuck.entity.dialogue.Trigger;
import com.mraof.minestuck.util.DialogueManager;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

import java.util.List;

public record ResponseTriggerPacket(int responseIndex, ResourceLocation dialogueLocation, int entityID) implements MSPacket.PlayToServer
{
	public static ResponseTriggerPacket createPacket(int responseIndex, ResourceLocation dialogueLocation, LivingEntity entity)
	{
		return new ResponseTriggerPacket(responseIndex, dialogueLocation, entity.getId());
	}
	
	@Override
	public void encode(FriendlyByteBuf buffer)
	{
		buffer.writeInt(this.responseIndex);
		buffer.writeResourceLocation(dialogueLocation);
		buffer.writeInt(entityID);
	}
	
	public static ResponseTriggerPacket decode(FriendlyByteBuf buffer)
	{
		int responseIndex = buffer.readInt();
		ResourceLocation dialogueLocation = buffer.readResourceLocation();
		int entityID = buffer.readInt();
		return new ResponseTriggerPacket(responseIndex, dialogueLocation, entityID);
	}
	
	@Override
	public void execute(ServerPlayer player)
	{
		Entity entity = player.level().getEntity(entityID);
		if(entity instanceof LivingEntity livingEntity && entity instanceof DialogueEntity)
		{
			validateThenTrigger(player, livingEntity);
		}
	}
	
	/**
	 * Validation for the Response the client is trying to activate.
	 * A corresponding Response must be found within the Dialogue and then the Conditions of that Response must match.
	 */
	private void validateThenTrigger(ServerPlayer player, LivingEntity livingEntity)
	{
		Dialogue dialogue = DialogueManager.getInstance().getDialogue(dialogueLocation);
		if(dialogue == null)
			return;
		
		if(this.responseIndex < 0 || dialogue.responses().size() <= this.responseIndex)
			return;
		
		Dialogue.Response response = dialogue.responses().get(this.responseIndex);
		if(!response.conditions().testWithContext(livingEntity, player))
			return;
		
		ResourceLocation nextPath = response.nextDialoguePath();
		
		Dialogue nextDialogue = null;
		if(nextPath.equals(DialogueProvider.LOOP_NEXT_PATH))
			nextDialogue = dialogue;
		else if(nextPath != null && nextPath != DialogueProvider.EMPTY_NEXT_PATH)
			nextDialogue = DialogueManager.getInstance().getDialogue(nextPath);
		
		List<Trigger> triggers = response.triggers();
		for(Trigger trigger : triggers)
		{
			trigger.triggerEffect(livingEntity, player);
		}
		
		if(nextDialogue != null)
		{
			Dialogue.openScreen(livingEntity, player, nextDialogue);
		}
	}
}