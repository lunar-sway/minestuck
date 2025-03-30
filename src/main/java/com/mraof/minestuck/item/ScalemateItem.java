package com.mraof.minestuck.item;

import net.minecraft.core.Holder;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.common.Tags;

public class ScalemateItem extends Item
{
    public ScalemateItem(Properties properties)
    {
        super(properties);
    }
    
    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player playerIn, InteractionHand handIn)
    {
        Holder<Item> newItem;
        ItemStack offhandItem = playerIn.getOffhandItem();
        ItemStack mainhandItemStack = playerIn.getItemInHand(handIn);
        
        if(offhandItem.is(Tags.Items.DYES_RED))
        {
            newItem = MSItems.SCALEMATE_APPLESCAB;
        } else if(offhandItem.is(Tags.Items.DYES_BLUE))
        {
            newItem = MSItems.SCALEMATE_BERRYBREATH;
        } else if(offhandItem.is(Tags.Items.DYES_BROWN))
        {
            newItem = MSItems.SCALEMATE_CINNAMONWHIFF;
        } else if(offhandItem.is(Tags.Items.DYES_YELLOW))
        {
            newItem = MSItems.SCALEMATE_HONEYTONGUE;
        } else if(offhandItem.is(Tags.Items.DYES_LIME))
        {
            newItem = MSItems.SCALEMATE_LEMONSNOUT;
        } else if(offhandItem.is(Tags.Items.DYES_LIGHT_BLUE))
        {
            newItem = MSItems.SCALEMATE_PINESNORT;
        } else if(offhandItem.is(Tags.Items.DYES_PINK))
        {
            newItem = MSItems.SCALEMATE_PUCEFOOT;
        } else if(offhandItem.is(Tags.Items.DYES_ORANGE))
        {
            newItem = MSItems.SCALEMATE_PUMPKINSNUFFLE;
        } else if(offhandItem.is(Tags.Items.DYES_WHITE))
        {
            newItem = MSItems.SCALEMATE_PYRALSPITE;
        } else if(offhandItem.is(Tags.Items.DYES_GREEN))
        {
            newItem = MSItems.SCALEMATE_WITNESS;
        } else
        {
            return InteractionResultHolder.pass(mainhandItemStack);
        }
        
        playerIn.getOffhandItem().shrink(1);
        ItemStack item = new ItemStack(newItem, mainhandItemStack.getCount(), mainhandItemStack.getComponentsPatch());
        
        playerIn.playSound(SoundEvents.AMBIENT_UNDERWATER_EXIT, 0.5F, 1.0F);
        return InteractionResultHolder.success(item);
    }
}
