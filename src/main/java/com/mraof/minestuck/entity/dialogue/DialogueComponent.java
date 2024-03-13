package com.mraof.minestuck.entity.dialogue;

import com.mraof.minestuck.advancements.MSCriteriaTriggers;
import com.mraof.minestuck.entity.consort.ConsortEntity;
import com.mraof.minestuck.util.DialogueManager;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;

public final class DialogueComponent
{
	private static final Logger LOGGER = LogManager.getLogger();
	
	@Nullable
	private ResourceLocation activeDialogue;
	private boolean hasGeneratedOnce = false;
	
	public void read(CompoundTag tag)
	{
		if(tag.contains("dialogue_id", CompoundTag.TAG_STRING))
		{
			this.activeDialogue = ResourceLocation.tryParse(tag.getString("dialogue_id"));
			this.hasGeneratedOnce = true;
		}
		else
			this.hasGeneratedOnce = tag.getBoolean("has_generated");
	}
	
	public CompoundTag write()
	{
		CompoundTag tag = new CompoundTag();
		if(this.activeDialogue != null)
			tag.putString("dialogue_id", this.activeDialogue.toString());
		
		tag.putBoolean("has_generated", this.hasGeneratedOnce);
		
		return tag;
	}
	
	public boolean hasGeneratedOnce()
	{
		return hasGeneratedOnce;
	}
	
	public void setDialogue(ResourceLocation dialogueId)
	{
		this.activeDialogue = dialogueId;
	}
	
	public void startDialogue(LivingEntity entity, ServerPlayer serverPlayer)
	{
		if(this.activeDialogue == null)
			generateNewDialogue(entity);
		
		if(this.activeDialogue == null)
			return;
		
		Dialogue dialogue = DialogueManager.getInstance().getDialogue(this.activeDialogue);
		if(dialogue == null)
		{
			LOGGER.warn("Unable to find dialogue with id {}", this.activeDialogue);
			this.activeDialogue = null;
			return;
		}
		
		if(entity instanceof ConsortEntity consort)
			MSCriteriaTriggers.CONSORT_TALK.trigger(serverPlayer, this.activeDialogue.toString(), consort);
		
		Dialogue.openScreen(entity, serverPlayer, dialogue);
	}
	
	private void generateNewDialogue(LivingEntity entity)
	{
		Dialogue dialogue = DialogueManager.getInstance().doRandomDialogue(entity);
		this.hasGeneratedOnce = true;
		if(dialogue != null)
			this.activeDialogue = dialogue.lookupId();
	}
}
