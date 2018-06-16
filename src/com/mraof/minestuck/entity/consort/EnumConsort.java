package com.mraof.minestuck.entity.consort;

import net.minecraft.entity.Entity;
import net.minecraft.util.text.TextFormatting;

import java.util.Random;

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
	
	public static MerchantType getRandomMerchant(Random rand)
	{
		float f = rand.nextFloat();
		if(f < 0.4f)
			return MerchantType.FOOD;
		else return MerchantType.GENERAL;
	}
	
	public enum MerchantType
	{
		NONE,
		SHADY,
		FOOD,
		GENERAL,
		;
		public static MerchantType getFromString(String str)
		{
			for(MerchantType type : MerchantType.values())
				if(type.name().toLowerCase().equals(str))
					return type;
			throw new IllegalArgumentException("Invalid merchant type " + str);
		}
	}
}