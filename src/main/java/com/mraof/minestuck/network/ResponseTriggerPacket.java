package com.mraof.minestuck.network;

import com.mraof.minestuck.entity.dialogue.Dialogue;
import com.mraof.minestuck.entity.dialogue.DialogueEntity;
import com.mraof.minestuck.util.DialogueManager;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

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
		
		if(this.responseIndex < 0 || dialogue.node().responses().size() <= this.responseIndex)
			return;
		
		Dialogue.Response response = dialogue.node().responses().get(this.responseIndex);
		response.trigger(livingEntity, player, dialogue);
	}
}