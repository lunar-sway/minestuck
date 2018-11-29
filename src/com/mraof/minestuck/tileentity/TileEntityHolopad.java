package com.mraof.minestuck.tileentity;

import com.mraof.minestuck.item.MinestuckItems;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class TileEntityHolopad extends TileEntity
{

	protected ItemStack card = ItemStack.EMPTY;
	
	public boolean onRightClick(EntityPlayer playerIn, World worldIn) 
	{
		if (playerIn.isSneaking())
		{
			return false;
		}
		
		ItemStack card = this.getCard();
		
		System.out.println(card);
		System.out.println(!card.isEmpty());
		
		if (!card.isEmpty())
		{    //Remove card from holopad
			if (playerIn.getHeldItemMainhand().isEmpty())
				playerIn.setHeldItem(EnumHand.MAIN_HAND, card);
			else if (!playerIn.inventory.addItemStackToInventory(card))
				dropItem(false, worldIn, pos, card);
			else playerIn.inventoryContainer.detectAndSendChanges();
			
			this.setCard(ItemStack.EMPTY);
			
			return true;
		}
		 else
			{
				ItemStack heldStack = playerIn.getHeldItemMainhand();
				if (!heldStack.isEmpty() && heldStack.getItem() == MinestuckItems.captchaCard)
					return this.setCard(heldStack.splitStack(1));
			}
		
		return false;
	}
	
	public void dropItem(boolean inBlock, World worldIn, BlockPos pos, ItemStack item)
	{
		BlockPos dropPos;
		if(inBlock)
			dropPos = pos;
		else if(!worldIn.getBlockState(pos).isBlockNormalCube())
			dropPos = pos;
		else if(!worldIn.getBlockState(pos.up()).isBlockNormalCube())
			dropPos = pos.up();
		else dropPos = pos;
		
		InventoryHelper.spawnItemStack(worldIn, dropPos.getX(), dropPos.getY(), dropPos.getZ(), item);
		
	}
	
	public boolean hasCard()
	{
		return !this.getCard().isEmpty();
	}
	
	public boolean setCard(ItemStack item)
	{
		if(item.getItem() != MinestuckItems.captchaCard) return false;
		
		this.card = item;
		return true;
	}
	
	public ItemStack getCard()
	{
		return this.card;
	}
}
