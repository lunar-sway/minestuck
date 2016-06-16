package com.mraof.minestuck.item.weapon;

public enum EnumBladeType 
{

	SORD(59, 2, 5, "sord"),
	CACTUS(104, 4, 10, "cactaceaeCutlass"),
	NINJA(250, 5, 15, "ninjaSword"),
	KATANA(2200, 7, 20, "katana"),
	FIREPOKER(250, 6, 15, "firePoker"),
	HOTHANDLE(350, 5, 15, "hotHandle"),
	CALEDSCRATCH(1561, 6, 30, "caledscratch"),
	CALEDFWLCH(1025, 6, 30, "caledfwlch"),
	REGISWORD(812, 6, 10, "regisword"),
	DERINGER(1561, 7, 30, "royalDeringer"),
	ZILLYWAIR(2500, 8, 30, "zillywairCutlass"),
	SCARLET(2000, 7, 30, "scarletRibbitar"),
	DOGG(1000, 5, 30, "doggMachete");
	
	private static final double DEFAULT_ATTACK_SPEED = -2.4D;
	private final int maxUses;
	private final double damageVsEntity;
	private final double attackSpeed;
	private final int enchantability;
	private final String name;
	
	private EnumBladeType(int maxUses, double damageVsEntity, int enchantability, String name)
	{
		this(maxUses, damageVsEntity, DEFAULT_ATTACK_SPEED, enchantability, name);
	}
	
	private EnumBladeType(int maxUses, double damageVsEntity, double attackSpeed, int enchantability, String name)
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