package com.mraof.minestuck.item.weapon;

public enum EnumClubType 
{
	DEUCE(1024, 2.5D, 15, "deuceClub"),
	NIGHT(600, 4.0D, 20, "nightClub"),
	POGO(600, 3.5D, 15, "pogoClub"),
	BAT(750, 5.0D, 5, "metalBat"),
	SPIKED(500, 5.5D, 5, "spikedClub");
	
	private static final double DEFAULT_ATTACK_SPEED = -2.2D;
	private final int maxUses;
	private final double damageVsEntity;
	private final double attackSpeed;
	private final int enchantability;
	private final String name;
	
	private EnumClubType(int maxUses, double damageVsEntity, int enchantability, String name)
	{
		this(maxUses, damageVsEntity, DEFAULT_ATTACK_SPEED, enchantability, name);
	}
	
	private EnumClubType(int maxUses, double damageVsEntity, double attackSpeed, int enchantability, String name)
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