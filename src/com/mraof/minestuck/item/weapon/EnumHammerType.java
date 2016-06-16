package com.mraof.minestuck.item.weapon;

public enum EnumHammerType
{
	CLAW(0, 131, 1.0F, 4.0D, -2.4D, 10, "clawHammer"),
	SLEDGE(2, 250, 4.0F, 6.0D, 8, "sledgeHammer"),
	BLACKSMITH(2, 450, 3.5F, 7.0D, 10, "blacksmithHammer"),
	POGO(1, 400, 2.0F, 7.0D, 8, "pogoHammer"),
	TELESCOPIC(2, 1024, 5.0F, 9.0D, -2.9D, 15, "telescopicSassacrusher"),
	FEARNOANVIL(3, 2048, 7.0F, 10.0D, 12, "fearNoAnvil"),
	SCARLET(3, 2000, 4.0F, 11.0D, 16, "scarletZillyhoo"),
	ZILLYHOO(4, 3000, 15.0F, 11.0D, 30, "zillyhooHammer"),
	POPAMATIC(4, 3000, 15.0F, 0.0D, 30, "popamaticVrillyhoo");
	
	private static final double DEFAULT_ATTACK_SPEED = -2.8D;
	private final int harvestLevel;
	private final int maxUses;
	private final float efficiencyOnProperMaterial;
	private final double damageVsEntity;
	private final double attackSpeed;
	private final int enchantability;
	private final String name;
	
	private EnumHammerType(int harvestLevel, int maxUses, float efficiencyOnProperMaterial, double damageVsEntity, int enchantability, String name)
	{
		this(harvestLevel, maxUses, efficiencyOnProperMaterial, damageVsEntity, DEFAULT_ATTACK_SPEED, enchantability, name);
	}
	
	private EnumHammerType(int harvestLevel, int maxUses, float efficiencyOnProperMaterial, double damageVsEntity, double attackSpeed, int enchantability, String name)
	{
		this.harvestLevel = harvestLevel;
		this.maxUses = maxUses;
		this.efficiencyOnProperMaterial = efficiencyOnProperMaterial;
		this.damageVsEntity = damageVsEntity;
		this.attackSpeed = attackSpeed;
		this.enchantability = enchantability;
		this.name = name;
	}
	
	public int getMaxUses()
	{
		return maxUses;
	}
	
	public float getEfficiencyOnProperMaterial()
	{
		return efficiencyOnProperMaterial;
	}
	
	public double getDamageVsEntity()
	{
		return damageVsEntity;
	}
	
	public double getAttackSpeed()
	{
		return attackSpeed;
	}
	
	public int getHarvestLevel()
	{
		return harvestLevel;
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