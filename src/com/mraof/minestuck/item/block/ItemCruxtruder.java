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
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemCruxtruder extends ItemBlock
{
	//TODO Must be looked over along with the other large machine items
	public ItemCruxtruder(Block blockIn, Properties builder)
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
		if(world.isRemote)
		{
			return EnumActionResult.SUCCESS;
		} else if(facing != EnumFacing.UP)
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
			
			if(!itemstack.isEmpty())
			{
				if(!canPlaceAt(context, pos, placedFacing))
					return EnumActionResult.FAIL;
				
				IBlockState state = this.getBlock().getDefaultState();
				this.placeBlock(context, state);
				return EnumActionResult.SUCCESS;
			}
			return EnumActionResult.FAIL;
		}
	}
	
	public static boolean canPlaceAt(BlockItemUseContext context, BlockPos pos, EnumFacing facing)
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
	protected boolean placeBlock(BlockItemUseContext context, IBlockState newState)
	{
		BlockPos pos = context.getPos();
		World world = context.getWorld();
		EntityPlayer player = context.getPlayer();
		if(!world.isRemote)
		{
			EnumFacing facing = context.getPlacementHorizontalFacing().getOpposite();
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
				else color = PlayerSavedData.getData((EntityPlayerMP) player).color;
				
				((TileEntityCruxtruder) te).setColor(color);
			} else Debug.warnf("Placed cruxtruder, but can't find tile entity. Instead found %s.", te);
			
			if(player instanceof EntityPlayerMP)
				CriteriaTriggers.PLACED_BLOCK.trigger((EntityPlayerMP) player, pos, context.getItem());
		}
		
		return true;
	}
}
