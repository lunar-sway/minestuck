package com.mraof.minestuck.item.weapon;

import com.mraof.minestuck.world.storage.PlayerSavedData;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.IItemTier;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.world.World;

import javax.annotation.Nullable;

import static com.mraof.minestuck.player.EnumAspect.BREATH;

public class BreathFloatWeaponItem extends WeaponItem
{
    public BreathFloatWeaponItem(IItemTier tier, int attackDamageIn, float attackSpeedIn, float efficiency, @Nullable MSToolType toolType, Properties builder)
    {
        super(tier, attackDamageIn, attackSpeedIn, efficiency, toolType, builder);
    }
    
    @Override
    public void inventoryTick(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected)
    {
        if(entityIn.world.isRemote && entityIn instanceof ServerPlayerEntity)
        {
            if (PlayerSavedData.getData((ServerPlayerEntity) entityIn).getTitle().getHeroAspect() == BREATH)
            {
                ((ServerPlayerEntity) entityIn).addPotionEffect(new EffectInstance(Effects.SLOW_FALLING, 20, 2));
            }
        }
    }
}
