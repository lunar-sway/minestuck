package com.mraof.minestuck.item.weapon;

public enum EnumBattleaxeType 
{
	
	BANE(413, 9.0D, 15, "blacksmithBane"),
	SCRAXE(500, 10.0D, 20, "scraxe"),
	CROAK(2000, 11.0D, -2.9D, 30, "rubyCroak"),
	HEPH(3000, 11.0D, 30, "hephaestusLumber");
	
	private static final double DEFAULT_ATTACK_SPEED = -3.0D;
	private final int maxUses;
	private final double damageVsEntity;
	private final double attackSpeed;
	private final int enchantability;
	private final String name;
	
	private EnumBattleaxeType(int maxUses, double damageVsEntity, int enchantability, String name)
	{
		this(maxUses, damageVsEntity, DEFAULT_ATTACK_SPEED, enchantability, name);
	}
	
	private EnumBattleaxeType(int maxUses, double damageVsEntity, double attackSpeed, int enchantability, String name)
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