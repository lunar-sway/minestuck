package com.mraof.minestuck.item.weapon;

import com.mraof.minestuck.player.EnumAspect;
import com.mraof.minestuck.world.storage.PlayerSavedData;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.IItemTier;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.world.World;

import javax.annotation.Nullable;

import static com.mraof.minestuck.player.EnumAspect.BREATH;

public class AspectBasedEffectWeaponItem extends WeaponItem
{
    private EnumAspect aspect;
    private Effect effect;
    private int duration;
    private int effectTier;
    
    public AspectBasedEffectWeaponItem(IItemTier tier, int attackDamageIn, float attackSpeedIn, float efficiency, EnumAspect aspect, Effect effect, int duration, int effectTier, @Nullable MSToolType toolType, Properties builder)
    {
        super(tier, attackDamageIn, attackSpeedIn, efficiency, toolType, builder);
        this.aspect = aspect;
        this.effect = effect;
        this.duration = duration;
        this.effectTier = effectTier;
    }
    
    @Override
    public void inventoryTick(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected)
    {
        if(entityIn instanceof ServerPlayerEntity)
        {
            if (PlayerSavedData.getData((ServerPlayerEntity) entityIn).getTitle().getHeroAspect() == aspect && isSelected)
            {
                ((ServerPlayerEntity) entityIn).addPotionEffect(new EffectInstance(effect, duration, effectTier));
            }
        }
    }
}
