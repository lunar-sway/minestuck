package com.mraof.minestuck.item.weapon;

import com.mraof.minestuck.item.MSItems;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.IItemTier;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

import java.util.Random;

public class BaguetteWeaponItem extends WeaponItem
{
    public BaguetteWeaponItem(IItemTier tier, int attackDamageIn, float attackSpeedIn, float efficiency, MSToolType toolType, Properties builder)
    {
        super(tier, attackDamageIn, attackSpeedIn, efficiency, toolType, builder);
    }
    
    @Override
    public boolean hitEntity(ItemStack itemStack, LivingEntity target, LivingEntity attacker)
    {
        ItemStack crumbs = new ItemStack(MSItems.BREADCRUMBS, 1);
        if(!target.world.isRemote) {
            ItemEntity item = new ItemEntity(target.world, target.posX, target.posY, target.posZ, crumbs);
            target.world.addEntity(item);
        }
        return super.hitEntity(itemStack, target, attacker);
    }
    
    @Override
    public ItemStack onItemUseFinish(ItemStack stack, World worldIn, LivingEntity entityLiving) {
        Random rand = new Random();
        int num = rand.nextInt(10);
        ItemStack crumbs = new ItemStack(MSItems.BREADCRUMBS, num);
        if(entityLiving instanceof PlayerEntity && !entityLiving.world.isRemote) {
            PlayerEntity entityplayer = (PlayerEntity)entityLiving;
            entityplayer.addItemStackToInventory(crumbs);
        }
        return super.onItemUseFinish(stack, worldIn, entityLiving);
    }
}
