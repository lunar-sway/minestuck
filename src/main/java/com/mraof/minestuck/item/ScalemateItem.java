package com.mraof.minestuck.item;

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
        Item newItem;
        ItemStack offhandItem = playerIn.getOffhandItem();
        ItemStack mainhandItemStack = playerIn.getItemInHand(handIn);
        
        if(offhandItem.is(Tags.Items.DYES_RED))
        {
            newItem = MSItems.SCALEMATE_APPLESCAB.get();
        } else if(offhandItem.is(Tags.Items.DYES_BLUE))
        {
            newItem = MSItems.SCALEMATE_BERRYBREATH.get();
        } else if(offhandItem.is(Tags.Items.DYES_BROWN))
        {
            newItem = MSItems.SCALEMATE_CINNAMONWHIFF.get();
        } else if(offhandItem.is(Tags.Items.DYES_YELLOW))
        {
            newItem = MSItems.SCALEMATE_HONEYTONGUE.get();
        } else if(offhandItem.is(Tags.Items.DYES_LIME))
        {
            newItem = MSItems.SCALEMATE_LEMONSNOUT.get();
        } else if(offhandItem.is(Tags.Items.DYES_LIGHT_BLUE))
        {
            newItem = MSItems.SCALEMATE_PINESNORT.get();
        } else if(offhandItem.is(Tags.Items.DYES_PINK))
        {
            newItem = MSItems.SCALEMATE_PUCEFOOT.get();
        } else if(offhandItem.is(Tags.Items.DYES_ORANGE))
        {
            newItem = MSItems.SCALEMATE_PUMPKINSNUFFLE.get();
        } else if(offhandItem.is(Tags.Items.DYES_WHITE))
        {
            newItem = MSItems.SCALEMATE_PYRALSPITE.get();
        } else if(offhandItem.is(Tags.Items.DYES_GREEN))
        {
            newItem = MSItems.SCALEMATE_WITNESS.get();
        } else
        {
            return InteractionResultHolder.pass(mainhandItemStack);
        }
        
        playerIn.getOffhandItem().shrink(1);
        ItemStack item = new ItemStack(newItem, mainhandItemStack.getCount());
        item.setTag(mainhandItemStack.getTag());
        
        playerIn.playSound(SoundEvents.AMBIENT_UNDERWATER_EXIT, 0.5F, 1.0F);
        return InteractionResultHolder.success(item);
    }
}
