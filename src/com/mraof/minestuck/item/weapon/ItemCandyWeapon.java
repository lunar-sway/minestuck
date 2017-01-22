package com.mraof.minestuck.item.weapon;

import com.mraof.minestuck.entity.underling.EntityUnderling;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;

/**
 * Created by mraof on 2017 January 18 at 7:42 PM.
 */
public class ItemCandyWeapon extends ItemWeapon
{
    public ItemCandyWeapon(int maxUses, double damageVsEntity, double weaponSpeed, int enchantability, String name)
    {
        super(maxUses, damageVsEntity, weaponSpeed, enchantability, name);
    }

    @Override
    public boolean hitEntity(ItemStack itemStack, EntityLivingBase target, EntityLivingBase player)
    {
        if(target instanceof EntityUnderling)
        {
            ((EntityUnderling) target).dropCandy = true;
        }
        return super.hitEntity(itemStack, target, player);
    }
}
