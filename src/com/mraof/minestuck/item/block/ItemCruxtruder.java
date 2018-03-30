package com.mraof.minestuck.item.block;

import com.mraof.minestuck.block.BlockCruxtruder;
import com.mraof.minestuck.block.BlockCruxtruder2;
import com.mraof.minestuck.block.MinestuckBlocks;
import com.mraof.minestuck.block.BlockCruxtruder.EnumParts;

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

public class ItemCruxtruder extends ItemBlock
{
	public ItemCruxtruder(Block block)
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
			
			if(placedFacing.getFrontOffsetX() > 0 && hitZ >= 0.5F || placedFacing.getFrontOffsetX() < 0 && hitZ < 0.5F
					|| placedFacing.getFrontOffsetZ() > 0 && hitX < 0.5F || placedFacing.getFrontOffsetZ() < 0 && hitX >= 0.5F)
				pos = pos.offset(placedFacing.rotateY());
			
			if (!itemstack.isEmpty())
			{
				if(!canPlaceAt(itemstack, player, worldIn, pos, placedFacing))
					return EnumActionResult.FAIL;
				
				IBlockState state = this.block.getDefaultState();
				this.placeBlockAt(itemstack, player, worldIn, pos, facing, hitX, hitY, hitZ, state);
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

		
		
		EnumFacing facing = EnumFacing.getHorizontal(MathHelper.floor((double)(player.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3).getOpposite();
		switch (facing) {

		case EAST:pos = pos.north(2).west(2);
			break;
		case NORTH:pos = pos.west(2);
			break;
		case SOUTH:pos=pos.north(2);
			break;
		case WEST:pos=pos;
			break;
		default:
			break;
		
		}
		
		
		if(player!=null && !(world.isRemote)){
			

			world.setBlockState(pos.south(1).up(0).east(1), MinestuckBlocks.cruxtruder2.getDefaultState().withProperty(BlockCruxtruder2.PART, BlockCruxtruder2.EnumParts.ONE_ONE_ONE));
			world.setBlockState(pos.south(1).up(1).east(1), MinestuckBlocks.cruxtruder2.getDefaultState().withProperty( BlockCruxtruder2.PART, BlockCruxtruder2.EnumParts.ONE_TWO_ONE));
			world.setBlockState(pos.south(1).up(2).east(1), MinestuckBlocks.cruxtruder2.getDefaultState().withProperty( BlockCruxtruder2.PART,  BlockCruxtruder2.EnumParts.ONE_THREE_ONE).withProperty(BlockCruxtruder2.HASLID, true));
			
			world.setBlockState(pos.south(0).up(1).east(0), newState.withProperty( BlockCruxtruder.PART, EnumParts.ZERO_TWO_ZERO));
			world.setBlockState(pos.south(0).up(1).east(1), newState.withProperty( BlockCruxtruder.PART,  EnumParts.ZERO_TWO_ONE));
			world.setBlockState(pos.south(0).up(1).east(2), newState.withProperty( BlockCruxtruder.PART,  EnumParts.ZERO_TWO_TWO));
			world.setBlockState(pos.south(1).up(1).east(0), newState.withProperty( BlockCruxtruder.PART,  EnumParts.ONE_TWO_ZERO));
			world.setBlockState(pos.south(1).up(1).east(2), newState.withProperty( BlockCruxtruder.PART,  EnumParts.ONE_TWO_TWO));
			world.setBlockState(pos.south(2).up(1).east(0), newState.withProperty( BlockCruxtruder.PART,  EnumParts.TWO_TWO_ZERO));
			world.setBlockState(pos.south(2).up(1).east(1), newState.withProperty( BlockCruxtruder.PART,  EnumParts.TWO_TWO_ONE));
			world.setBlockState(pos.south(2).up(1).east(2), newState.withProperty( BlockCruxtruder.PART,  EnumParts.TWO_TWO_TWO));
			
			
			world.setBlockState(pos.south(0).up(0).east(0), newState.withProperty( BlockCruxtruder.PART, EnumParts.ZERO_ONE_ZERO));
			world.setBlockState(pos.south(0).up(0).east(1), newState.withProperty( BlockCruxtruder.PART, EnumParts.ZERO_ONE_ONE));
			world.setBlockState(pos.south(0).up(0).east(2), newState.withProperty( BlockCruxtruder.PART, EnumParts.ZERO_ONE_TWO));;
			world.setBlockState(pos.south(1).up(0).east(0), newState.withProperty( BlockCruxtruder.PART, EnumParts.ONE_ONE_ZERO));
			world.setBlockState(pos.south(1).up(0).east(2), newState.withProperty( BlockCruxtruder.PART, EnumParts.ONE_ONE_TWO));
			world.setBlockState(pos.south(2).up(0).east(0), newState.withProperty( BlockCruxtruder.PART, EnumParts.TWO_ONE_ZERO));
			world.setBlockState(pos.south(2).up(0).east(1), newState.withProperty( BlockCruxtruder.PART, EnumParts.TWO_ONE_ONE));
			world.setBlockState(pos.south(2).up(0).east(2), newState.withProperty( BlockCruxtruder.PART, EnumParts.TWO_ONE_TWO));
		}
		
		
		
		
		return true;
	}
}
