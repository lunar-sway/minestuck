package com.mraof.minestuck.entity.consort;

import net.minecraft.util.ResourceLocation;

public class DialogueCard
{
	final String text;
	final String portraitResourcePath;
	final int textColor;
	
	public DialogueCard(String text, String portraitResourcePath, int textColor)
	{
		this.text = text;
		this.portraitResourcePath = portraitResourcePath;
		this.textColor = textColor;
	}
	
	public String getText()
	{
		return text;
	}
	
	public String getPortraitResourcePath()
	{
		return portraitResourcePath != null ? portraitResourcePath : "";
	}
	
	public int getTextColor()
	{
		return textColor;
	}
	
	public ResourceLocation getPortraitResource()
	{
		return portraitResourcePath != null && !portraitResourcePath.isEmpty() ? new ResourceLocation("minestuck", portraitResourcePath) : null;
	}
}