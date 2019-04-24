package com.mraof.minestuck.item.weapon;

import com.mraof.minestuck.item.TabMinestuck;
import com.mraof.minestuck.util.MinestuckSoundHandler;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundCategory;

public class ItemSbahjEEEE extends ItemPogoWeapon {
    public ItemSbahjEEEE(int durability, double damage, double speed, int enchantability, String name, double pogoness)
    {
        super(durability, damage, speed, enchantability, name, pogoness);
        this.setCreativeTab(TabMinestuck.instance);
    }
    
    @Override
    public boolean hitEntity(ItemStack stack, EntityLivingBase target, EntityLivingBase player)
    {
        player.world.playSound(null, player.posX, player.posY, player.posZ, MinestuckSoundHandler.soundScreech, SoundCategory.AMBIENT, 1.5F, 1.0F);
        return super.hitEntity(stack, target, player);
    }
}
