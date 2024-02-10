package com.mraof.minestuck.entity;

import com.mraof.minestuck.util.Dialogue;
import com.mraof.minestuck.util.DialogueManager;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;

public interface DialogueEntity
{
	String DIALOGUE_NBT_TAG = "Dialogue";
	
	Dialogue getDialogue();
	
	void setDialogue(String location);
	
	default Dialogue getDialogueFromEntity(LivingEntity entity)
	{
		CompoundTag entityNBT = entity.getPersistentData();
		if(entityNBT.contains(DIALOGUE_NBT_TAG))
		{
			String storedDialogue = entityNBT.getString(DIALOGUE_NBT_TAG);
			
			return dialogueFromLocation(storedDialogue);
		}
		
		return null;
	}
	
	default Dialogue dialogueFromLocation(String location)
	{
		return DialogueManager.getInstance().getDialogue(ResourceLocation.tryParse(location));
	}
}