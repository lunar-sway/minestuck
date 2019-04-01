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
    static PotionEffect[] effect = new PotionEffect[] {new PotionEffect(MobEffects.BLINDNESS, 400, 0),
            new PotionEffect(MobEffects.WITHER, 300, 1),
            new PotionEffect(MobEffects.POISON, 300, 2),
            new PotionEffect(MobEffects.HUNGER, 400, 1),
            new PotionEffect(MobEffects.SLOWNESS, 400, 2),
            new PotionEffect(MobEffects.UNLUCK, 600, 3),
            new PotionEffect(MobEffects.LUCK, 600, 3),
            new PotionEffect(MobEffects.NIGHT_VISION, 400, 0),
            new PotionEffect(MobEffects.STRENGTH, 200, 1),
            new PotionEffect(MobEffects.RESISTANCE, 300, 1),
            new PotionEffect(MobEffects.REGENERATION, 300, 2),
            new PotionEffect(MobEffects.JUMP_BOOST, 400, 2),
            new PotionEffect(MobEffects.SPEED, 400, 2),
            new PotionEffect(MobEffects.HASTE, 400, 2),
            new PotionEffect(MobEffects.ABSORPTION, 500, 1),
            new PotionEffect(MobEffects.FIRE_RESISTANCE, 600, 0),
            new PotionEffect(MobEffects.GLOWING, 500, 0),
            new PotionEffect(MobEffects.INSTANT_HEALTH, 20, 0),
            new PotionEffect(MobEffects.INSTANT_DAMAGE, 20, 0),
            new PotionEffect(MobEffects.INVISIBILITY, 500, 3),
            new PotionEffect(MobEffects.WATER_BREATHING, 400, 0),
            new PotionEffect(MobEffects.NAUSEA, 300, 0),
            new PotionEffect(MobEffects.WEAKNESS, 200, 1),
            new PotionEffect(MobEffects.LEVITATION, 200, 2),
            new PotionEffect(MobEffects.MINING_FATIGUE, 300, 2),
            new PotionEffect(MobEffects.SATURATION, 400, 1)};
    public ItemLongForgottenWarhorn() {
        setMaxDamage(100);
        setMaxStackSize(1);
        setCreativeTab(TabMinestuck.instance);
    }
    
    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
        ItemStack item = playerIn.getHeldItem(handIn);
        if(worldIn.isRemote != true) {
            Random rand = new Random();
            int durability = rand.nextInt(14) + 1;
            int raneffect = rand.nextInt(effect.length);
            playerIn.addPotionEffect(new PotionEffect(effect[raneffect]));
            if(raneffect != 0) {
                playerIn.addPotionEffect(new PotionEffect(MobEffects.BLINDNESS, 15, 1));
            }
            item.damageItem(durability, playerIn);
            playerIn.world.playSound((EntityPlayer)null, playerIn.posX, playerIn.posY, playerIn.posZ, MinestuckSoundHandler.soundWarhorn, SoundCategory.AMBIENT, 1.5F, 1.0F);
        }
        if(worldIn.isRemote) {
            return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, item);
        }
        else {
            return new ActionResult<ItemStack>(EnumActionResult.PASS, item);
        }
    }
}