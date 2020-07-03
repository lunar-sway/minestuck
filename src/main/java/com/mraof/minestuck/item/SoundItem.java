package com.mraof.minestuck.item;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.world.World;

import java.util.function.Supplier;

public class SoundItem extends Item
{
    private final Supplier<SoundEvent> sound;
    
    public SoundItem(Supplier<SoundEvent> sound, Properties properties)
    {
        super(properties);
        this.sound = sound;
    }
    
    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity player, Hand handIn)
    {
        worldIn.playSound(player, player.posX, player.posY, player.posZ, sound.get(), SoundCategory.PLAYERS, 1.0F, 1.0F);
    
        return new ActionResult<>(ActionResultType.SUCCESS, player.getHeldItem(handIn));
    }
}
