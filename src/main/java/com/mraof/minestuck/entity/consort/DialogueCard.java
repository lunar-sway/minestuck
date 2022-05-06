package com.mraof.minestuck.entity.consort;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;

public class DialogueCard
{
	final ITextComponent text;
	final String portraitResourcePath;
	final int textColor;
	
	public DialogueCard(ITextComponent text, String portraitResourcePath, int textColor)
	{
		this.text = text;
		this.portraitResourcePath = portraitResourcePath;
		this.textColor = textColor;
	}

	public DialogueCard(String text, String portraitResourcePath, int textColor)
	{
		this.text = new StringTextComponent(text);
		this.portraitResourcePath = portraitResourcePath;
		this.textColor = textColor;
	}
	
	public ITextComponent getText()
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