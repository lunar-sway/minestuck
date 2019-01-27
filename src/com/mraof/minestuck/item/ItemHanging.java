package com.mraof.minestuck.item;

import net.minecraft.entity.EntityHanging;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;


public abstract class ItemHanging extends Item
{
	
	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
	{
		ItemStack stack = player.getHeldItem(hand);
		BlockPos blockPos = pos.offset(facing);
		
		if (facing != EnumFacing.DOWN && facing != EnumFacing.UP && player.canPlayerEdit(blockPos, facing, stack))
		{
			EntityHanging entityhanging = this.createEntity(worldIn, blockPos, facing, stack, stack.getMetadata());
			
			if (entityhanging != null && entityhanging.onValidSurface())
			{
				if (!worldIn.isRemote)
				{
					entityhanging.playPlaceSound();
					worldIn.spawnEntity(entityhanging);
				}
				
				stack.shrink(1);
			}
			
			return EnumActionResult.SUCCESS;
		}
		else
		{
			return EnumActionResult.FAIL;
		}
	}
	
	public abstract EntityHanging createEntity(World worldIn, BlockPos pos, EnumFacing facing, ItemStack stack, int meta);
}
