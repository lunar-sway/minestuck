package com.mraof.minestuck.item;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundEvents;
import net.minecraft.world.World;
import net.minecraftforge.common.Tags;

public class ScalemateItem extends Item
{
    public ScalemateItem(Properties properties)
    {
        super(properties);
    }
    
    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn)
    {
        Item newItem;
        Item offhandItem = playerIn.getHeldItemOffhand().getItem();
        ItemStack mainhandItemStack = playerIn.getHeldItem(handIn);
        
        if(offhandItem.isIn(Tags.Items.DYES_RED))
        {
            newItem = MSItems.SCALEMATE_APPLESCAB;
        } else if(offhandItem.isIn(Tags.Items.DYES_BLUE))
        {
            newItem = MSItems.SCALEMATE_BERRYBREATH;
        } else if(offhandItem.isIn(Tags.Items.DYES_BROWN))
        {
            newItem = MSItems.SCALEMATE_CINNAMONWHIFF;
        } else if(offhandItem.isIn(Tags.Items.DYES_ORANGE))
        {
            newItem = MSItems.SCALEMATE_HONEYTONGUE;
        } else if(offhandItem.isIn(Tags.Items.DYES_YELLOW))
        {
            newItem = MSItems.SCALEMATE_LEMONSNOUT;
        } else if(offhandItem.isIn(Tags.Items.DYES_LIGHT_BLUE))
        {
            newItem = MSItems.SCALEMATE_PINESNOUT;
        } else if(offhandItem.isIn(Tags.Items.DYES_PINK))
        {
            newItem = MSItems.SCALEMATE_PUCEFOOT;
        } else if(offhandItem == Items.PUMPKIN)
        {
            newItem = MSItems.SCALEMATE_PUMPKINSNUFFLE;
        } else if(offhandItem.isIn(Tags.Items.DYES_WHITE))
        {
            newItem = MSItems.SCALEMATE_PYRALSPITE;
        } else if(offhandItem.isIn(Tags.Items.DYES_GREEN))
        {
            newItem = MSItems.SCALEMATE_WITNESS;
        } else
        {
            return ActionResult.resultPass(mainhandItemStack);
        }
        
        playerIn.getHeldItemOffhand().shrink(1);
        ItemStack item = new ItemStack(newItem, mainhandItemStack.getCount());
        item.setTag(mainhandItemStack.getTag());
        
        playerIn.playSound(SoundEvents.AMBIENT_UNDERWATER_EXIT, 0.5F, 1.0F);
        return ActionResult.resultSuccess(item);
    }
}
