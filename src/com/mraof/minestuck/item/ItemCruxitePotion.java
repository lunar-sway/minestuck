package com.mraof.minestuck.item;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

public class ItemCruxitePotion extends ItemCruxiteArtifact
{
	
	@Override
	public int getMaxItemUseDuration(ItemStack stack)
	{
		return 32;
	}
	
	@Override
	public EnumAction getItemUseAction(ItemStack stack)
	{
		return EnumAction.DRINK;
	}
	
	@Override
	public ItemStack onItemUseFinish(ItemStack stack, World worldIn, EntityLivingBase entityLiving)
	{
		stack.stackSize--;
		if(entityLiving instanceof EntityPlayer)
			onArtifactActivated(worldIn, (EntityPlayer) entityLiving);
		
		return stack;
	}
	
	@Override
	public ActionResult<ItemStack> onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn, EnumHand hand)
	{
		playerIn.setActiveHand(hand);
		return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, itemStackIn);
	}
	
	@Override
	public int getColorFromItemStack(ItemStack stack, int renderPass)
	{
		if(renderPass == 0)
		{
			int color = super.getColorFromItemStack(stack, renderPass);
			int i0 = ((color & 255) + 255)/2;
			int i1 = (((color >> 8) & 255) + 255)/2;
			int i2 = (((color >> 16) & 255) + 255)/2;
			color = i0 | (i1 << 8) | (i2 << 16);
			return color;
		}
		else if(renderPass == 2)
			return 0xFFFFFF;
		else return super.getColorFromItemStack(stack, renderPass);
	}
}