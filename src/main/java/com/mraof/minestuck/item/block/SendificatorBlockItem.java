package com.mraof.minestuck.item.block;

import com.mraof.minestuck.block.MSBlocks;
import com.mraof.minestuck.util.MSDamageSources;
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
	public ActionResult<ItemStack> use(World worldIn, PlayerEntity playerIn, Hand handIn)
	{
		ItemStack itemStackIn = playerIn.getItemInHand(handIn);
		if(playerIn.isShiftKeyDown() && playerIn.getItemBySlot(EquipmentSlotType.HEAD).isEmpty())
		{
			playerIn.setSlot(103, new ItemStack(MSBlocks.SENDIFICATOR));
			
			return ActionResult.success(new ItemStack(Blocks.AIR));
		}
		return ActionResult.pass(itemStackIn);
	}
	
	@Override
	public void inventoryTick(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected)
	{
		if(entityIn instanceof PlayerEntity)
		{
			PlayerEntity playerIn = (PlayerEntity) entityIn;
			ItemStack recoverItem = playerIn.getItemBySlot(EquipmentSlotType.HEAD);
			if(recoverItem.sameItem(new ItemStack(MSBlocks.SENDIFICATOR)) && !playerIn.isCreative())
			{
				ItemStack headItem = new ItemStack(Items.PLAYER_HEAD, 1);
				NBTUtil.writeGameProfile(headItem.getOrCreateTagElement("SkullOwner"), playerIn.getGameProfile());
				ItemEntity headItemEntity = new ItemEntity(playerIn.level, playerIn.getX(), playerIn.getY(), playerIn.getZ(), headItem);
				playerIn.level.addFreshEntity(headItemEntity);
				
				ItemEntity recoverItemEntity = new ItemEntity(playerIn.level, playerIn.getX(), playerIn.getY(), playerIn.getZ(), recoverItem);
				playerIn.level.addFreshEntity(recoverItemEntity);
				playerIn.setSlot(103, new ItemStack(Items.AIR));
				playerIn.hurt(MSDamageSources.DECAPITATION, Float.MAX_VALUE);
			}
		}
		super.inventoryTick(stack, worldIn, entityIn, itemSlot, isSelected);
	}
}