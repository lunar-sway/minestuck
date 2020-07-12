package com.mraof.minestuck.item.weapon;

import com.mraof.minestuck.item.MSItems;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.IItemTier;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import java.util.Random;

public class BaguetteWeaponItem extends ConsumableWeaponItem
{
    
    public BaguetteWeaponItem(IItemTier tier, int attackDamageIn, float attackSpeedIn, float efficiency, int healAmount, float saturationModifier, MSToolType toolType, Properties builder)
    {
        super(tier, attackDamageIn, attackSpeedIn, efficiency, healAmount, saturationModifier, toolType, builder);
    }
    
    @Override
    public boolean hitEntity(ItemStack itemStack, LivingEntity target, LivingEntity attacker)
    {
        ItemStack crumbs = new ItemStack(MSItems.BREADCRUMBS, 1);
        if(!target.world.isRemote)
        {
            ItemEntity item = new ItemEntity(target.world, target.posX, target.posY, target.posZ, crumbs);
            target.world.addEntity(item);
        }
        return super.hitEntity(itemStack, target, attacker);
    }
    
    @Override
    public ItemStack onItemUseFinish(ItemStack stack, World worldIn, LivingEntity entityLiving)
    {
        if(entityLiving instanceof PlayerEntity && !entityLiving.world.isRemote)
        {
            Random rand = new Random();
            int num = rand.nextInt(10);
            ItemStack crumbs = new ItemStack(MSItems.BREADCRUMBS, num);
            
            PlayerEntity entityplayer = (PlayerEntity)entityLiving;
            entityplayer.addItemStackToInventory(crumbs);
        }
        return super.onItemUseFinish(stack, worldIn, entityLiving);
    }
}
