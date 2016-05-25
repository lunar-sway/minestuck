package com.mraof.minestuck.item;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
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
	public ItemStack onItemUseFinish(ItemStack stack, World worldIn, EntityLivingBase entityLiving)
	{
		stack.stackSize--;
		
		if(entityLiving instanceof EntityPlayer)
		{
			EntityPlayer entityplayer = (EntityPlayer)entityLiving;
			worldIn.playSound(null, entityplayer.posX, entityplayer.posY, entityplayer.posZ, SoundEvents.entity_player_burp, SoundCategory.PLAYERS, 0.5F, worldIn.rand.nextFloat() * 0.1F + 0.9F);
			onArtifactActivated(worldIn, entityplayer);
		}
		return stack;
	}
	
	@Override
	public ActionResult<ItemStack> onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn, EnumHand hand)
	{
		playerIn.setActiveHand(hand);
		return new ActionResult(EnumActionResult.SUCCESS, itemStackIn);
	}
	
	/*@Override
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
	}*/
}