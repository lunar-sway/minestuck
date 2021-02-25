package com.mraof.minestuck.item.block;

import com.mraof.minestuck.block.MSBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

public class SendificatorBlockItem extends BlockItem
{
	public SendificatorBlockItem(Block blockIn, Properties builder)
	{
		super(blockIn, builder);
	}
	
	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn)
	{
		ItemStack itemStackIn = playerIn.getHeldItem(handIn);
		if(playerIn.isSneaking() && playerIn.getItemStackFromSlot(EquipmentSlotType.HEAD).isEmpty())
		{
			playerIn.replaceItemInInventory(103, new ItemStack(MSBlocks.SENDIFICATOR));
			
			return ActionResult.resultSuccess(new ItemStack(Blocks.AIR));
		}
		return ActionResult.resultPass(itemStackIn);
	}
	
	@Override
	public void inventoryTick(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected)
	{
		if(entityIn instanceof PlayerEntity)
		{
			PlayerEntity playerIn = (PlayerEntity) entityIn;
			ItemStack recoverItem = playerIn.getItemStackFromSlot(EquipmentSlotType.HEAD);
			if(recoverItem.isItemEqual(new ItemStack(MSBlocks.SENDIFICATOR)) && !playerIn.isCreative())
			{
				ItemStack headItem = new ItemStack(Items.PLAYER_HEAD, 1);
				NBTUtil.writeGameProfile(headItem.getOrCreateChildTag("SkullOwner"), playerIn.getGameProfile());
				ItemEntity headItemEntity = new ItemEntity(playerIn.world, playerIn.getPosX(), playerIn.getPosY(), playerIn.getPosZ(), headItem);
				playerIn.world.addEntity(headItemEntity);
				
				ItemEntity recoverItemEntity = new ItemEntity(playerIn.world, playerIn.getPosX(), playerIn.getPosY(), playerIn.getPosZ(), recoverItem);
				playerIn.world.addEntity(recoverItemEntity);
				playerIn.replaceItemInInventory(103, new ItemStack(Items.AIR));
				//playerIn.setHealth(0);
				playerIn.onKillCommand();
			}
		}
		super.inventoryTick(stack, worldIn, entityIn, itemSlot, isSelected);
	}
	
}