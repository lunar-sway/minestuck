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
	public EnumActionResult onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
	{
		BlockPos blockpos = pos.offset(facing);
		
		if (facing != EnumFacing.DOWN && facing != EnumFacing.UP && playerIn.canPlayerEdit(blockpos, facing, stack))
		{
			EntityHanging entityhanging = this.createEntity(worldIn, blockpos, facing, stack);
			
			if (entityhanging != null && entityhanging.onValidSurface())
			{
				if (!worldIn.isRemote)
				{
					entityhanging.playPlaceSound();
					worldIn.spawnEntity(entityhanging);
				}
				
				stack.stackSize--;
			}
			
			return EnumActionResult.SUCCESS;
		}
		else
		{
			return EnumActionResult.FAIL;
		}
	}
	
	public abstract EntityHanging createEntity(World worldIn, BlockPos pos, EnumFacing facing, ItemStack stack);
}
