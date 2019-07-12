package com.mraof.minestuck.item.block;

import com.mraof.minestuck.block.BlockCruxtruder;
import com.mraof.minestuck.block.MinestuckBlocks;
import com.mraof.minestuck.editmode.EditData;
import com.mraof.minestuck.editmode.ServerEditHandler;
import com.mraof.minestuck.tileentity.TileEntityCruxtruder;
import com.mraof.minestuck.util.Debug;
import com.mraof.minestuck.world.storage.PlayerSavedData;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemCruxtruder extends BlockItem
{
	//TODO Must be looked over along with the other large machine items
	public ItemCruxtruder(Block blockIn, Properties builder)
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
		if(world.isRemote)
		{
			return ActionResultType.SUCCESS;
		} else if(facing != Direction.UP)
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
			
			if(!itemstack.isEmpty())
			{
				if(!canPlaceAt(context, pos, placedFacing))
					return ActionResultType.FAIL;
				
				BlockState state = this.getBlock().getDefaultState();
				this.placeBlock(context, state);
				return ActionResultType.SUCCESS;
			}
			return ActionResultType.FAIL;
		}
	}
	
	public static boolean canPlaceAt(BlockItemUseContext context, BlockPos pos, Direction facing)
	{
		for(int x = 0; x < 3; x++)
		{
			for(int z = 0; z < 3; z++)
			{
				if(!context.getPlayer().canPlayerEdit(pos.offset(facing.rotateYCCW(), x).offset(facing.getOpposite(), z), EnumFacing.UP, context.getItem()))
					return false;
				for(int y = 0; y < 3; y++)
				{
					if(!context.getWorld().getBlockState(pos.offset(facing.getOpposite(), z).offset(facing.rotateYCCW(), x).up(y)).isReplaceable(context))
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
			Direction facing = context.getPlacementHorizontalFacing().getOpposite();
			switch (facing)
			{
				case EAST:
					pos = pos.north(1).west(2);
					break;
				case NORTH:
					pos = pos.west(1);
					break;
				case SOUTH:
					pos = pos.north(2).west(1);
					break;
				case WEST:
					pos = pos.north(1);
					break;
			}
			
			world.setBlockState(pos.south(0).up(0).east(0), MinestuckBlocks.CRUXTRUDER.CORNER.getDefaultState().with(BlockCruxtruder.FACING, EnumFacing.NORTH));
			world.setBlockState(pos.south(0).up(0).east(1), MinestuckBlocks.CRUXTRUDER.SIDE.getDefaultState().with(BlockCruxtruder.FACING, EnumFacing.NORTH));
			world.setBlockState(pos.south(0).up(0).east(2), MinestuckBlocks.CRUXTRUDER.CORNER.getDefaultState().with(BlockCruxtruder.FACING, EnumFacing.EAST));
			world.setBlockState(pos.south(1).up(0).east(2), MinestuckBlocks.CRUXTRUDER.SIDE.getDefaultState().with(BlockCruxtruder.FACING, EnumFacing.EAST));
			world.setBlockState(pos.south(2).up(0).east(2), MinestuckBlocks.CRUXTRUDER.CORNER.getDefaultState().with(BlockCruxtruder.FACING, EnumFacing.SOUTH));
			world.setBlockState(pos.south(2).up(0).east(1), MinestuckBlocks.CRUXTRUDER.SIDE.getDefaultState().with(BlockCruxtruder.FACING, EnumFacing.SOUTH));
			world.setBlockState(pos.south(2).up(0).east(0), MinestuckBlocks.CRUXTRUDER.CORNER.getDefaultState().with(BlockCruxtruder.FACING, EnumFacing.WEST));
			world.setBlockState(pos.south(1).up(0).east(0), MinestuckBlocks.CRUXTRUDER.SIDE.getDefaultState().with(BlockCruxtruder.FACING, EnumFacing.WEST));
			world.setBlockState(pos.south(1).up(0).east(1), MinestuckBlocks.CRUXTRUDER.CENTER.getDefaultState().with(BlockCruxtruder.FACING, facing));
			world.setBlockState(pos.south(1).up(1).east(1), MinestuckBlocks.CRUXTRUDER.TUBE.getDefaultState().with(BlockCruxtruder.FACING, facing));
			world.setBlockState(pos.south().up(2).east(), MinestuckBlocks.CRUXTRUDER_LID.getDefaultState());
			
			TileEntity te = world.getTileEntity(pos.add( 1, 1, 1));
			if(te instanceof TileEntityCruxtruder)
			{
				int color;
				EditData editData = ServerEditHandler.getData(player);
				if(editData != null)
					color = PlayerSavedData.get(world).getData(editData.getTarget()).color;
				else color = PlayerSavedData.getData((ServerPlayerEntity) player).color;
				
				((TileEntityCruxtruder) te).setColor(color);
			} else Debug.warnf("Placed cruxtruder, but can't find tile entity. Instead found %s.", te);
			
			if(player instanceof ServerPlayerEntity)
				CriteriaTriggers.PLACED_BLOCK.trigger((ServerPlayerEntity) player, pos, context.getItem());
		}
		
		return true;
	}
}
