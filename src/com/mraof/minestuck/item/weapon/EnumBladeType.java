package com.mraof.minestuck.item.weapon;

public enum EnumBladeType 
{

	SORD(59, 3, 5, "sord"),
	CACTUS(104, 5, 10, "cactaceaeCutlass"),
	NINJA(250, 6, 15, "ninjaSword"),
	KATANA(2200, 8, 20, "katana"),
	FIREPOKER(250, 7, 15, "firePoker"),
	HOTHANDLE(350, 6, 15, "hotHandle"),
	CALEDSCRATCH(1561, 7, 30, "caledscratch"),
	CALEDFWLCH(1025, 7, 30, "caledfwlch"),
	REGISWORD(812, 7, 10, "regisword"),
	DERINGER(1561, 8, 30, "royalDeringer"),
	ZILLYWAIR(2500, 9, 30, "zillywairCutlass"),
	SCARLET(2000, 9, 30, "scarletRibbitar"),
	DOGG(1000, 6, 30, "doggMachete");
	
	private final int maxUses;
	private final int damageVsEntity;
	private final int enchantability;
	private final String name;
	
	private EnumBladeType(int maxUses, int damageVsEntity, int enchantability, String name) 
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