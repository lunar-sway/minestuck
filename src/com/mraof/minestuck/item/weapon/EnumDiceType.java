package com.mraof.minestuck.item.weapon;

public enum EnumDiceType //Do not make dices available in survival before properly tweaking and fixing them
{

	DICE(51, 6, 3, 6, "dice"),
	FLUORITE_OCTET(67, 15, 6, 8, "fluoriteOctet");
	
	private static final double DEFAULT_ATTACK_SPEED = -2.4D;
	private final int maxUses;
	private final double damageVsEntity;
	private final double attackSpeed;
	private final int enchantability;
	private final int probability;
	private final String name;
	
	private EnumDiceType(int maxUses, double damageVsEntity, int enchantability, int probibility, String name)
	{
		this(maxUses, damageVsEntity, DEFAULT_ATTACK_SPEED, enchantability, probibility, name);
	}
	
	private EnumDiceType(int maxUses, double damageVsEntity, double attackSpeed, int enchantability, int probibility, String name)
	{
		this.maxUses = maxUses;
		this.damageVsEntity = damageVsEntity;
		this.attackSpeed = attackSpeed;
		this.enchantability = enchantability;
		this.name = name;
		this.probability = probibility;
	}
	
	public int getMaxUses()
	{
		return maxUses;
	}
	
	public double getDamageVsEntity()
	{
		double damage= damageVsEntity;
		damage+=(Math.ceil( Math.random()*probability)-(probability/2));
		return damage;
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
	public int getProbibility(){
		return probability;
	}
}