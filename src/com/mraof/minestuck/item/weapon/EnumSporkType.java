package com.mraof.minestuck.item.weapon;

public enum EnumSporkType 
{
	SPOON_WOOD(59, 2, 5, true, "woodenSpoon"),
	SPOON_SILVER(250, 3, 12, true, "silverSpoon"),
	CROCKER(512, 6, 15, true, "crockerSpork"),
	SKAIA(2048, 8, 10, false, "skaiaFork");
	
	private final int maxUses;
	private final int damageVsEntity;
	private final int enchantability;
	private final boolean isSpoon;
	private final String name;
	
	private EnumSporkType(int maxUses, int damageVsEntity, int enchantability, boolean isSpoon, String name) 
	{
		this.maxUses = maxUses;
		this.damageVsEntity = damageVsEntity;
		this.enchantability = enchantability;
		this.isSpoon = isSpoon;
		this.name = name;
	}
	
	public int getMaxUses() 
	{
		return maxUses;
	}
	
	public int getDamageVsEntity() 
	{
		return damageVsEntity;
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