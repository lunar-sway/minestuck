package com.mraof.minestuck.item.weapon;

import com.mraof.minestuck.entity.underling.UnderlingEntity;
import com.mraof.minestuck.item.MSItems;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.IItemTier;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

public class CandyCaneWeaponItem extends ConsumableWeaponItem
{
    
    public CandyCaneWeaponItem(IItemTier tier, int attackDamageIn, float attackSpeedIn, float efficiency, int healAmount, float saturationModifier, MSToolType toolType, Properties builder)
    {
        super(tier, attackDamageIn, attackSpeedIn, efficiency, healAmount, saturationModifier, toolType, builder);
    }
    
    @Override
    public boolean hitEntity(ItemStack itemStack, LivingEntity target, LivingEntity player)
    {
        if(target instanceof UnderlingEntity)
        {
            ((UnderlingEntity) target).dropCandy = true;
        }
        return super.hitEntity(itemStack, target, player);
    }
    
    @Override
    public ItemStack onItemUseFinish(ItemStack stack, World worldIn, LivingEntity livingEntity)
    {
        if(livingEntity instanceof PlayerEntity && !livingEntity.world.isRemote)
        {
            ((PlayerEntity) livingEntity).addItemStackToInventory(new ItemStack(MSItems.SHARP_CANDY_CANE, 1));
        }
        stack.damageItem(999, livingEntity, entity -> entity.sendBreakAnimation(Hand.MAIN_HAND));
        return super.onItemUseFinish(stack, worldIn, livingEntity);
    }
}
