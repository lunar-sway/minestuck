package com.mraof.minestuck.item.block;

import com.mraof.minestuck.block.PunchDesignixBlock;
import com.mraof.minestuck.block.MinestuckBlocks;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class PunchDesignixItem extends BlockItem
{
	public PunchDesignixItem(Block blockIn, Properties builder)
	{
		super(blockIn, builder);
	}
	
	@Override
	public ActionResultType tryPlace(BlockItemUseContext context)
	{
		World world = context.getWorld();
		Direction facing = context.getFace();
		BlockPos pos = context.getPos();
		PlayerEntity player = context.getPlayer();
		if (world.isRemote)
		{
			return ActionResultType.SUCCESS;
		} else if (facing != Direction.UP)
		{
			return ActionResultType.FAIL;
		} else
		{
			BlockState block = world.getBlockState(pos);
			boolean flag = block.isReplaceable(context);
			
			if (!flag)
			{
				pos = pos.up();
			}
			
			Direction placedFacing = context.getPlacementHorizontalFacing().getOpposite();
			ItemStack itemstack = context.getItem();
			
			if(placedFacing == Direction.EAST && context.getHitVec().z >= 0.5F || placedFacing == Direction.WEST && context.getHitVec().z < 0.5F
					|| placedFacing == Direction.SOUTH && context.getHitVec().x < 0.5F || placedFacing == Direction.NORTH && context.getHitVec().x >= 0.5F)
				pos = pos.offset(placedFacing.rotateY());
			
			if (!itemstack.isEmpty())
			{
				if(!canPlaceAt(context, pos, placedFacing))
					return ActionResultType.FAIL;
				
				BlockState state = getBlock().getDefaultState().with(PunchDesignixBlock.FACING, placedFacing);
				this.placeBlock(context, state);
				return ActionResultType.SUCCESS;
			}
			return ActionResultType.FAIL;
		}
	}
	
	public static boolean canPlaceAt(BlockItemUseContext context, BlockPos pos, Direction facing)
	{
		for(int x = 0; x < 2; x++)
		{
			if(!context.getPlayer().canPlayerEdit(pos.offset(facing.rotateYCCW(), x), Direction.UP, context.getItem()))
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
	protected boolean placeBlock(BlockItemUseContext context, BlockState newState)
	{
		BlockPos pos = context.getPos();
		World world = context.getWorld();
		PlayerEntity player = context.getPlayer();
		
		Direction facing = context.getPlacementHorizontalFacing().getOpposite();
		
		if(facing == Direction.EAST && context.getHitVec().z >= 0.5F || facing == Direction.WEST && context.getHitVec().z < 0.5F
				|| facing == Direction.SOUTH && context.getHitVec().x < 0.5F || facing == Direction.NORTH && context.getHitVec().x >= 0.5F)
			pos = pos.offset(facing.rotateY());
		
		world.setBlockState(pos, MinestuckBlocks.PUNCH_DESIGNIX.LEFT_LEG.getDefaultState().with(PunchDesignixBlock.FACING, facing), 11);
		world.setBlockState(pos.offset(facing.rotateYCCW()), MinestuckBlocks.PUNCH_DESIGNIX.RIGHT_LEG.getDefaultState().with(PunchDesignixBlock.FACING, facing), 11);
		world.setBlockState(pos.up().offset(facing.rotateYCCW()), MinestuckBlocks.PUNCH_DESIGNIX.KEYBOARD.getDefaultState().with(PunchDesignixBlock.FACING, facing), 11);
		
		world.setBlockState(pos.up(), MinestuckBlocks.PUNCH_DESIGNIX.SLOT.getDefaultState().with(PunchDesignixBlock.FACING, facing), 11);
		
		if(player instanceof ServerPlayerEntity)
			CriteriaTriggers.PLACED_BLOCK.trigger((ServerPlayerEntity) player, pos, context.getItem());
		
		return true;
	}
}
