package com.mraof.minestuck.item.weapon;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.IItemTier;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.function.Supplier;

public class ElectricWeaponItem extends DualWeaponItem
{
    private final boolean electrocuteInWater;
    
    public ElectricWeaponItem(IItemTier tier, int attackDamageIn, float attackSpeedIn, float efficiency, boolean electrocuteInWater, Supplier<Item> otherItem, MSToolType toolType, Properties builder)
    {
        super(tier, attackDamageIn, attackSpeedIn, efficiency, otherItem, toolType, builder);
        this.electrocuteInWater = electrocuteInWater;
    }
    
    @Override
    public void inventoryTick(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected)
    {
        if(electrocuteInWater && isSelected && entityIn.isInWater() && entityIn instanceof LivingEntity)
        {
            stack.damageItem(70, ((LivingEntity) entityIn), entity -> entity.sendBreakAnimation(Hand.MAIN_HAND));
            ItemEntity weapon = new ItemEntity(entityIn.world, entityIn.getPosX(), entityIn.getPosY(), entityIn.getPosZ(), stack.copy());
            weapon.getItem().setCount(1);
            weapon.setPickupDelay(40);
            entityIn.world.addEntity(weapon);
            stack.shrink(1);
            
            entityIn.attackEntityFrom(DamageSource.LIGHTNING_BOLT, 5);
        }
        super.inventoryTick(stack, worldIn, entityIn, itemSlot, isSelected);
    }
    
    @Override
    public boolean hitEntity(ItemStack stack, LivingEntity target, LivingEntity attacker)
    {
        ItemStack held = target.getHeldItemMainhand();
        if (attacker.getRNG().nextFloat() < .05 && !held.isEmpty())
        {
            ItemEntity item = new ItemEntity(target.world, target.getPosX(), target.getPosY(), target.getPosZ(), held.copy());
            item.getItem().setCount(1);
            item.setPickupDelay(40);
            target.world.addEntity(item);
            held.shrink(1);
        }
        return super.hitEntity(stack, target, attacker);
    }
}
