package com.mraof.minestuck.item;

public enum EnumSporkType {
	CROCKER(3, 64, 4.0F, 1, 15, true),
	SKAIA(3, 2048, 4.0F, 3, 10, false	);
	
	private final int harvestLevel;
    private final int maxUses;
    private final float efficiencyOnProperMaterial;
    private final int damageVsEntity;
    private final int enchantability;
    private final boolean isSpoon;
	
	private EnumSporkType(int harvestLevel, int maxUses, float efficiencyOnProperMaterial, int damageVsEntity, int enchantability, boolean isSpoon) 
	{
		this.harvestLevel = harvestLevel;
        this.maxUses = maxUses;
        this.efficiencyOnProperMaterial = efficiencyOnProperMaterial;
        this.damageVsEntity = damageVsEntity;
        this.enchantability = enchantability;
        this.isSpoon = isSpoon;
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
	public boolean getIsSpoon()
	{
		return isSpoon;
	}
}
