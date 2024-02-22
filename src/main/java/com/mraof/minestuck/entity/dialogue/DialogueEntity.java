package com.mraof.minestuck.entity.dialogue;

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
	
	default Dialogue dialogueFromLocation(ResourceLocation location)
	{
		return DialogueManager.getInstance().getDialogue(location);
	}
}