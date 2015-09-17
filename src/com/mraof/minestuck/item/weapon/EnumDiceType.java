package com.mraof.minestuck.item.weapon;

public enum EnumDiceType 
{

	DICE(51, 6, 3, 6, "dice"),
	FLUORITE_OCTET(67, 15, 6, 8, "fluoriteOctet");
	
	private final int maxUses;
	private final int damageVsEntity;
	private final int enchantability;
	private final int probability;
	private final String name;
	
	private EnumDiceType(int maxUses, int damageVsEntity, int enchantability,int probibility, String name) 
	{
		this.maxUses = maxUses;
		this.damageVsEntity = damageVsEntity;
		this.enchantability = enchantability;
		this.name = name;
		this.probability = probibility;
	}
	
	public int getMaxUses()
	{
		return maxUses;
	}
	
	public int getDamageVsEntity()
	{
		int damage=damageVsEntity;
		damage+=(Math.ceil( Math.random()*probability)-(probability/2));
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
		return probability;
	}
}