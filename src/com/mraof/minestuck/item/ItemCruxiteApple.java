package com.mraof.minestuck.item;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemCruxiteApple extends ItemCruxiteArtifact
{
	
	@Override
	public int getMaxItemUseDuration(ItemStack stack)
	{
		return 32;
	}
	
	@Override
	public EnumAction getItemUseAction(ItemStack stack)
	{
		return EnumAction.EAT;
	}
	
	@Override
	public ItemStack onItemUseFinish(ItemStack stack, World worldIn, EntityPlayer playerIn)
	{
		stack.stackSize--;
		worldIn.playSoundAtEntity(playerIn, "random.burp", 0.5F, worldIn.rand.nextFloat() * 0.1F + 0.9F);
		onArtifactActivated(worldIn, playerIn);
		
		return stack;
	}
	
	@Override
	public ItemStack onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn)
	{
		playerIn.setItemInUse(itemStackIn, this.getMaxItemUseDuration(itemStackIn));
		return itemStackIn;
	}
	
	@Override
	public int getColorFromItemStack(ItemStack stack, int renderPass)
	{
		if(renderPass == 1)
		{
			int color = super.getColorFromItemStack(stack, renderPass);
			int i0 = ((color & 255) + 255)/2;
			int i1 = (((color >> 8) & 255) + 255)/2;
			int i2 = (((color >> 16) & 255) + 255)/2;
			color = i0 | (i1 << 8) | (i2 << 16);
			return color;
		}
		else return super.getColorFromItemStack(stack, renderPass);
	}
	
}