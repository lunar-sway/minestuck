package com.mraof.minestuck.item;

import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.world.World;

public class ObsidianBucketItem extends Item
{
	public ObsidianBucketItem(Properties properties)
	{
		super(properties);
	}
	
	@Override
	public ActionResult<ItemStack> use(World worldIn, PlayerEntity playerIn, Hand handIn)
	{
		worldIn.playSound(null, playerIn.getX(), playerIn.getY(), playerIn.getZ(), SoundEvents.ITEM_PICKUP, SoundCategory.PLAYERS, 0.2F, ((worldIn.random.nextFloat() - worldIn.random.nextFloat()) * 0.7F + 1.0F) * 2.0F);
		if(!playerIn.inventory.add(new ItemStack(Blocks.OBSIDIAN)))
			if(!worldIn.isClientSide)
				playerIn.drop(new ItemStack(Blocks.OBSIDIAN), false);
		
		return ActionResult.success(new ItemStack(Items.BUCKET));
	}
	
}