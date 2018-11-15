package com.mraof.minestuck.item.enums;

import net.minecraft.util.IStringSerializable;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public enum EnumShopPoster implements IStringSerializable
{
	FRAYMOTIFS(0,0,"fraymotifs","fraymotifs"),
	FOOD(1,1,"food","food"),
	HATS(2,2,"hats","hats"),
	GENERAL(3,3,"general","general"),
	CANDY(4,4,"candy","candy");
	
	
	private final String name;
    private final String unlocalizedName;
    private final int meta;
    private final int damage;
    private static final EnumShopPoster[] META_LOOKUP = new EnumShopPoster[values().length];
    private static final EnumShopPoster[] DMG_LOOKUP = new EnumShopPoster[values().length];
    
    private EnumShopPoster(int metaIn, int damageIn, String nameIn, String unlocalizedNameIn)
    {
    	name = nameIn;
    	unlocalizedName = unlocalizedNameIn;
    	meta = metaIn;
    	damage = damageIn;
    }
    
    public static EnumShopPoster byMetadata(int meta)
    {
        if (meta < 0 || meta >= META_LOOKUP.length)
        {
            meta = 0;
        }

        return META_LOOKUP[meta];
    }
    
    public static EnumShopPoster byDamage(int damage)
    {
        if (damage < 0 || damage >= DMG_LOOKUP.length)
        {
            damage = 0;
        }

        return DMG_LOOKUP[damage];
    }
    
    public String toString()
    {
        return this.unlocalizedName;
    }

    public String getName()
    {
        return this.name;
    }
	
    public int getMetadata()
    {
        return this.meta;
    }
    
    public int getDamage()
    {
        return this.damage;
    }

    
    @SideOnly(Side.CLIENT)
    public String getPosterName()
    {
        return this.name;
    }

    public String getUnlocalizedName()
    {
        return this.unlocalizedName;
    }
    
    static
    {
        for (EnumShopPoster enumdyecolor : values())
        {
            META_LOOKUP[enumdyecolor.getMetadata()] = enumdyecolor;
            DMG_LOOKUP[enumdyecolor.getDamage()] = enumdyecolor;
        }
    }
    
}


