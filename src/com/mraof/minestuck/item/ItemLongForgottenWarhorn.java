package com.mraof.minestuck.item;

import com.mraof.minestuck.util.MinestuckSoundHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import net.minecraft.util.SoundCategory;

import java.util.Random;

public class ItemLongForgottenWarhorn extends Item {
    public ItemLongForgottenWarhorn() {
        setMaxDamage(100);
        setMaxStackSize(1);
        setCreativeTab(TabMinestuck.instance);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
        ItemStack item = playerIn.getHeldItem(handIn);
        Random rand = new Random();
        int duration = rand.nextInt(1199) + 1;
        int amplifier = rand.nextInt(8);
        int durability = rand.nextInt(14) + 1;
        PotionEffect[] effect = new PotionEffect[] {new PotionEffect(MobEffects.BLINDNESS, duration, amplifier),
                new PotionEffect(MobEffects.WITHER, duration, amplifier),
                new PotionEffect(MobEffects.POISON, duration, amplifier),
                new PotionEffect(MobEffects.HUNGER, duration, amplifier),
                new PotionEffect(MobEffects.SLOWNESS, duration, amplifier),
                new PotionEffect(MobEffects.UNLUCK, duration, amplifier),
                new PotionEffect(MobEffects.LUCK, duration, amplifier),
                new PotionEffect(MobEffects.NIGHT_VISION, duration, amplifier),
                new PotionEffect(MobEffects.STRENGTH, duration, amplifier),
                new PotionEffect(MobEffects.RESISTANCE, duration, amplifier),
                new PotionEffect(MobEffects.REGENERATION, duration, amplifier),
                new PotionEffect(MobEffects.JUMP_BOOST, duration, amplifier),
                new PotionEffect(MobEffects.SPEED, duration, amplifier),
                new PotionEffect(MobEffects.HASTE, duration, amplifier),
                new PotionEffect(MobEffects.ABSORPTION, duration, amplifier),
                new PotionEffect(MobEffects.FIRE_RESISTANCE, duration, amplifier),
                new PotionEffect(MobEffects.GLOWING, duration, amplifier),
                new PotionEffect(MobEffects.INSTANT_HEALTH, duration, amplifier),
                new PotionEffect(MobEffects.INSTANT_DAMAGE, duration, amplifier),
                new PotionEffect(MobEffects.INVISIBILITY, duration, amplifier),
                new PotionEffect(MobEffects.WATER_BREATHING, duration, amplifier),
                new PotionEffect(MobEffects.NAUSEA, duration, amplifier),
                new PotionEffect(MobEffects.WEAKNESS, duration, amplifier),
                new PotionEffect(MobEffects.LEVITATION, duration, amplifier),
                new PotionEffect(MobEffects.MINING_FATIGUE, duration, amplifier),
                new PotionEffect(MobEffects.SATURATION, duration, amplifier)};
        int raneffect = rand.nextInt(effect.length);
        if(worldIn.isRemote != true) {
            playerIn.addPotionEffect(effect[raneffect]);
            if(raneffect != 0) {
                playerIn.addPotionEffect(new PotionEffect(MobEffects.BLINDNESS, 10, 1));
            }
            item.damageItem(durability, playerIn);
            playerIn.world.playSound((EntityPlayer)null, playerIn.posX, playerIn.posY, playerIn.posZ, MinestuckSoundHandler.soundWarhorn, SoundCategory.AMBIENT, 1.5F, 1.0F);
        }
        return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, item);
    }
}
