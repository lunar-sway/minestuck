package com.mraof.minestuck.entity.consort;

import com.mraof.minestuck.entity.MSEntityTypes;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.util.text.TextFormatting;

import java.util.Random;

public enum EnumConsort	//TODO Could ideally be changed into a registry.
{
	SALAMANDER(MSEntityTypes.SALAMANDER, "salamander", TextFormatting.YELLOW),
	TURTLE(MSEntityTypes.TURTLE, "turtle", TextFormatting.LIGHT_PURPLE),
	NAKAGATOR(MSEntityTypes.NAKAGATOR, "nakagator", TextFormatting.RED),
	IGUANA(MSEntityTypes.IGUANA, "iguana", TextFormatting.AQUA);
	
	private final EntityType<? extends ConsortEntity> consortType;
	private final String name;
	private final TextFormatting color;
	
	EnumConsort(EntityType<? extends ConsortEntity> consort, String name, TextFormatting color)
	{
		consortType = consort;
		this.color = color;
		this.name = name;
	}
	
	public boolean isConsort(Entity consort)
	{
		return consortType.equals(consort.getType());
	}
	
	public TextFormatting getColor()
	{
		return color;
	}
	
	public String getName()
	{
		return name;
	}
	
	public EntityType<? extends ConsortEntity> getConsortType()
	{
		return consortType;
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