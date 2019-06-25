package com.mraof.minestuck.item.block;

import com.mraof.minestuck.block.BlockPunchDesignix;
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

public class ItemPunchDesignix extends ItemBlock
{
	public ItemPunchDesignix(Block blockIn, Properties builder)
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
			
			if(placedFacing == EnumFacing.EAST && context.getHitZ() >= 0.5F || placedFacing == EnumFacing.WEST && context.getHitZ() < 0.5F
					|| placedFacing == EnumFacing.SOUTH && context.getHitX() < 0.5F || placedFacing == EnumFacing.NORTH && context.getHitX() >= 0.5F)
				pos = pos.offset(placedFacing.rotateY());
			
			if (!itemstack.isEmpty())
			{
				if(!canPlaceAt(context, pos, placedFacing))
					return EnumActionResult.FAIL;
				
				IBlockState state = getBlock().getDefaultState().with(BlockPunchDesignix.FACING, placedFacing);
				this.placeBlock(context, state);
				return EnumActionResult.SUCCESS;
			}
			return EnumActionResult.FAIL;
		}
	}
	
	public static boolean canPlaceAt(BlockItemUseContext context, BlockPos pos, EnumFacing facing)
	{
		for(int x = 0; x < 2; x++)
		{
			if(!context.getPlayer().canPlayerEdit(pos.offset(facing.rotateYCCW(), x), EnumFacing.UP, context.getItem()))
				return false;
			for(int y = 0; y < 2; y++)
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
		
		EnumFacing facing = context.getPlacementHorizontalFacing().getOpposite();
		
		if(facing == EnumFacing.EAST && context.getHitZ() >= 0.5F || facing == EnumFacing.WEST && context.getHitZ() < 0.5F
				|| facing == EnumFacing.SOUTH && context.getHitX() < 0.5F || facing == EnumFacing.NORTH && context.getHitX() >= 0.5F)
			pos = pos.offset(facing.rotateY());
		
		world.setBlockState(pos, MinestuckBlocks.PUNCH_DESIGNIX.LEFT_LEG.getDefaultState().with(BlockPunchDesignix.FACING, facing), 11);
		world.setBlockState(pos.offset(facing.rotateYCCW()), MinestuckBlocks.PUNCH_DESIGNIX.RIGHT_LEG.getDefaultState().with(BlockPunchDesignix.FACING, facing), 11);
		world.setBlockState(pos.up().offset(facing.rotateYCCW()), MinestuckBlocks.PUNCH_DESIGNIX.KEYBOARD.getDefaultState().with(BlockPunchDesignix.FACING, facing), 11);
		
		world.setBlockState(pos.up(), MinestuckBlocks.PUNCH_DESIGNIX.SLOT.getDefaultState().with(BlockPunchDesignix.FACING, facing), 11);
		
		if(player instanceof EntityPlayerMP)
			CriteriaTriggers.PLACED_BLOCK.trigger((EntityPlayerMP) player, pos, context.getItem());
		
		return true;
	}
}
