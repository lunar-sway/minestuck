package com.mraof.minestuck.item.weapon;

public enum EnumHammerType
{
	CLAW(0, 131, 1.0F, 4, 10, "clawHammer"),
	SLEDGE(2, 250, 4.0F, 5, 8, "sledgeHammer"),
	POGO(1, 400, 2.0F, 6, 8, "pogoHammer"),
	TELESCOPIC(2, 1024, 5.0F, 7, 15, "telescopicSassacrusher"),
	FEARNOANVIL(3, 2048, 7.0F, 8, 12, "fearNoAnvil"),
	SCARLET(3, 2000, 4.0F, 9, 16, "scarletZillyhoo"),
	ZILLYHOO(4, 3000, 15.0F, 9, 30, "zillyhooHammer"),
	POPAMATIC(4, 3000, 15.0F, 0, 30, "popamaticVrillyhoo");
	
	private final int harvestLevel;
	private final int maxUses;
	private final float efficiencyOnProperMaterial;
	private final int damageVsEntity;
	private final int enchantability;
	private final String name;
	
	private EnumHammerType(int harvestLevel, int maxUses, float efficiencyOnProperMaterial, int damageVsEntity, int enchantability, String name) 
	{
		this.harvestLevel = harvestLevel;
		this.maxUses = maxUses;
		this.efficiencyOnProperMaterial = efficiencyOnProperMaterial;
		this.damageVsEntity = damageVsEntity;
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
	
	public int getDamageVsEntity()
	{
		return damageVsEntity;
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