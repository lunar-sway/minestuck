package com.mraof.minestuck.entity.consort;

import net.minecraft.entity.Entity;
import net.minecraft.util.text.TextFormatting;

public enum EnumConsort
{
	SALAMANDER(EntitySalamander.class, "salamander", TextFormatting.YELLOW),
	TURTLE(EntityTurtle.class, "turtle", TextFormatting.LIGHT_PURPLE),
	NAKAGATOR(EntityNakagator.class, "nakagator", TextFormatting.RED),
	IGUANA(EntityIguana.class, "iguana", TextFormatting.AQUA);
	
	private final Class<? extends EntityConsort> consortClass;
	private final String name;
	private final TextFormatting color;
	
	EnumConsort(Class<? extends EntityConsort> consort, String name, TextFormatting color)
	{
		consortClass = consort;
		this.color = color;
		this.name = name;
	}
	
	public boolean isConsort(Entity consort)
	{
		return consortClass.isInstance(consort);
	}
	
	public TextFormatting getColor()
	{
		return color;
	}
	
	public String getName()
	{
		return name;
	}
	
	public Class<? extends EntityConsort> getConsortClass()
	{
		return consortClass;
	}
	
	public enum MerchantType
	{
		NONE,
		SHADY,
		FOOD,
	}
}