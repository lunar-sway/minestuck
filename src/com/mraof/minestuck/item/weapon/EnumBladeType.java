package com.mraof.minestuck.item.weapon;

public enum EnumBladeType 
{

	SORD(59, -1, 5),
	NINJA(250, 2, 15),
	KATANA(2200, 4, 20),
	CALEDSCRATCH(1561, 3, 30),
	REGISWORD(812, 3, 10),
	DERINGER(1561, 4, 30),
	SCARLET(2000, 5, 30),
	DOGG(1000, 2, 30);
	
    private final int maxUses;
    private final int damageVsEntity;
    private final int enchantability;
	
	private EnumBladeType(int maxUses, int damageVsEntity, int enchantability) 
	{
        this.maxUses = maxUses;
        this.damageVsEntity = damageVsEntity;
        this.enchantability = enchantability;
	}
	
	public int getMaxUses() {
		return maxUses;
	}
	
	public int getDamageVsEntity() {
		return damageVsEntity;
	}
	
	public int getEnchantability() {
		return enchantability;
	}
	

}
