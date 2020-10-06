package com.mraof.minestuck.item.weapon;

import com.mraof.minestuck.item.MSItems;
import net.minecraft.block.Blocks;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.item.IItemTier;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Util;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;

import javax.annotation.Nullable;
import java.util.Random;

public class KundlerSurpriseWeaponItem extends WeaponItem
{
    public KundlerSurpriseWeaponItem(IItemTier tier, int attackDamageIn, float attackSpeedIn, float efficiency, @Nullable MSToolType toolType, Properties builder)
    {
        super(tier, attackDamageIn, attackSpeedIn, efficiency, toolType, builder);
    }
    
    @Override
    public boolean hitEntity(ItemStack stack, LivingEntity target, LivingEntity attacker)
    {
        if(target.getHealth() <= 0 && !attacker.world.isRemote && attacker.getRNG().nextFloat() <= 0.20)
        {
            Random ran = new Random();
            ItemStack[] items = new ItemStack[]{new ItemStack(Items.MELON_SLICE), new ItemStack(Items.STICK), new ItemStack(Items.EGG),
                    new ItemStack(Blocks.DIRT), new ItemStack(Blocks.PUMPKIN), new ItemStack(Blocks.COBBLESTONE), new ItemStack(Items.REDSTONE),
                    new ItemStack(MSItems.SURPRISE_EMBRYO), new ItemStack(MSItems.GAMEGRL_MAGAZINE), new ItemStack(MSItems.GAMEBRO_MAGAZINE),
                    new ItemStack(Blocks.DEAD_HORN_CORAL)};
            int num = ran.nextInt(items.length);
            ItemEntity item = new ItemEntity(target.world, target.getPosX(), target.getPosY(), target.getPosZ(), items[num].copy());
            target.world.addEntity(item);
    
            IFormattableTextComponent message = new TranslationTextComponent(getTranslationKey() + ".message", items[num].getDisplayName());
            message.mergeStyle(TextFormatting.GOLD);
            attacker.sendMessage(message, Util.DUMMY_UUID);
        }
        return super.hitEntity(stack, target, attacker);
    }
}
