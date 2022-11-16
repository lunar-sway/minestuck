package com.mraof.minestuck.item;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

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
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand handIn)
    {
        level.playSound(player, player.getX(), player.getY(), player.getZ(), sound.get(), SoundSource.PLAYERS, 1.0F, 1.0F);
    
        return InteractionResultHolder.success(player.getItemInHand(handIn));
    }
}
