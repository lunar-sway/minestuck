package com.mraof.minestuck.computer.theme;

import com.mraof.minestuck.Minestuck;
import net.minecraft.resources.ResourceLocation;

/**
 * Official ComputerTheme instances
 */
public enum ComputerThemes
{
	DEFAULT		(),
	PESTERCHUM	(),
	TROLLIAN	(),
	CROCKER		(),
	TYPHEUS		(),
	CETUS		(),
	HEPHAESTUS	(),
	ECHIDNA		(),
	JOY			(),
	SBURB_95	();
	
	/**
	 * Used for short handed references
	 */
	public String getLowercaseName()
	{
		return this.name().toLowerCase();
	}
	
	/**
	 * Used for referencing actual theme. The translatable version stored in computers
	 */
	public String getLangLocation()
	{
		return "theme." + getLowercaseName() + ".name";
	}
	
	public ResourceLocation id()
	{
		return new ResourceLocation(Minestuck.MOD_ID, this.getLowercaseName());
	}
}
