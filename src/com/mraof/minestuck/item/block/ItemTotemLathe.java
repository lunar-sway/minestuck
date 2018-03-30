package com.mraof.minestuck.item.block;

import com.mraof.minestuck.block.BlockTotemlathe;
import com.mraof.minestuck.block.BlockTotemlathe.EnumParts;
import com.mraof.minestuck.block.BlockTotemlathe2;
import com.mraof.minestuck.block.BlockTotemlathe3;
import com.mraof.minestuck.block.MinestuckBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
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
			
			int i = MathHelper.floor((double) (player.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;
			EnumFacing placedFacing = EnumFacing.getHorizontal(i).getOpposite();
			ItemStack itemstack = player.getHeldItem(hand);
			
			pos = pos.offset(placedFacing.rotateY());
			
			if(placedFacing.getFrontOffsetX() > 0 && hitZ >= 0.5F || placedFacing.getFrontOffsetX() < 0 && hitZ < 0.5F
					|| placedFacing.getFrontOffsetZ() > 0 && hitX < 0.5F || placedFacing.getFrontOffsetZ() < 0 && hitX >= 0.5F)
				pos = pos.offset(placedFacing.rotateY());
			
			if (!itemstack.isEmpty())
			{
				if(!canPlaceAt(itemstack, player, worldIn, pos, placedFacing))
					return EnumActionResult.FAIL;
				
				IBlockState state = this.block.getDefaultState().withProperty(BlockTotemlathe.DIRECTION, placedFacing).withProperty(BlockTotemlathe.PART, BlockTotemlathe.EnumParts.BOTTOM_LEFT);
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
				if (!world.mayPlace(MinestuckBlocks.totemlathe, pos.offset(facing.rotateYCCW(), x).up(y), false, EnumFacing.UP, null))
					return false;
			}
		}
		return true;
	}
	
	@Override
	public boolean placeBlockAt(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, IBlockState newState)
	{
		EnumFacing facing = EnumFacing.getHorizontal(MathHelper.floor((double)(player.rotationYaw * 4.0F / 360.0F) + 0.5D) + 2 & 3).getOpposite();
		newState = newState.withProperty(BlockTotemlathe.DIRECTION, facing);
		IBlockState state2=MinestuckBlocks.totemlathe2.getDefaultState().withProperty(BlockTotemlathe2.DIRECTION, facing);
		IBlockState state3=MinestuckBlocks.totemlathe3.getDefaultState().withProperty(BlockTotemlathe3.DIRECTION, facing);
		
		
		if(!(world.isRemote)){
			world.setBlockState(pos.offset(facing.rotateY(),3), newState.withProperty(BlockTotemlathe.PART, EnumParts.BOTTOM_RIGHT));
			world.setBlockState(pos.offset(facing.rotateY(),2), newState.withProperty(BlockTotemlathe.PART, EnumParts.BOTTOM_MIDRIGHT));
			world.setBlockState(pos.offset(facing.rotateY(),1), newState.withProperty(BlockTotemlathe.PART, EnumParts.BOTTOM_MIDLEFT));
			world.setBlockState(pos, newState.withProperty(BlockTotemlathe.PART, EnumParts.BOTTOM_LEFT));
			world.setBlockState(pos.offset(facing.rotateY(),3).up(1), state2.withProperty(BlockTotemlathe2.PART, BlockTotemlathe2.EnumParts.MID_RIGHT));
			world.setBlockState(pos.offset(facing.rotateY(),2).up(1), state2.withProperty(BlockTotemlathe2.PART, BlockTotemlathe2.EnumParts.MID_MIDRIGHT));
			world.setBlockState(pos.offset(facing.rotateY(),1).up(1), state2.withProperty(BlockTotemlathe2.PART, BlockTotemlathe2.EnumParts.MID_MIDLEFT));
			world.setBlockState(pos.up(1), state2.withProperty(BlockTotemlathe2.PART, BlockTotemlathe2.EnumParts.MID_LEFT));
			world.setBlockState(pos.offset(facing.rotateY(),2).up(2), state3.withProperty(BlockTotemlathe3.PART, BlockTotemlathe3.EnumParts.TOP_MIDRIGHT));
			world.setBlockState(pos.offset(facing.rotateY(),1).up(2), state3.withProperty(BlockTotemlathe3.PART, BlockTotemlathe3.EnumParts.TOP_MIDLEFT));
			world.setBlockState(pos.up(2), state3.withProperty(BlockTotemlathe3.PART, BlockTotemlathe3.EnumParts.TOP_LEFT));
		}
		return true;
	}
}
