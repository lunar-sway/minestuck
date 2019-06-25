package com.mraof.minestuck.item.block;

import com.mraof.minestuck.block.BlockTotemLathe;
import com.mraof.minestuck.block.MinestuckBlocks;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemTotemLathe extends ItemBlock
{
	
	public ItemTotemLathe(Block blockIn, Properties builder)
	{
		super(blockIn, builder);
	}
	
	@Override
	public EnumActionResult tryPlace(BlockItemUseContext context)
	{
		World world = context.getWorld();
		EnumFacing facing = context.getFace();
		BlockPos pos = context.getPos();
		EntityPlayer player = context.getPlayer();
		if (world.isRemote)
		{
			return EnumActionResult.SUCCESS;
		} else if (facing != EnumFacing.UP)
		{
			return EnumActionResult.FAIL;
		} else
		{
			IBlockState block = world.getBlockState(pos);
			boolean flag = block.isReplaceable(context);
			
			if (!flag)
			{
				pos = pos.up();
			}
			
			EnumFacing placedFacing = context.getPlacementHorizontalFacing().getOpposite();
			ItemStack itemstack = context.getItem();
			
			pos = pos.offset(placedFacing.rotateY());
			
			if(placedFacing == EnumFacing.EAST && context.getHitZ() >= 0.5F || placedFacing == EnumFacing.WEST && context.getHitZ() < 0.5F
					|| placedFacing == EnumFacing.SOUTH && context.getHitX() < 0.5F || placedFacing == EnumFacing.NORTH && context.getHitX() >= 0.5F)
				pos = pos.offset(placedFacing.rotateY());
			
			if (!itemstack.isEmpty())
			{
				if(!canPlaceAt(context, pos, placedFacing))
					return EnumActionResult.FAIL;
				
				IBlockState state = getBlock().getDefaultState().with(BlockTotemLathe.FACING, placedFacing);
				this.placeBlock(context, state);
				return EnumActionResult.SUCCESS;
			}
			return EnumActionResult.FAIL;
		}
	}
	
	public static boolean canPlaceAt(BlockItemUseContext context, BlockPos pos, EnumFacing facing)
	{
		for(int x = 0; x < 4; x++)
		{
			if(!context.getPlayer().canPlayerEdit(pos.offset(facing.rotateYCCW(), x), EnumFacing.UP, context.getItem()))
				return false;
			for(int y = 0; y < 3; y++)
			{
				if(!context.getWorld().getBlockState(pos.offset(facing.rotateYCCW(), x).up(y)).isReplaceable(context))
					return false;
			}
		}
		return true;
	}
	
	@Override
	protected boolean placeBlock(BlockItemUseContext context, IBlockState newState)
	{
		BlockPos pos = context.getPos();
		World world = context.getWorld();
		EntityPlayer player = context.getPlayer();
		if(!world.isRemote)
		{
			EnumFacing facing = context.getPlacementHorizontalFacing().getOpposite();
			
			pos = pos.offset(facing.rotateY());
			
			if(facing == EnumFacing.EAST && context.getHitZ() >= 0.5F || facing == EnumFacing.WEST && context.getHitZ() < 0.5F
					|| facing == EnumFacing.SOUTH && context.getHitX() < 0.5F || facing == EnumFacing.NORTH && context.getHitX() >= 0.5F)
				pos = pos.offset(facing.rotateY());
			
			world.setBlockState(pos, MinestuckBlocks.TOTEM_LATHE.CARD_SLOT.getDefaultState().with(BlockTotemLathe.FACING, facing));
			world.setBlockState(pos.offset(facing.rotateYCCW(),1), MinestuckBlocks.TOTEM_LATHE.BOTTOM_LEFT.getDefaultState().with(BlockTotemLathe.FACING, facing));
			world.setBlockState(pos.offset(facing.rotateYCCW(),2), MinestuckBlocks.TOTEM_LATHE.BOTTOM_RIGHT.getDefaultState().with(BlockTotemLathe.FACING, facing));
			world.setBlockState(pos.offset(facing.rotateYCCW(),3), MinestuckBlocks.TOTEM_LATHE.BOTTOM_CORNER.getDefaultState().with(BlockTotemLathe.FACING, facing));
			world.setBlockState(pos.up(1), MinestuckBlocks.TOTEM_LATHE.MIDDLE.getDefaultState().with(BlockTotemLathe.FACING, facing));
			world.setBlockState(pos.offset(facing.rotateYCCW(),1).up(1), MinestuckBlocks.TOTEM_LATHE.ROD.getDefaultState().with(BlockTotemLathe.FACING, facing));
			world.setBlockState(pos.offset(facing.rotateYCCW(),3).up(1), MinestuckBlocks.TOTEM_LATHE.WHEEL.getDefaultState().with(BlockTotemLathe.FACING, facing));
			world.setBlockState(pos.up(2), MinestuckBlocks.TOTEM_LATHE.TOP_CORNER.getDefaultState().with(BlockTotemLathe.FACING, facing));
			world.setBlockState(pos.offset(facing.rotateYCCW(),1).up(2), MinestuckBlocks.TOTEM_LATHE.TOP.getDefaultState().with(BlockTotemLathe.FACING, facing));
			world.setBlockState(pos.offset(facing.rotateYCCW(),2).up(2), MinestuckBlocks.TOTEM_LATHE.CARVER.getDefaultState().with(BlockTotemLathe.FACING, facing));
			
			if(player instanceof EntityPlayerMP)
				CriteriaTriggers.PLACED_BLOCK.trigger((EntityPlayerMP) player, pos, context.getItem());
		}
		return true;
	}
}
