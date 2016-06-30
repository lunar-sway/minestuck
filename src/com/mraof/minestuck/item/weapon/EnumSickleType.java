package com.mraof.minestuck.item.weapon;

public enum EnumSickleType 
{
	SICKLE(220, 4.0D, 8, "sickle"),
	HOMES(400, 5.5D, 10, "homesSmellYaLater"),
	FUDGE(450, 5.7D, 10, "fudgeSickle"),
	REGISICKLE(812, 6.0D, 5, "regiSickle"),
	CLAW(2048, 7.0D, 15, "clawSickle");
	
	private static final double DEFAULT_ATTACK_SPEED = -2.4D;
	private final int maxUses;
	private final double damageVsEntity;
	private final double attackSpeed;
	private final int enchantability;
	private final String name;
	
	private EnumSickleType(int maxUses, double damageVsEntity, int enchantability, String name)
	{
		this(maxUses, damageVsEntity, DEFAULT_ATTACK_SPEED, enchantability, name);
	}
	
	private EnumSickleType(int maxUses, double damageVsEntity, double attackSpeed, int enchantability, String name)
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