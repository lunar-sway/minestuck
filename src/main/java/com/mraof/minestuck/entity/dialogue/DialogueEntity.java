package com.mraof.minestuck.entity.dialogue;

import net.minecraft.resources.ResourceLocation;

/**
 * Can be implemented by entities to help standardize storage/retrieval of dialogue
 */
public interface DialogueEntity
{
	void setDialoguePath(ResourceLocation location);
}