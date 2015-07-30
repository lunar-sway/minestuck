package com.mraof.minestuck.item.weapon;

public enum EnumSporkType 
{
	SPOON_WOOD(1, 64, 2.0F, 0, 5, true, "woodenSpoon"),
	SPOON_SILVER(2, 128, 3.0F, 1, 12, true, "silverSpoon"),
	CROCKER(3, 256, 4.0F, 2, 15, true, "crockerSpork"),
	SKAIA(3, 2048, 4.0F, 3, 10, false, "skaiaFork");
	
	private final int harvestLevel;
	private final int maxUses;
	private final float efficiencyOnProperMaterial;
	private final int damageVsEntity;
	private final int enchantability;
	private final boolean isSpoon;
	private final String name;
	
	private EnumSporkType(int harvestLevel, int maxUses, float efficiencyOnProperMaterial, int damageVsEntity, int enchantability, boolean isSpoon, String name) 
	{
		this.harvestLevel = harvestLevel;
		this.maxUses = maxUses;
		this.efficiencyOnProperMaterial = efficiencyOnProperMaterial;
		this.damageVsEntity = damageVsEntity;
		this.enchantability = enchantability;
		this.isSpoon = isSpoon;
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
	public boolean getIsSpoon()
	{
		return isSpoon;
	}
	
	public String getUnlocalizedName()
	{
		return name;
	}
}
