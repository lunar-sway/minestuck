package com.mraof.minestuck.network;

import com.mraof.minestuck.entity.dialogue.DialogueEntity;
import com.mraof.minestuck.entity.dialogue.Trigger;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

public class DialogueTriggerPacket implements MSPacket.PlayToServer
{
	private final Trigger trigger;
	private final int entityID;
	
	public static DialogueTriggerPacket createPacket(Trigger trigger, LivingEntity entity)
	{
		return new DialogueTriggerPacket(trigger, entity.getId());
	}
	public DialogueTriggerPacket(Trigger trigger, int entityID)
	{
		this.trigger = trigger;
		this.entityID = entityID;
	}
	
	@Override
	public void encode(FriendlyByteBuf buffer)
	{
		trigger.write(buffer);
		buffer.writeInt(entityID);
	}
	
	public static DialogueTriggerPacket decode(FriendlyByteBuf buffer)
	{
		try {
			Trigger trigger = Trigger.read(buffer);
			return new DialogueTriggerPacket(trigger, buffer.readInt());
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		
		return null;
	}
	
	@Override
	public void execute(ServerPlayer player)
	{
		if(trigger == null)
			return;
		
		Entity entity = player.level().getEntity(entityID);
		if(entity instanceof LivingEntity livingEntity && entity instanceof DialogueEntity)
		{
			trigger.triggerEffect(livingEntity, player);
		}
	}
}