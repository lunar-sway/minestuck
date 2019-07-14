package com.mraof.minestuck.entity.consort;

import net.minecraft.entity.Entity;
import net.minecraft.util.text.TextFormatting;

import java.util.Random;

public enum EnumConsort
{
	SALAMANDER(SalamanderEntity.class, "salamander", TextFormatting.YELLOW),
	TURTLE(TurtleEntity.class, "turtle", TextFormatting.LIGHT_PURPLE),
	NAKAGATOR(NakagatorEntity.class, "nakagator", TextFormatting.RED),
	IGUANA(IguanaEntity.class, "iguana", TextFormatting.AQUA);
	
	private final Class<? extends ConsortEntity> consortClass;
	private final String name;
	private final TextFormatting color;
	
	EnumConsort(Class<? extends ConsortEntity> consort, String name, TextFormatting color)
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
	
	public Class<? extends ConsortEntity> getConsortClass()
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