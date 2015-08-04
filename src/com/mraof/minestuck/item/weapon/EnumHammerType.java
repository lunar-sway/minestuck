package com.mraof.minestuck.item.weapon;

public enum EnumHammerType
{
	CLAW(0, 131, 2.0F, 1, 15),
	SLEDGE(2, 250, 4.0F, 2, 5),
	POGO(1, 400, 6.0F, 3, 14),
	TELESCOPIC(2, 1024, 8.0F, 4, 10),
	FEARNOANVIL(3, 2048, 12.0F, 5, 22),
	SCARLET(3, 2000, 4.0F, 6, 30),
	ZILLYHOO(3, 3000, 15.0F, 6, 30),
	POPAMATIC(3, 3000, 15.0F, -3, 30);

	private final int harvestLevel;
	private final int maxUses;
	private final float efficiencyOnProperMaterial;
	private final int damageVsEntity;
	private final int enchantability;

	private EnumHammerType(int harvestLevel, int maxUses, float efficiencyOnProperMaterial, int damageVsEntity, int enchantability) 
	{
		this.harvestLevel = harvestLevel;
		this.maxUses = maxUses;
		this.efficiencyOnProperMaterial = efficiencyOnProperMaterial;
		this.damageVsEntity = damageVsEntity;
		this.enchantability = enchantability;
	}

	public int getMaxUses() {
		return maxUses;
	}

	public float getEfficiencyOnProperMaterial() {
		return efficiencyOnProperMaterial;
	}

	public int getDamageVsEntity() {
		return damageVsEntity;
	}

	public int getHarvestLevel() {
		return harvestLevel;
	}

	public int getEnchantability() {
		return enchantability;
	}


}
