package com.mraof.minestuck.item;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;

public class ObsidianBucketItem extends Item
{
	public ObsidianBucketItem(Properties properties)
	{
		super(properties);
	}
	
	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn)
	{
		worldIn.playSound(null, playerIn.posX, playerIn.posY, playerIn.posZ, SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.PLAYERS, 0.2F, ((worldIn.rand.nextFloat() - worldIn.rand.nextFloat()) * 0.7F + 1.0F) * 2.0F);
		if(!playerIn.inventory.addItemStackToInventory(new ItemStack(Blocks.OBSIDIAN)))
			if(!worldIn.isRemote)
				playerIn.dropItem(new ItemStack(Blocks.OBSIDIAN), false);
		
		return new ActionResult<>(EnumActionResult.SUCCESS, new ItemStack(Items.BUCKET));
	}
	
}