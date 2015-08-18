package com.mraof.minestuck.item.weapon;

public enum EnumSickleType 
{
	SICKLE(220, 4, 8, "sickle"),
	HOMES(400, 6, 10, "homesSmellYaLater"),
	REGISICKLE(812, 7, 5, "regiSickle"),
	CLAW(2048, 8, 15, "clawSickle");
	
	private final int maxUses;
	private final int damageVsEntity;
	private final int enchantability;
	private final String name;
	
	private EnumSickleType(int maxUses, int damageVsEntity, int enchantability, String name) 
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