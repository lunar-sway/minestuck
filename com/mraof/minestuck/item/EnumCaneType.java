package com.mraof.minestuck.item;

public enum EnumCaneType 
{
	CANE(0, 100, 2.0F, 0, 15),
	SPEAR(1, 300, 4.0F, 3, 10),
	DRAGON(1, 300, 4.0F, 4, 20);
	
	
	private final int harvestLevel;
	private final int maxUses;
	private final float efficiencyOnProperMaterial;
	private final int damageVsEntity;
	private final int enchantability;
	
	private EnumCaneType(int harvestLevel, int maxUses, float efficiencyOnProperMaterial, int damageVsEntity, int enchantability) 
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