package com.mraof.minestuck.item;

import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;

public class ItemRazorBlade extends Item {
	public ItemRazorBlade() {
		this.setCreativeTab(TabMinestuck.instance);
		this.setUnlocalizedName("razorBlade");
	}
	
	@Override
	public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
		if(entityIn instanceof EntityPlayer) {
			if(!((EntityPlayer) entityIn).capabilities.isCreativeMode) {
				EntityItem razor = new EntityItem(entityIn.world, entityIn.posX, entityIn.posY, entityIn.posZ, stack.copy());
				if(!worldIn.isRemote) {
					razor.getItem().setCount(1);
					razor.setPickupDelay(40);
            		entityIn.world.spawnEntity(razor);
            		stack.shrink(1);
            		ITextComponent message = new TextComponentTranslation("Ouch! You cut yourself on the razor blade. You don't know why you picked it up.");       
        			entityIn.sendMessage(message);
				}
				((EntityPlayer) entityIn).setHealth(((EntityPlayer) entityIn).getHealth() - 1);
			}
		}
		super.onUpdate(stack, worldIn, entityIn, itemSlot, isSelected);
	}
}
