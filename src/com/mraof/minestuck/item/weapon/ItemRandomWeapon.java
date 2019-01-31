package com.mraof.minestuck.item.weapon;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;

/**
 * Created by mraof on 2017 January 18 at 6:33 PM.
 * Edited by Cibernet some day, idk which one.
 */



public class ItemRandomWeapon extends ItemWeapon
{
	private int maxRand = 7;
	
	public ItemRandomWeapon(int maxUses, double damageVsEntity, double weaponSpeed, int enchantability, int maxRand, String name)
	{
		this(maxUses, damageVsEntity, weaponSpeed, enchantability, name);
		this.maxRand = maxRand;
	}
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
        
        float rng = (float) (player.getRNG().nextInt(maxRand)+1) * (player.getRNG().nextInt(maxRand)+1);
        target.attackEntityFrom(source, rng );
        
        
        return super.hitEntity(itemStack, target, player);
    }
}
