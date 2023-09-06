package com.mraof.minestuck.item.block;

import com.mraof.minestuck.block.MSBlocks;
import com.mraof.minestuck.item.MSItems;
import com.mraof.minestuck.util.MSDamageSources;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

public class SendificatorBlockItem extends BlockItem
{
	public SendificatorBlockItem(Block blockIn, Properties builder)
	{
		super(blockIn, builder);
	}
	
	@Override
	public InteractionResultHolder<ItemStack> use(Level level, Player playerIn, InteractionHand handIn)
	{
		ItemStack itemStackIn = playerIn.getItemInHand(handIn);
		if(playerIn.isShiftKeyDown() && playerIn.getItemBySlot(EquipmentSlot.HEAD).isEmpty())
		{
			playerIn.setItemSlot(EquipmentSlot.HEAD, new ItemStack(MSBlocks.SENDIFICATOR.get()));
			
			return InteractionResultHolder.success(new ItemStack(Blocks.AIR));
		}
		return InteractionResultHolder.pass(itemStackIn);
	}
	
	@Override
	public void inventoryTick(ItemStack stack, Level level, Entity entityIn, int itemSlot, boolean isSelected)
	{
		if(entityIn instanceof Player playerIn)
		{
			ItemStack recoverItem = playerIn.getItemBySlot(EquipmentSlot.HEAD);
			if(recoverItem.is(MSItems.SENDIFICATOR.get()) && !playerIn.isCreative())
			{
				ItemStack headItem = new ItemStack(Items.PLAYER_HEAD, 1);
				NbtUtils.writeGameProfile(headItem.getOrCreateTagElement("SkullOwner"), playerIn.getGameProfile());
				ItemEntity headItemEntity = new ItemEntity(level, playerIn.getX(), playerIn.getY(), playerIn.getZ(), headItem);
				level.addFreshEntity(headItemEntity);
				
				ItemEntity recoverItemEntity = new ItemEntity(level, playerIn.getX(), playerIn.getY(), playerIn.getZ(), recoverItem);
				level.addFreshEntity(recoverItemEntity);
				playerIn.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Items.AIR));
				playerIn.hurt(MSDamageSources.decapitation(level.registryAccess()), Float.MAX_VALUE);
			}
		}
		super.inventoryTick(stack, level, entityIn, itemSlot, isSelected);
	}
}