package com.mraof.minestuck.item;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
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
    public ActionResult<ItemStack> use(World worldIn, PlayerEntity player, Hand handIn)
    {
        worldIn.playSound(player, player.getX(), player.getY(), player.getZ(), sound.get(), SoundCategory.PLAYERS, 1.0F, 1.0F);
    
        return ActionResult.success(player.getItemInHand(handIn));
    }
}
