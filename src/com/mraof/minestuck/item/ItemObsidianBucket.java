package com.mraof.minestuck.item;

import com.mraof.minestuck.Minestuck;

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

public class ItemObsidianBucket extends Item
{
	
	public ItemObsidianBucket()
	{
		setUnlocalizedName("bucketObsidian");
		setCreativeTab(Minestuck.tabMinestuck);
		setMaxStackSize(1);
	}
	
	@Override
	public ActionResult<ItemStack> onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn, EnumHand hand)
	{
		worldIn.playSound(null, playerIn.posX, playerIn.posY, playerIn.posZ, SoundEvents.entity_item_pickup, SoundCategory.PLAYERS, 0.2F, ((worldIn.rand.nextFloat() - worldIn.rand.nextFloat()) * 0.7F + 1.0F) * 2.0F);
		if(!playerIn.inventory.addItemStackToInventory(new ItemStack(Blocks.obsidian, 1)))
			if(!worldIn.isRemote)
				playerIn.dropItem(Item.getItemFromBlock(Blocks.obsidian), 1);
		
		return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, new ItemStack(Items.bucket, 1));
	}
	
}