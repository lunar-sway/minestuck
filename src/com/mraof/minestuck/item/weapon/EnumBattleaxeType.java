package com.mraof.minestuck.item.weapon;

public enum EnumBattleaxeType 
{
    BANE(413, 6, 15, "blacksmithBane"),
    SCRAXE(500, 7, 20, "scraxe"),
	CROAK(2000, 9, 30, "rubyCroak"),
	HEPH(3000, 10, 30, "hephaestusLumber");
    
    private final int maxUses;
    private final int damageVsEntity;
    private final int enchantability;
    private final String name;
    
    private EnumBattleaxeType(int maxUses, int damageVsEntity, int enchantability, String name) 
    {
        this.maxUses = maxUses;
        this.damageVsEntity = damageVsEntity;
        this.enchantability = enchantability;
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
    
    public String getName()
    {
        return name;
    }
}