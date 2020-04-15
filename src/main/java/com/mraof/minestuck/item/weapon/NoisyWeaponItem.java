package com.mraof.minestuck.item.weapon;

import com.mraof.minestuck.util.MSSoundEvents;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.IItemTier;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;

import java.util.function.Supplier;

public class NoisyWeaponItem extends WeaponItem
{
    private final Supplier<SoundEvent> sound;
    private final float volume;
    private final float pitch;
    
    public NoisyWeaponItem(IItemTier tier, int attackDamageIn, float attackSpeedIn, float efficiency, Supplier<SoundEvent> sound, MSToolType toolType, Properties builder)
    {
        this(tier, attackDamageIn, attackSpeedIn, efficiency, sound, 1.0F, 1.0F, toolType, builder);
    }
    
    public NoisyWeaponItem(IItemTier tier, int attackDamageIn, float attackSpeedIn, float efficiency, Supplier<SoundEvent> sound, float volume, float pitch, MSToolType toolType, Properties builder)
    {
        super(tier, attackDamageIn, attackSpeedIn, efficiency, toolType, builder);
        this.sound = sound;
        this.volume = volume;
        this.pitch = pitch;
    }
    
    @Override
    public boolean hitEntity(ItemStack stack, LivingEntity target, LivingEntity player)
    {
        target.playSound(sound.get(), volume, pitch);
        return super.hitEntity(stack, target, player);
    }
}
