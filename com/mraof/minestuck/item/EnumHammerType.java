package com.mraof.minestuck.item;

public enum EnumHammerType {
	CLAW(0, 59, 2.0F, 0, 15),
    SLEDGE(1, 131, 4.0F, 1, 5),
    POGO(2, 250, 6.0F, 2, 14),
    TELESCOPIC(3, 1561, 8.0F, 3, 10),
    FEARNOANVIL(4, 2048, 12.0F, 4, 22),
	SCARLET(3, 2000, 4.0F, 6, 30),
    ZILLYHOO(5, 3000, 15.0F, 5, 30),
	POPAMATIC(5, 3000, 15.0F, 0, 30);
	
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
