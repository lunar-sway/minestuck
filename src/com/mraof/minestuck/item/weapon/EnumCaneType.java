package com.mraof.minestuck.item.weapon;

public enum EnumCaneType 
{
	CANE(100, 2.0D, 15, "cane"),
	SPEAR(300, 4.0D, 13, "spearCane"),
	DRAGON(300, 6.5D, 20, "dragonCane");
	
	private static final double DEFAULT_ATTACK_SPEED = -2.0D;
	private final int maxUses;
	private final double damageVsEntity;
	private final double attackSpeed;
	private final int enchantability;
	private final String name;
	
	private EnumCaneType(int maxUses, double damageVsEntity, int enchantability, String name)
	{
		this(maxUses, damageVsEntity, DEFAULT_ATTACK_SPEED, enchantability, name);
	}
	
	private EnumCaneType(int maxUses, double damageVsEntity, double attackSpeed, int enchantability, String name)
	{
		this.maxUses = maxUses;
		this.damageVsEntity = damageVsEntity;
		this.attackSpeed = attackSpeed;
		this.enchantability = enchantability;
		this.name = name;
	}
	
	public int getMaxUses()
	{
		return maxUses;
	}
	
	public double getDamageVsEntity()
	{
		return damageVsEntity;
	}
	
	public double getAttackSpeed()
	{
		return attackSpeed;
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