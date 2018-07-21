package com.mraof.minestuck.item.block;

import com.mraof.minestuck.block.BlockTotemLathe;
import com.mraof.minestuck.block.BlockTotemLathe.EnumParts;
import com.mraof.minestuck.block.MinestuckBlocks;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemTotemLathe extends ItemBlock
{
	public ItemTotemLathe(Block block)
	{
		super(block);
	}
	
	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
	{
		
		if (worldIn.isRemote)
		{
			return EnumActionResult.SUCCESS;
		} else if (facing != EnumFacing.UP)
		{
			return EnumActionResult.FAIL;
		} else
		{
			Block block = worldIn.getBlockState(pos).getBlock();
			boolean flag = block.isReplaceable(worldIn, pos);
			
			if (!flag)
			{
				pos = pos.up();
			}
			
			EnumFacing placedFacing = player.getHorizontalFacing().getOpposite();
			ItemStack itemstack = player.getHeldItem(hand);
			
			pos = pos.offset(placedFacing.rotateY());
			
			if(placedFacing.getFrontOffsetX() > 0 && hitZ >= 0.5F || placedFacing.getFrontOffsetX() < 0 && hitZ < 0.5F
					|| placedFacing.getFrontOffsetZ() > 0 && hitX < 0.5F || placedFacing.getFrontOffsetZ() < 0 && hitX >= 0.5F)
				pos = pos.offset(placedFacing.rotateY());
			
			if (!itemstack.isEmpty())
			{
				if(!canPlaceAt(itemstack, player, worldIn, pos, placedFacing))
					return EnumActionResult.FAIL;
				
				IBlockState state = this.block.getDefaultState().withProperty(BlockTotemLathe.DIRECTION, placedFacing).withProperty(BlockTotemLathe.PART1, BlockTotemLathe.EnumParts.BOTTOM_LEFT);
				this.placeBlockAt(itemstack, player, worldIn, pos, facing, hitX, hitY, hitZ, state);
				return EnumActionResult.SUCCESS;
			}
			return EnumActionResult.FAIL;
		}
	}
	
	public static boolean canPlaceAt(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumFacing facing)
	{
		for (int x = 0; x < 4; x++)
		{
			if (!player.canPlayerEdit(pos.offset(facing.rotateYCCW(), x), EnumFacing.UP, stack))
				return false;
			for (int y = 0; y < 3; y++)
			{
				if (!world.mayPlace(MinestuckBlocks.totemlathe[0], pos.offset(facing.rotateYCCW(), x).up(y), false, EnumFacing.UP, null))
					return false;
			}
		}
		return true;
	}
	
	@Override
	public boolean placeBlockAt(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, IBlockState newState)
	{
		EnumFacing facing = player.getHorizontalFacing().getOpposite();
		
		if(!(world.isRemote))
		{
			world.setBlockState(pos, BlockTotemLathe.getState(EnumParts.BOTTOM_LEFT, facing));
			world.setBlockState(pos.offset(facing.rotateYCCW(),1), BlockTotemLathe.getState(EnumParts.BOTTOM_MIDLEFT, facing));
			world.setBlockState(pos.offset(facing.rotateYCCW(),2), BlockTotemLathe.getState(EnumParts.BOTTOM_MIDRIGHT, facing));
			world.setBlockState(pos.offset(facing.rotateYCCW(),3), BlockTotemLathe.getState(EnumParts.BOTTOM_RIGHT, facing));
			world.setBlockState(pos.up(1), BlockTotemLathe.getState(EnumParts.MID_LEFT, facing));
			world.setBlockState(pos.offset(facing.rotateYCCW(),1).up(1), BlockTotemLathe.getState(EnumParts.ROD_LEFT, facing));
			world.setBlockState(pos.offset(facing.rotateYCCW(),3).up(1), BlockTotemLathe.getState(EnumParts.MID_RIGHT, facing));
			world.setBlockState(pos.up(2), BlockTotemLathe.getState(EnumParts.TOP_LEFT, facing));
			world.setBlockState(pos.offset(facing.rotateYCCW(),1).up(2), BlockTotemLathe.getState(EnumParts.TOP_MIDLEFT, facing));
			world.setBlockState(pos.offset(facing.rotateYCCW(),2).up(2), BlockTotemLathe.getState(EnumParts.TOP_MIDRIGHT, facing));
			
			if(player instanceof EntityPlayerMP)
				CriteriaTriggers.PLACED_BLOCK.trigger((EntityPlayerMP) player, pos, stack);
		}
		return true;
	}
}
