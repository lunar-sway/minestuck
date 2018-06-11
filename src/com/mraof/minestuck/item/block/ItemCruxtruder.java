package com.mraof.minestuck.item.block;

import com.mraof.minestuck.block.BlockCruxtruder;
import com.mraof.minestuck.block.BlockCruxtruder.EnumParts;
import com.mraof.minestuck.block.MinestuckBlocks;
import com.mraof.minestuck.editmode.EditData;
import com.mraof.minestuck.editmode.ServerEditHandler;
import com.mraof.minestuck.tileentity.TileEntityCruxtruder;
import com.mraof.minestuck.util.Debug;
import com.mraof.minestuck.util.MinestuckPlayerData;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class ItemCruxtruder extends ItemBlock
{
	public ItemCruxtruder(Block block)
	{
		super(block);
	}
	
	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
	{
		
		if (world.isRemote)
		{
			return EnumActionResult.SUCCESS;
		} else if (facing != EnumFacing.UP)
		{
			return EnumActionResult.FAIL;
		} else
		{
			Block block = world.getBlockState(pos).getBlock();
			boolean flag = block.isReplaceable(world, pos);
			
			if (!flag)
			{
				pos = pos.up();
			}
			
			EnumFacing placedFacing = player.getHorizontalFacing().getOpposite();
			ItemStack itemstack = player.getHeldItem(hand);
			
			pos = pos.offset(placedFacing.rotateY());
			
			if (!itemstack.isEmpty())
			{
				if(!canPlaceAt(itemstack, player, world, pos, placedFacing))
					return EnumActionResult.FAIL;
				
				IBlockState state = this.block.getDefaultState();
				this.placeBlockAt(itemstack, player, world, pos, facing, hitX, hitY, hitZ, state);
				return EnumActionResult.SUCCESS;
			}
			return EnumActionResult.FAIL;
		}
	}
	
	public static boolean canPlaceAt(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumFacing facing)
	{
		for (int x = 0; x < 3; x++)
		{
			if (!player.canPlayerEdit(pos.offset(facing.rotateYCCW(), x), EnumFacing.UP, stack))
				return false;
			for (int y = 0; y < 3; y++)
			{
				for(int z=0;z<3;z++) {
					if (!world.mayPlace(MinestuckBlocks.cruxtruder, pos.offset(facing.getOpposite(),z).offset(facing.rotateYCCW(), x).up(y), false, EnumFacing.UP, null))
						return false;
				}
			}
		}
		return true;
	}
	
	@Override
	public boolean placeBlockAt(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, IBlockState newState)
	{
		if(!world.isRemote)
		{
			EnumFacing facing = player.getHorizontalFacing().getOpposite();
			switch (facing)
			{
				case EAST:
					pos = pos.north(2).west(2);
					break;
				case NORTH:
					pos = pos.west(2);
					break;
				case SOUTH:
					pos = pos.north(2);
					break;
				case WEST:
				default:
					break;
			}
			
			world.setBlockState(pos.south(0).up(0).east(0), newState.withProperty(BlockCruxtruder.PART, EnumParts.BASE_CORNER).withProperty(BlockCruxtruder.DIRECTION, EnumFacing.NORTH));
			world.setBlockState(pos.south(0).up(0).east(1), newState.withProperty(BlockCruxtruder.PART, EnumParts.BASE_SIDE).withProperty(BlockCruxtruder.DIRECTION, EnumFacing.NORTH));
			world.setBlockState(pos.south(0).up(0).east(2), newState.withProperty(BlockCruxtruder.PART, EnumParts.BASE_CORNER).withProperty(BlockCruxtruder.DIRECTION, EnumFacing.EAST));
			world.setBlockState(pos.south(1).up(0).east(2), newState.withProperty(BlockCruxtruder.PART, EnumParts.BASE_SIDE).withProperty(BlockCruxtruder.DIRECTION, EnumFacing.EAST));
			world.setBlockState(pos.south(2).up(0).east(2), newState.withProperty(BlockCruxtruder.PART, EnumParts.BASE_CORNER).withProperty(BlockCruxtruder.DIRECTION, EnumFacing.SOUTH));
			world.setBlockState(pos.south(2).up(0).east(1), newState.withProperty(BlockCruxtruder.PART, EnumParts.BASE_SIDE).withProperty(BlockCruxtruder.DIRECTION, EnumFacing.SOUTH));
			world.setBlockState(pos.south(2).up(0).east(0), newState.withProperty(BlockCruxtruder.PART, EnumParts.BASE_CORNER).withProperty(BlockCruxtruder.DIRECTION, EnumFacing.WEST));
			world.setBlockState(pos.south(1).up(0).east(0), newState.withProperty(BlockCruxtruder.PART, EnumParts.BASE_SIDE).withProperty(BlockCruxtruder.DIRECTION, EnumFacing.WEST));
			world.setBlockState(pos.south(1).up(0).east(1), newState.withProperty(BlockCruxtruder.PART, EnumParts.CENTER).withProperty(BlockCruxtruder.DIRECTION, facing));
			world.setBlockState(pos.south(1).up(1).east(1), newState.withProperty(BlockCruxtruder.PART, EnumParts.TUBE).withProperty(BlockCruxtruder.DIRECTION, facing));
			world.setBlockState(pos.south().up(2).east(), MinestuckBlocks.cruxtruderLid.getDefaultState());
			
			TileEntity te = world.getTileEntity(pos.add( 1, 1, 1));
			if(te instanceof TileEntityCruxtruder)
			{
				int color;
				EditData editData = ServerEditHandler.getData(player);
				if(editData != null)
					color = MinestuckPlayerData.getData(editData.getTarget()).color;
				else color = MinestuckPlayerData.getData(player).color;
				
				((TileEntityCruxtruder) te).setColor(color);
			} else Debug.warnf("Placed cruxtruder, but can't find tile entity. Instead found %s.", te);
		}
		
		return true;
	}
}
