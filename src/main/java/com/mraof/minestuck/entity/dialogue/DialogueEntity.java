package com.mraof.minestuck.entity.dialogue;

import net.minecraft.ChatFormatting;

public interface DialogueEntity
{
	DialogueComponent getDialogueComponent();
	
	ChatFormatting getChatColor();
	
	String getSpriteType();
}