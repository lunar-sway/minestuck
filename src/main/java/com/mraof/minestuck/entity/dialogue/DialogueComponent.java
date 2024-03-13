package com.mraof.minestuck.entity.dialogue;

import com.mraof.minestuck.advancements.MSCriteriaTriggers;
import com.mraof.minestuck.entity.consort.ConsortEntity;
import com.mraof.minestuck.util.DialogueManager;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;

import javax.annotation.Nullable;

public final class DialogueComponent
{
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
		Dialogue dialogue = getActiveOrRandomDialogue(entity);
		if(dialogue == null)
			return;
		
		if(entity instanceof ConsortEntity consort)
			MSCriteriaTriggers.CONSORT_TALK.trigger(serverPlayer, dialogue.lookupId().toString(), consort);
		
		Dialogue.openScreen(entity, serverPlayer, dialogue);
	}
	
	@Nullable
	private Dialogue getActiveOrRandomDialogue(LivingEntity entity)
	{
		Dialogue dialogue = null;
		
		if(this.activeDialogue != null)
			dialogue = DialogueManager.getInstance().getDialogue(this.activeDialogue);
		
		if(dialogue == null)
		{
			dialogue = DialogueManager.getInstance().doRandomDialogue(entity);
			
			this.hasGeneratedOnce = true;
			if(dialogue != null)
				this.activeDialogue = dialogue.lookupId();
		}
		
		return dialogue;
	}
	
}
