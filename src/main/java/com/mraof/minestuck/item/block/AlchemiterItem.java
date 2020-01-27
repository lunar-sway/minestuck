package com.mraof.minestuck.item.block;

import com.mraof.minestuck.block.AlchemiterBlock;
import com.mraof.minestuck.block.MSBlocks;
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

public class AlchemiterItem extends BlockItem
{
	
	public AlchemiterItem(Block blockIn, Properties builder)
	{
		super(blockIn, builder);
	}
	
	@Override
	public ActionResultType tryPlace(BlockItemUseContext context)
	{
		World world = context.getWorld();
		Direction sideFace = context.getFace();
		BlockPos pos = context.getPos();
		PlayerEntity player = context.getPlayer();
		if (world.isRemote)
		{
			return ActionResultType.SUCCESS;
		} else if (sideFace != Direction.UP)
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
			
			Direction facing = context.getPlacementHorizontalFacing();
			ItemStack itemstack = context.getItem();
			
			pos = pos.offset(facing.rotateYCCW());
			
			if(facing == Direction.WEST && context.getHitVec().z >= 0.5F || facing == Direction.EAST && context.getHitVec().z < 0.5F
					|| facing == Direction.NORTH && context.getHitVec().x < 0.5F || facing == Direction.SOUTH && context.getHitVec().x >= 0.5F)
				pos = pos.offset(facing.rotateYCCW());
			
			if (!itemstack.isEmpty())
			{
				if(!canPlaceAt(context, pos, facing))
					return ActionResultType.FAIL;
				
				BlockState state = getBlock().getDefaultState().with(AlchemiterBlock.FACING, facing);
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
			for(int z = 0; z < 4; z++)
			{
				if(!context.getPlayer().canPlayerEdit(pos.offset(facing.rotateY(), x).offset(facing, z), Direction.UP, context.getItem()))
					return false;
				for(int y = 0; y < 4; y++)
				{
					if(!context.getWorld().getBlockState(pos.offset(facing, z).offset(facing.rotateY(), x).up(y)).isReplaceable(context))
						return false;
				}
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
			Direction facing = context.getPlacementHorizontalFacing();
			
			pos = pos.offset(facing.rotateYCCW());
			
			if(facing == Direction.WEST && context.getHitVec().z >= 0.5F || facing == Direction.EAST && context.getHitVec().z < 0.5F
					|| facing == Direction.NORTH && context.getHitVec().x < 0.5F || facing == Direction.SOUTH && context.getHitVec().x >= 0.5F)
				pos = pos.offset(facing.rotateYCCW());
			
			MSBlocks.ALCHEMITER.placeWithRotation(world, pos, MSRotationUtil.fromDirection(facing));
			
			if(player instanceof ServerPlayerEntity)
				CriteriaTriggers.PLACED_BLOCK.trigger((ServerPlayerEntity) player, pos, context.getItem());
		}
		return true;
	}
}