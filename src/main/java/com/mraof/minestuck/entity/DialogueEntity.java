package com.mraof.minestuck.entity;

import com.mraof.minestuck.util.Dialogue;
import com.mraof.minestuck.util.DialogueManager;
import net.minecraft.resources.ResourceLocation;

/**
 * Can be implemented by entities to help standardize storage/retrieval of dialogue
 */
public interface DialogueEntity
{
	String DIALOGUE_NBT_TAG = "Dialogue";
	
	Dialogue getDialogue();
	
	void setDialoguePath(ResourceLocation location);
	
	/*default Dialogue getDialogueFromEntity(LivingEntity entity)
	{
		CompoundTag entityNBT = entity.getPersistentData();
		if(entityNBT.contains(DIALOGUE_NBT_TAG))
		{
			String storedDialogue = entityNBT.getString(DIALOGUE_NBT_TAG);
			
			return dialogueFromLocation(storedDialogue);
		}
		
		return null;
	}*/
	
	default Dialogue dialogueFromLocation(ResourceLocation location)
	{
		return DialogueManager.getInstance().getDialogue(location);
	}
}