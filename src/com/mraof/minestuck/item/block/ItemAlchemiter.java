package com.mraof.minestuck.item.block;

import com.mraof.minestuck.block.BlockAlchemiter;
import com.mraof.minestuck.block.MinestuckBlocks;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemAlchemiter extends BlockItem
{
	
	public ItemAlchemiter(Block blockIn, Properties builder)
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
			
			if(facing == Direction.WEST && context.getHitZ() >= 0.5F || facing == Direction.EAST && context.getHitZ() < 0.5F
					|| facing == Direction.NORTH && context.getHitX() < 0.5F || facing == Direction.SOUTH && context.getHitX() >= 0.5F)
				pos = pos.offset(facing.rotateYCCW());
			
			if (!itemstack.isEmpty())
			{
				if(!canPlaceAt(context, pos, facing))
					return ActionResultType.FAIL;
				
				BlockState state = getBlock().getDefaultState().with(BlockAlchemiter.FACING, facing);
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
				if(!context.getPlayer().canPlayerEdit(pos.offset(facing.rotateY(), x).offset(facing, z), EnumFacing.UP, context.getItem()))
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
	protected boolean placeBlock(BlockItemUseContext context, IBlockState newState)
	{
		BlockPos pos = context.getPos();
		World world = context.getWorld();
		PlayerEntity player = context.getPlayer();
		if(!world.isRemote)
		{
			Direction facing = context.getPlacementHorizontalFacing();
			
			pos = pos.offset(facing.rotateYCCW());
			
			if(facing == Direction.WEST && context.getHitZ() >= 0.5F || facing == Direction.EAST && context.getHitZ() < 0.5F
					|| facing == Direction.NORTH && context.getHitX() < 0.5F || facing == Direction.SOUTH && context.getHitX() >= 0.5F)
				pos = pos.offset(facing.rotateYCCW());
			
			world.setBlockState(pos.offset(facing,3).offset(facing.rotateY(),3).up(0), MinestuckBlocks.ALCHEMITER.TOTEM_CORNER.getDefaultState().with(BlockAlchemiter.FACING, facing));
			world.setBlockState(pos.offset(facing,3).offset(facing.rotateY(),3).up(1), MinestuckBlocks.ALCHEMITER.TOTEM_PAD.getDefaultState().with(BlockAlchemiter.FACING, facing));
			world.setBlockState(pos.offset(facing,3).offset(facing.rotateY(),3).up(2), MinestuckBlocks.ALCHEMITER.LOWER_ROD.getDefaultState().with(BlockAlchemiter.FACING, facing));
			world.setBlockState(pos.offset(facing,3).offset(facing.rotateY(),3).up(3), MinestuckBlocks.ALCHEMITER.UPPER_ROD.getDefaultState().with(BlockAlchemiter.FACING, facing));
			
			world.setBlockState(pos.offset(facing,0).offset(facing.rotateY(),0), MinestuckBlocks.ALCHEMITER.CORNER.getDefaultState().with(BlockAlchemiter.FACING, facing.getOpposite()));
			world.setBlockState(pos.offset(facing,0).offset(facing.rotateY(),1), MinestuckBlocks.ALCHEMITER.LEFT_SIDE.getDefaultState().with(BlockAlchemiter.FACING, facing.getOpposite()));
			world.setBlockState(pos.offset(facing,0).offset(facing.rotateY(),2), MinestuckBlocks.ALCHEMITER.RIGHT_SIDE.getDefaultState().with(BlockAlchemiter.FACING, facing.getOpposite()));
			world.setBlockState(pos.offset(facing,1).offset(facing.rotateY(),1), MinestuckBlocks.ALCHEMITER.CENTER.getDefaultState().with(BlockAlchemiter.FACING, facing.getOpposite()));
			world.setBlockState(pos.offset(facing,0).offset(facing.rotateY(),3), MinestuckBlocks.ALCHEMITER.CORNER.getDefaultState().with(BlockAlchemiter.FACING, facing.rotateY()));
			world.setBlockState(pos.offset(facing,1).offset(facing.rotateY(),2), MinestuckBlocks.ALCHEMITER.CENTER.getDefaultState().with(BlockAlchemiter.FACING, facing.rotateY()));
			world.setBlockState(pos.offset(facing,1).offset(facing.rotateY(),3), MinestuckBlocks.ALCHEMITER.LEFT_SIDE.getDefaultState().with(BlockAlchemiter.FACING, facing.rotateY()));
			world.setBlockState(pos.offset(facing,2).offset(facing.rotateY(),3), MinestuckBlocks.ALCHEMITER.RIGHT_SIDE.getDefaultState().with(BlockAlchemiter.FACING, facing.rotateY()));
			world.setBlockState(pos.offset(facing,1).offset(facing.rotateY(),0), MinestuckBlocks.ALCHEMITER.RIGHT_SIDE.getDefaultState().with(BlockAlchemiter.FACING, facing.rotateYCCW()));
			world.setBlockState(pos.offset(facing,2).offset(facing.rotateY(),0), MinestuckBlocks.ALCHEMITER.LEFT_SIDE.getDefaultState().with(BlockAlchemiter.FACING, facing.rotateYCCW()));
			world.setBlockState(pos.offset(facing,2).offset(facing.rotateY(),1), MinestuckBlocks.ALCHEMITER.CENTER.getDefaultState().with(BlockAlchemiter.FACING, facing.rotateYCCW()));
			world.setBlockState(pos.offset(facing,3).offset(facing.rotateY(),0), MinestuckBlocks.ALCHEMITER.CORNER.getDefaultState().with(BlockAlchemiter.FACING, facing.rotateYCCW()));
			world.setBlockState(pos.offset(facing,2).offset(facing.rotateY(),2), MinestuckBlocks.ALCHEMITER.CENTER.getDefaultState().with(BlockAlchemiter.FACING, facing));
			world.setBlockState(pos.offset(facing,3).offset(facing.rotateY(),1), MinestuckBlocks.ALCHEMITER.RIGHT_SIDE.getDefaultState().with(BlockAlchemiter.FACING, facing));
			world.setBlockState(pos.offset(facing,3).offset(facing.rotateY(),2), MinestuckBlocks.ALCHEMITER.LEFT_SIDE.getDefaultState().with(BlockAlchemiter.FACING, facing));
			
			if(player instanceof ServerPlayerEntity)
				CriteriaTriggers.PLACED_BLOCK.trigger((ServerPlayerEntity) player, pos, context.getItem());
		}
		return true;
	}
}