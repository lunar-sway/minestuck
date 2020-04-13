package com.mraof.minestuck.item.weapon;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.IItemTier;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;

public class GivePotionEffectWeaponItem extends WeaponItem
{
    private Effect effect;
    private int duration;
    private int effectTier;
    
    public GivePotionEffectWeaponItem(IItemTier tier, int attackDamageIn, float attackSpeedIn, float efficiency, Effect effect, int duration, int effectTier, MSToolType toolType, Properties builder)
    {
        super(tier, attackDamageIn, attackSpeedIn, efficiency, toolType, builder);
        this.effect = effect;
        this.duration = duration;
        this.effectTier = effectTier;
    }
    
    @Override
    public boolean hitEntity(ItemStack stack, LivingEntity target, LivingEntity player)
    {
        if(!(target instanceof PlayerEntity) && !target.world.isRemote)
        {
            target.addPotionEffect(new EffectInstance(effect, duration, effectTier));
        }
        return super.hitEntity(stack, target, player);
    }
}
