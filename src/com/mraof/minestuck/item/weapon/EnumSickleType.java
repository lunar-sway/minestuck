package com.mraof.minestuck.item.weapon;

public enum EnumSickleType 
{
	SICKLE(1, 131, 4.0F, 0, 5),
	HOMES(1, 200, 4.0F, 1, 10),
	REGISICKLE(2, 256, 4.0F, 2, 1),
	CLAW(3, 2048, 4.0F, 4, 15);
	
	private final int harvestLevel;
    private final int maxUses;
    private final float efficiencyOnProperMaterial;
    private final int damageVsEntity;
    private final int enchantability;
	
	private EnumSickleType(int harvestLevel, int maxUses, float efficiencyOnProperMaterial, int damageVsEntity, int enchantability) 
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
