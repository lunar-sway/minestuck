package com.mraof.minestuck.item.weapon;

public enum EnumClubType 
{
	DEUCE(1024, 4, 15, "deuceClub");
	
	private final int maxUses;
	private final int damageVsEntity;
	private final int enchantability;
	private final String name;
	
	private EnumClubType(int maxUses, int damageVsEntity, int enchantability, String name) 
	{
		this.maxUses = maxUses;
		this.damageVsEntity = damageVsEntity;
		this.enchantability = enchantability;
		this.name = name;
	}
	
	public int getMaxUses()
	{
		return maxUses;
	}
	
	public int getDamageVsEntity()
	{
		return damageVsEntity;
	}
	
	public int getEnchantability()
	{
		return enchantability;
	}
	
	public String getName()
	{
		return name;
	}
}