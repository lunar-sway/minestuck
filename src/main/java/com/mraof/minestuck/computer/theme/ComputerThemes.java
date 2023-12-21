package com.mraof.minestuck.computer.theme;

/**
 * Official ComputerTheme instances
 */
public enum ComputerThemes
{
	DEFAULT		(ComputerTheme.DEFAULT_TEXT_COLOR),
	PESTERCHUM	(0x404040),
	TROLLIAN	(0xFF0000),
	CROCKER		(0x000000),
	TYPHEUS		(0x6DAFAD),
	CETUS		(0x9081EE),
	HEPHAESTUS	(0xFFFFFF),
	ECHIDNA		(0x005DFF),
	JOY			(0x282828),
	SBURB_95	(0x282828);
	
	private final int textColor;
	
	ComputerThemes(int textColor)
	{
		this.textColor = textColor;
	}
	
	public int getTextColor()
	{
		return this.textColor;
	}
	
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
}
