package com.mraof.minestuck.item.weapon;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;

/**
 * Created by mraof on 2017 January 18 at 6:33 PM.
 */
public class ItemRandomWeapon extends ItemWeapon
{
    public ItemRandomWeapon(int maxUses, double damageVsEntity, double weaponSpeed, int enchantability, String name)
    {
        super(maxUses, damageVsEntity, weaponSpeed, enchantability, name);
    }

    @Override
    public boolean hitEntity(ItemStack itemStack, EntityLivingBase target, EntityLivingBase player)
    {
        DamageSource source;
        if(player instanceof EntityPlayer)
        {
            source = DamageSource.causePlayerDamage((EntityPlayer) player);
        }
        else
        {
            source = DamageSource.causeMobDamage(player);
        }
        
        float rng = (float) (player.getRNG().nextInt(7)+1) * (player.getRNG().nextInt(7)+1);
        target.attackEntityFrom(source, rng );
        
        
        return super.hitEntity(itemStack, target, player);
    }
}
