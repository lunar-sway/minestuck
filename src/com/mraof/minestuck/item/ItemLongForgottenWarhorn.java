package com.mraof.minestuck.item;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

import java.util.Random;

public class ItemLongForgottenWarhorn extends Item {
    public ItemLongForgottenWarhorn() {
        setMaxDamage(100);
        setCreativeTab(TabMinestuck.instance);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
        ItemStack item = playerIn.getHeldItem(handIn);
        Random rand = new Random();
        int duration = rand.nextInt(1199);
        int amplifier = rand.nextInt(7);
        int durability = rand.nextInt(99) + 10;
        int raneffect = rand.nextInt(11);
        switch(raneffect) {
            case 1:
                playerIn.addPotionEffect(new PotionEffect(MobEffects.BLINDNESS, duration, amplifier));
                break;
            case 2:
                playerIn.addPotionEffect(new PotionEffect(MobEffects.WITHER, duration, amplifier));
                break;
            case 3:
                playerIn.addPotionEffect(new PotionEffect(MobEffects.POISON, duration, amplifier));
                break;
            case 4:
                playerIn.addPotionEffect(new PotionEffect(MobEffects.HUNGER, duration, amplifier));
                break;
            case 5:
                playerIn.addPotionEffect(new PotionEffect(MobEffects.SLOWNESS, duration, amplifier));
                break;
            case 6:
                playerIn.addPotionEffect(new PotionEffect(MobEffects.UNLUCK, duration, amplifier));
                break;
            case 7:
                playerIn.addPotionEffect(new PotionEffect(MobEffects.LUCK, duration, amplifier));
                break;
            case 8:
                playerIn.addPotionEffect(new PotionEffect(MobEffects.NIGHT_VISION, duration, amplifier));
                break;
            case 9:
                playerIn.addPotionEffect(new PotionEffect(MobEffects.STRENGTH, duration, amplifier));
                break;
            case 10:
                playerIn.addPotionEffect(new PotionEffect(MobEffects.RESISTANCE, duration, amplifier));
                break;
            case 11:
                playerIn.addPotionEffect(new PotionEffect(MobEffects.REGENERATION, duration, amplifier));
                break;
            case 12:
                playerIn.addPotionEffect(new PotionEffect(MobEffects.SATURATION, duration, amplifier));
                break;
        }
        item.damageItem(durability, playerIn);
        return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, item);
    }
}
