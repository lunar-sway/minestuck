package com.mraof.minestuck.item.weapon;

public enum EnumCaneType 
{
	CANE(100, 2, 15, "cane"),
	SPEAR(300, 6, 13, "spearCane"),
	DRAGON(300, 8, 20, "dragonCane");
	
	
	private final int maxUses;
	private final int damageVsEntity;
	private final int enchantability;
	private final String name;
	
	private EnumCaneType(int maxUses, int damageVsEntity, int enchantability, String name)
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