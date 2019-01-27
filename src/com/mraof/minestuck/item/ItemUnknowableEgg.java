package com.mraof.minestuck.item;

import com.mraof.minestuck.util.MinestuckSoundHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;

public class ItemUnknowableEgg extends ItemFood {
    public ItemUnknowableEgg(int amount, float saturation, boolean isWolfFood) {
        super(amount, saturation, isWolfFood);
        setCreativeTab(TabMinestuck.instance);
        setMaxStackSize(16);
    }

    @Override
    protected void onFoodEaten(ItemStack stack, World worldIn, EntityPlayer player) {
        if(worldIn.isRemote != true) {
            player.addPotionEffect(new PotionEffect(MobEffects.NAUSEA, 160, 2));
            player.addPotionEffect(new PotionEffect(MobEffects.POISON, 160, 2));
            player.world.playSound((EntityPlayer)null, player.posX, player.posY, player.posZ, MinestuckSoundHandler.soundWhispers, SoundCategory.AMBIENT, 1.0F, 0.8F);
        }
        super.onFoodEaten(stack, worldIn, player);
    }
}
