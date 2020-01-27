package com.mraof.minestuck.item.block;

import com.mraof.minestuck.block.MSBlocks;
import com.mraof.minestuck.block.TotemLatheBlock;
import com.mraof.minestuck.util.MSRotationUtil;
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

public class TotemLatheItem extends BlockItem
{
	
	public TotemLatheItem(Block blockIn, Properties builder)
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
			
			pos = pos.offset(placedFacing.rotateY());
			
			if(placedFacing == Direction.EAST && context.getHitVec().z >= 0.5F || placedFacing == Direction.WEST && context.getHitVec().z < 0.5F
					|| placedFacing == Direction.SOUTH && context.getHitVec().x < 0.5F || placedFacing == Direction.NORTH && context.getHitVec().x >= 0.5F)
				pos = pos.offset(placedFacing.rotateY());
			
			if (!itemstack.isEmpty())
			{
				if(!canPlaceAt(context, pos, placedFacing))
					return ActionResultType.FAIL;
				
				BlockState state = getBlock().getDefaultState().with(TotemLatheBlock.FACING, placedFacing);
				this.placeBlock(context, state);
				return ActionResultType.SUCCESS;
			}
			return ActionResultType.FAIL;
		}
	}
	
	public static boolean canPlaceAt(BlockItemUseContext context, BlockPos pos, Direction facing)
	{
		for(int x = 0; x < 4; x++)
		{
			if(!context.getPlayer().canPlayerEdit(pos.offset(facing.rotateYCCW(), x), Direction.UP, context.getItem()))
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
	protected boolean placeBlock(BlockItemUseContext context, BlockState newState)
	{
		BlockPos pos = context.getPos();
		World world = context.getWorld();
		PlayerEntity player = context.getPlayer();
		if(!world.isRemote)
		{
			Direction facing = context.getPlacementHorizontalFacing().getOpposite();
			
			pos = pos.offset(facing.rotateY());
			
			if(facing == Direction.EAST && context.getHitVec().z >= 0.5F || facing == Direction.WEST && context.getHitVec().z < 0.5F
					|| facing == Direction.SOUTH && context.getHitVec().x < 0.5F || facing == Direction.NORTH && context.getHitVec().x >= 0.5F)
				pos = pos.offset(facing.rotateY());
			
			MSBlocks.TOTEM_LATHE.placeWithRotation(world, pos, MSRotationUtil.fromDirection(facing));
			
			if(player instanceof ServerPlayerEntity)
				CriteriaTriggers.PLACED_BLOCK.trigger((ServerPlayerEntity) player, pos, context.getItem());
		}
		return true;
	}
}
