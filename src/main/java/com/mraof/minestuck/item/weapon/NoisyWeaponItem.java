package com.mraof.minestuck.item.weapon;

import com.mraof.minestuck.util.MSSoundEvents;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.IItemTier;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;

public class NoisyWeaponItem extends WeaponItem
{
    private SoundEvent sound;
    
    public NoisyWeaponItem(IItemTier tier, int attackDamageIn, float attackSpeedIn, float efficiency, SoundEvent sound, MSToolType toolType, Properties builder)
    {
        super(tier, attackDamageIn, attackSpeedIn, efficiency, toolType, builder);
        this.sound = sound;
    }
    
    @Override
    public boolean hitEntity(ItemStack stack, LivingEntity target, LivingEntity player)
    {
        target.playSound(sound, 1.0F, 1.0F);
        return super.hitEntity(stack, target, player);
    }
}
