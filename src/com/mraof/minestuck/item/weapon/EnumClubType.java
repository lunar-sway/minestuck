package com.mraof.minestuck.item.weapon;

public enum EnumClubType 
{
	DEUCE(3, 1024, 20.0F, 2, 15);
	
	private final int harvestLevel;
    private final int maxUses;
    private final float efficiencyOnProperMaterial;
    private final int damageVsEntity;
    private final int enchantability;
	
	private EnumClubType(int harvestLevel, int maxUses, float efficiencyOnProperMaterial, int damageVsEntity, int enchantability) 
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
