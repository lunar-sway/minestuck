package com.mraof.minestuck.computer;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.util.ComputerTheme;
import com.mraof.minestuck.util.ComputerThemeDataManager;
import net.minecraft.resources.ResourceLocation;

import java.util.Optional;

// TODO make this more data-driven
public class ComputerThemes
{
	/*public static final ComputerTheme DEFAULT = new ComputerTheme(0x404040);
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
	
	ComputerThemes(int textColor)
	{
		this.texture = locationForName(this.name().toLowerCase());
		this.textColor = textColor;
	}*/
	
	/*public ComputerThemes next()
	{
		return values()[(this.ordinal() + 1) % values().length];
	}
	
	public int getTextColor()
	{
		Optional<Integer> price = ComputerThemeDataManager.getInstance().findTextColor(texture);
		return price.orElse(0xBFAA6D);
	}
	
	public ResourceLocation getTexture()
	{
		return this.texture;
	}*/
	
	/*public String getName()
	{
		return "theme." + this.name().toLowerCase() + ".name";
	}
	
	private static ResourceLocation locationForName(String name)
	{
		Optional<ResourceLocation> price = ComputerThemeDataManager.getInstance().findTexturePath(name);
		return price.orElse(new ResourceLocation(Minestuck.MOD_ID, "textures/gui/theme/pesterchum.png"));
		//return new ResourceLocation(Minestuck.MOD_ID, "textures/gui/theme/" + name + ".png");
	}*/
}
