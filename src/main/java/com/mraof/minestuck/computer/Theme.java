package com.mraof.minestuck.computer;

import com.mraof.minestuck.Minestuck;
import net.minecraft.resources.ResourceLocation;

// TODO make this more data-driven
public enum Theme
{
	DEFAULT		(0x404040),
	PESTERCHUM	(0x404040),
	TROLLIAN	(0xFF0000),
	CROCKER		(0x000000),
	TYPHEUS		(0x6DAFAD),
	CETUS		(0x9081EE),
	HEPHAESTUS	(0xFFFFFF),
	ECHIDNA		(0x005DFF),
	JOY			(0x282828),
	SBURB_95 	(0x282828);
	
	private final ResourceLocation texture;
	private final int textColor;
	
	Theme(int textColor)
	{
		this.texture = locationForName(this.name().toLowerCase());
		this.textColor = textColor;
	}
	
	public Theme next()
	{
		return values()[(this.ordinal() + 1) % values().length];
	}
	
	public int getTextColor()
	{
		return this.textColor;
	}
	
	public ResourceLocation getTexture()
	{
		return this.texture;
	}
	
	public String getName()
	{
		return "theme." + this.name().toLowerCase() + ".name";
	}
	
	private static ResourceLocation locationForName(String name)
	{
		return new ResourceLocation(Minestuck.MOD_ID, "textures/gui/theme/" + name + ".png");
	}
}
