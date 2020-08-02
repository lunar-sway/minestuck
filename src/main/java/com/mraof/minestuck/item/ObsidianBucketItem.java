package com.mraof.minestuck.item;

import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.*;
import net.minecraft.world.World;

public class ObsidianBucketItem extends Item
{
	public ObsidianBucketItem(Properties properties)
	{
		super(properties);
	}
	
	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn)
	{
		worldIn.playSound(null, playerIn.getPosX(), playerIn.getPosY(), playerIn.getPosZ(), SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.PLAYERS, 0.2F, ((worldIn.rand.nextFloat() - worldIn.rand.nextFloat()) * 0.7F + 1.0F) * 2.0F);
		if(!playerIn.inventory.addItemStackToInventory(new ItemStack(Blocks.OBSIDIAN)))
			if(!worldIn.isRemote)
				playerIn.dropItem(new ItemStack(Blocks.OBSIDIAN), false);
		
		return new ActionResult<>(ActionResultType.SUCCESS, new ItemStack(Items.BUCKET));
	}
	
}