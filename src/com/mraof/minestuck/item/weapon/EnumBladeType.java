package com.mraof.minestuck.item.weapon;

public enum EnumBladeType 
{

	SORD(0, 59, 2.0F, -1, 5),
	NINJA(1, 100, 4.0F, 1, 15),
	KATANA(3, 3000, 4.0F, 3, 30),
	CALEDSCRATCH(3, 1561, 4.0F, 3, 30),
	REGISWORD(2, 256, 4.0F, 3, 1),
	DERINGER(3, 1561, 4.0F, 4, 30),
	SCARLET(3, 2000, 4.0F, 4, 30),
	DOGG(3, 1000, 4.0F, 2, 30);
	
	private final int harvestLevel;
    private final int maxUses;
    private final float efficiencyOnProperMaterial;
    private final int damageVsEntity;
    private final int enchantability;
	
	private EnumBladeType(int harvestLevel, int maxUses, float efficiencyOnProperMaterial, int damageVsEntity, int enchantability) 
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
