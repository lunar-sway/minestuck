package com.mraof.minestuck.network;

import com.mraof.minestuck.client.ClientProxy;
import com.mraof.minestuck.client.gui.MSScreenFactories;
import com.mraof.minestuck.entity.dialogue.Dialogue;
import com.mraof.minestuck.entity.dialogue.DialogueEntity;
import com.mraof.minestuck.util.DialogueManager;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

public record DialogueScreenPacket(int entityID, ResourceLocation dialogueLocation, CompoundTag conditionChecks, CompoundTag messageArgs) implements MSPacket.PlayToClient
{
	public static DialogueScreenPacket createPacket(LivingEntity entity, Dialogue dialogue, CompoundTag conditionChecks, CompoundTag messageArgs)
	{
		return new DialogueScreenPacket(entity.getId(), dialogue.path(), conditionChecks, messageArgs);
	}
	
	@Override
	public void encode(FriendlyByteBuf buffer)
	{
		buffer.writeInt(entityID);
		buffer.writeUtf(dialogueLocation.toString(), 500);
		
		buffer.writeNbt(conditionChecks);
		buffer.writeNbt(messageArgs);
	}
	
	public static DialogueScreenPacket decode(FriendlyByteBuf buffer)
	{
		int entityID = buffer.readInt();
		ResourceLocation dialogueLocation = ResourceLocation.tryParse(buffer.readUtf(500));
		
		CompoundTag conditionChecks = buffer.readNbt();
		CompoundTag messageArgs = buffer.readNbt();
		return new DialogueScreenPacket(entityID, dialogueLocation, conditionChecks, messageArgs);
	}
	
	@Override
	public void execute()
	{
		Player playerEntity = ClientProxy.getClientPlayer();
		if(playerEntity != null)
		{
			Entity entity = playerEntity.level().getEntity(entityID);
			if(entity instanceof LivingEntity livingEntity && entity instanceof DialogueEntity)
				MSScreenFactories.displayDialogueScreen(livingEntity, DialogueManager.getInstance().getDialogue(dialogueLocation), conditionChecks, messageArgs);
		}
	}
}