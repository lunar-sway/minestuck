package com.mraof.minestuck.network;

import com.mraof.minestuck.entity.dialogue.Dialogue;
import com.mraof.minestuck.entity.dialogue.DialogueEntity;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

import java.util.Optional;

public record ResponseTriggerPacket(int responseIndex, Dialogue.NodeReference nodeReference, int entityID) implements MSPacket.PlayToServer
{
	public static ResponseTriggerPacket createPacket(int responseIndex, Dialogue.NodeReference nodeReference, LivingEntity entity)
	{
		return new ResponseTriggerPacket(responseIndex, nodeReference, entity.getId());
	}
	
	@Override
	public void encode(FriendlyByteBuf buffer)
	{
		buffer.writeInt(this.responseIndex);
		this.nodeReference.write(buffer);
		buffer.writeInt(this.entityID);
	}
	
	public static ResponseTriggerPacket decode(FriendlyByteBuf buffer)
	{
		int responseIndex = buffer.readInt();
		var nodeReference = Dialogue.NodeReference.read(buffer);
		int entityID = buffer.readInt();
		
		return new ResponseTriggerPacket(responseIndex, nodeReference, entityID);
	}
	
	@Override
	public void execute(ServerPlayer player)
	{
		Entity entity = player.level().getEntity(entityID);
		if(entity instanceof LivingEntity livingEntity && entity instanceof DialogueEntity dialogueEntity)
		{
			Optional<Dialogue.Node> optionalNode = dialogueEntity.getDialogueComponent().validateAndGetCurrentNode(this.nodeReference, player);
			dialogueEntity.getDialogueComponent().clearCurrentNode(player);
			optionalNode.flatMap(node -> node.getResponseIfValid(this.responseIndex))
					.ifPresent(response -> response.trigger(livingEntity, player));
		}
	}
	
}