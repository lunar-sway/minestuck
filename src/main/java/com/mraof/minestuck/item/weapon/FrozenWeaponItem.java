package com.mraof.minestuck.item.weapon;

import com.mraof.minestuck.item.MSItems;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.IItemTier;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundEvents;

import javax.annotation.Nullable;

public class FrozenWeaponItem extends WeaponItem
{
    public FrozenWeaponItem(IItemTier tier, int attackDamageIn, float attackSpeedIn, float efficiency, @Nullable MSToolType toolType, Properties builder)
    {
        super(tier, attackDamageIn, attackSpeedIn, efficiency, toolType, builder);
    }
    
    @Override
    public boolean hitEntity(ItemStack stack, LivingEntity target, LivingEntity attacker)
    {
        target.playSound(SoundEvents.BLOCK_GLASS_BREAK, 0.25F, 1.5F);
        if (attacker.getRNG().nextFloat() < .10 && attacker instanceof PlayerEntity)
        {
            target.playSound(SoundEvents.BLOCK_GLASS_BREAK, 1.5F, 1F);
            target.attackEntityFrom(DamageSource.causePlayerDamage((PlayerEntity) attacker), 2);
            stack.damageItem(2, attacker, entity -> entity.sendBreakAnimation(Hand.MAIN_HAND));
            
            ItemEntity shardEntity = new ItemEntity(target.world, target.posX, target.posY, target.posZ, new ItemStack(MSItems.ICE_SHARD, 1));
            if (!target.world.isRemote)
                target.world.addEntity(shardEntity);
        }
        return super.hitEntity(stack, target, attacker);
    }
}
