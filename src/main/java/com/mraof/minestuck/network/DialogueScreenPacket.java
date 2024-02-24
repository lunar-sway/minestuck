package com.mraof.minestuck.network;

import com.mraof.minestuck.client.ClientProxy;
import com.mraof.minestuck.client.gui.MSScreenFactories;
import com.mraof.minestuck.entity.dialogue.DialogueEntity;
import com.mraof.minestuck.entity.dialogue.Dialogue;
import com.mraof.minestuck.util.DialogueManager;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

public class DialogueScreenPacket implements MSPacket.PlayToClient
{
	private final int entityID;
	private final ResourceLocation dialogueLocation;
	private final CompoundTag conditionChecks;
	
	public static DialogueScreenPacket createPacket(LivingEntity entity, Dialogue dialogue, CompoundTag conditionChecks)
	{
		return new DialogueScreenPacket(entity.getId(), dialogue.path(), conditionChecks);
	}
	
	public DialogueScreenPacket(int entityID, ResourceLocation dialogueLocation, CompoundTag conditionChecks)
	{
		this.entityID = entityID;
		this.dialogueLocation = dialogueLocation;
		this.conditionChecks = conditionChecks;
	}
	
	@Override
	public void encode(FriendlyByteBuf buffer)
	{
		buffer.writeInt(entityID);
		buffer.writeUtf(dialogueLocation.toString(), 500);
		
		buffer.writeNbt(conditionChecks);
	}
	
	public static DialogueScreenPacket decode(FriendlyByteBuf buffer)
	{
		int entityID = buffer.readInt();
		ResourceLocation dialogueLocation = ResourceLocation.tryParse(buffer.readUtf(500));
		
		CompoundTag nbt = buffer.readNbt();
		return new DialogueScreenPacket(entityID, dialogueLocation, nbt);
	}
	
	@Override
	public void execute()
	{
		Player playerEntity = ClientProxy.getClientPlayer();
		if(playerEntity != null)
		{
			Entity entity = playerEntity.level().getEntity(entityID);
			if(entity instanceof LivingEntity livingEntity && entity instanceof DialogueEntity)
				MSScreenFactories.displayDialogueScreen(livingEntity, DialogueManager.getInstance().getDialogue(dialogueLocation), conditionChecks);
		}
	}
}