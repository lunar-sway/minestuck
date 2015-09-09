package com.mraof.minestuck.item.weapon;

public enum EnumDiceType 
{

	NormalDice(51,6,3,6,"Dice"),
	FlurineOctet(67,15,6,8,"Flurine Octet");
	
	private final int maxUses;
	private final int damageVsEntity;
	private final int enchantability;
	private final int probibility;
	private final String name;
	
	private EnumDiceType(int maxUses, int damageVsEntity, int enchantability,int probibility, String name) 
	{
		this.maxUses = maxUses;
		this.damageVsEntity = damageVsEntity;
		this.enchantability = enchantability;
		this.name = name;
		this.probibility=probibility;
	}
	
	public int getMaxUses()
	{
		return maxUses;
	}
	
	public int getDamageVsEntity()
	{
		int damage=damageVsEntity;
		damage+=(Math.ceil( Math.random()*probibility)-(probibility/2));
		return damage;
	}

	
	public int getEnchantability()
	{
		return enchantability;
	}
	
	public String getName()
	{
		return name;
	}
	public int getProbibility(){
		return probibility;
	}
}