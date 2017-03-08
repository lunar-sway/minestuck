package com.mraof.minestuck.item.weapon;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;

/**
 * Created by mraof on 2017 January 18 at 6:24 PM.
 */
public class ItemFireWeapon extends ItemWeapon
{
    private final int duration;

    public ItemFireWeapon(int maxUses, double damageVsEntity, double weaponSpeed, int enchantability, String name, int duration)
    {
        super(maxUses, damageVsEntity, weaponSpeed, enchantability, name);
        this.duration = duration;
    }

    @Override
    public boolean hitEntity(ItemStack itemStack, EntityLivingBase target, EntityLivingBase player)
    {
        target.setFire(duration);
        return super.hitEntity(itemStack, target, player);
    }
}
