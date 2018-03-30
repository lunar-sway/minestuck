package com.mraof.minestuck.item.block;

import com.mraof.minestuck.block.BlockAlchemiter;
import com.mraof.minestuck.block.BlockAlchemiter.EnumParts;
import com.mraof.minestuck.block.MinestuckBlocks;
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

public class ItemAlchemiter extends ItemBlock
{
	public ItemAlchemiter(Block block)
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
			
			pos = pos.offset(placedFacing.rotateY());
			
			if(placedFacing.getFrontOffsetX() > 0 && hitZ >= 0.5F || placedFacing.getFrontOffsetX() < 0 && hitZ < 0.5F
					|| placedFacing.getFrontOffsetZ() > 0 && hitX < 0.5F || placedFacing.getFrontOffsetZ() < 0 && hitX >= 0.5F)
				pos = pos.offset(placedFacing.rotateY());
			
			if (!itemstack.isEmpty())
			{
				if(!canPlaceAt(itemstack, player, worldIn, pos, placedFacing))
					return EnumActionResult.FAIL;
				
				IBlockState state = this.block.getDefaultState().withProperty(BlockAlchemiter.DIRECTION, placedFacing);
				this.placeBlockAt(itemstack, player, worldIn, pos, facing, hitX, hitY, hitZ, state);
				return EnumActionResult.SUCCESS;
			}
			return EnumActionResult.FAIL;
		}
	}
	

	public static boolean canPlaceAt(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumFacing facing)
	{
		for (int x = 0; x < 4; x++)
		{
			if (!player.canPlayerEdit(pos.offset(facing.rotateYCCW(), x), EnumFacing.UP, stack))
				return false;
			for (int y = 0; y < 4; y++)
			{
				for(int z=0;z<4;z++) {
					if (!world.mayPlace(MinestuckBlocks.alchemiter[0], pos.offset(facing.getOpposite(),z).offset(facing.rotateYCCW(), x).up(y), false, EnumFacing.UP, null))
						return false;
				}
			}
		}
		return true;
	}
	@Override
	public boolean placeBlockAt(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, IBlockState newState)
	{
		EnumFacing facing = EnumFacing.getHorizontal(MathHelper.floor((double)(player.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3);
		if(player!=null && !(world.isRemote)){
			world.setBlockState(pos.up(0), BlockAlchemiter.getBlockState(EnumParts.TOTEM_CORNER, facing));
			world.setBlockState(pos.up(1), BlockAlchemiter.getBlockState(EnumParts.TOTEM_PAD, facing));
			world.setBlockState(pos.up(2), BlockAlchemiter.getBlockState(EnumParts.LOWER_ROD, facing));
			world.setBlockState(pos.up(3), BlockAlchemiter.getBlockState(EnumParts.UPPER_ROD, facing));
			
			world.setBlockState(pos.offset(facing,0).offset(facing.rotateY(),1), BlockAlchemiter.getBlockState(EnumParts.EDGE_LEFT, facing));
			world.setBlockState(pos.offset(facing,0).offset(facing.rotateY(),2), BlockAlchemiter.getBlockState(EnumParts.EDGE_RIGHT, facing));
			world.setBlockState(pos.offset(facing,0).offset(facing.rotateY(),3), BlockAlchemiter.getBlockState(EnumParts.CORNER, facing));
			world.setBlockState(pos.offset(facing,1).offset(facing.rotateY(),1), BlockAlchemiter.getBlockState(EnumParts.CENTER_PAD, facing));
			world.setBlockState(pos.offset(facing,1).offset(facing.rotateY(),0), BlockAlchemiter.getBlockState(EnumParts.EDGE_RIGHT, facing.rotateY()));
			world.setBlockState(pos.offset(facing,1).offset(facing.rotateY(),2), BlockAlchemiter.getBlockState(EnumParts.CENTER_PAD, facing.rotateYCCW()));
			world.setBlockState(pos.offset(facing,1).offset(facing.rotateY(),3), BlockAlchemiter.getBlockState(EnumParts.EDGE_LEFT, facing.rotateYCCW()));
			world.setBlockState(pos.offset(facing,2).offset(facing.rotateY(),0), BlockAlchemiter.getBlockState(EnumParts.EDGE_LEFT, facing.rotateY()));
			world.setBlockState(pos.offset(facing,2).offset(facing.rotateY(),1), BlockAlchemiter.getBlockState(EnumParts.CENTER_PAD, facing.rotateY()));
			world.setBlockState(pos.offset(facing,2).offset(facing.rotateY(),2), BlockAlchemiter.getBlockState(EnumParts.CENTER_PAD, facing.getOpposite()));
			world.setBlockState(pos.offset(facing,2).offset(facing.rotateY(),3), BlockAlchemiter.getBlockState(EnumParts.EDGE_RIGHT, facing.rotateYCCW()));
			world.setBlockState(pos.offset(facing,3).offset(facing.rotateY(),0), BlockAlchemiter.getBlockState(EnumParts.CORNER, facing.getOpposite()));
			world.setBlockState(pos.offset(facing,3).offset(facing.rotateY(),1), BlockAlchemiter.getBlockState(EnumParts.EDGE_RIGHT, facing.getOpposite()));
			world.setBlockState(pos.offset(facing,3).offset(facing.rotateY(),2), BlockAlchemiter.getBlockState(EnumParts.EDGE_LEFT, facing.getOpposite()));
			world.setBlockState(pos.offset(facing,3).offset(facing.rotateY(),3), BlockAlchemiter.getBlockState(EnumParts.CORNER, facing.rotateYCCW()));
		}
		return true;
	}
}


