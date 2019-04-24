package com.mraof.minestuck.item.block;

import com.mraof.minestuck.block.BlockAlchemiter;
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

public class ItemAlchemiter extends ItemBlock
{
	
	public ItemAlchemiter(Block blockIn, Properties builder)
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
			
			EnumFacing placedFacing = context.getPlacementHorizontalFacing();
			ItemStack itemstack = context.getItem();
			
			pos = pos.offset(placedFacing.rotateY());
			
			if(placedFacing == EnumFacing.EAST && context.getHitZ() >= 0.5F || placedFacing == EnumFacing.WEST && context.getHitZ() < 0.5F
					|| placedFacing == EnumFacing.SOUTH && context.getHitX() < 0.5F || placedFacing == EnumFacing.NORTH && context.getHitX() >= 0.5F)
				pos = pos.offset(placedFacing.rotateY());
			
			if (!itemstack.isEmpty())
			{
				if(!canPlaceAt(itemstack, player, world, pos, placedFacing))
					return EnumActionResult.FAIL;
				
				IBlockState state = getBlock().getDefaultState().with(BlockAlchemiter.FACING, placedFacing);
				this.placeBlock(context, state);
				return EnumActionResult.SUCCESS;
			}
			return EnumActionResult.FAIL;
		}
	}
	

	public static boolean canPlaceAt(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumFacing facing)
	{
		for(int x = 0; x < 4; x++)
		{
			if(!player.canPlayerEdit(pos.offset(facing.rotateYCCW(), x), EnumFacing.UP, stack))
				return false;
			for(int y = 0; y < 4; y++)
			{
				for(int z = 0; z < 4; z++) {
					if(!MinestuckBlocks.ALCHEMITER_CENTER.getDefaultState().isValidPosition(world, pos.offset(facing.getOpposite(),z).offset(facing.rotateYCCW(), x).up(y)))
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
			EnumFacing facing = context.getPlacementHorizontalFacing();
			world.setBlockState(pos.up(0), MinestuckBlocks.ALCHEMITER_TOTEM_CORNER.getDefaultState().with(BlockAlchemiter.FACING, facing));
			world.setBlockState(pos.up(1), MinestuckBlocks.ALCHEMITER_TOTEM_PAD.getDefaultState().with(BlockAlchemiter.FACING, facing));
			world.setBlockState(pos.up(2), MinestuckBlocks.ALCHEMITER_LOWER_ROD.getDefaultState().with(BlockAlchemiter.FACING, facing));
			world.setBlockState(pos.up(3), MinestuckBlocks.ALCHEMITER_UPPER_ROD.getDefaultState().with(BlockAlchemiter.FACING, facing));
			
			world.setBlockState(pos.offset(facing,0).offset(facing.rotateY(),1), MinestuckBlocks.ALCHEMITER_LEFT_SIDE.getDefaultState().with(BlockAlchemiter.FACING, facing));
			world.setBlockState(pos.offset(facing,0).offset(facing.rotateY(),2), MinestuckBlocks.ALCHEMITER_RIGHT_SIDE.getDefaultState().with(BlockAlchemiter.FACING, facing));
			world.setBlockState(pos.offset(facing,0).offset(facing.rotateY(),3), MinestuckBlocks.ALCHEMITER_CORNER.getDefaultState().with(BlockAlchemiter.FACING, facing.rotateYCCW()));
			world.setBlockState(pos.offset(facing,1).offset(facing.rotateY(),1), MinestuckBlocks.ALCHEMITER_CENTER.getDefaultState().with(BlockAlchemiter.FACING, facing));
			world.setBlockState(pos.offset(facing,1).offset(facing.rotateY(),0), MinestuckBlocks.ALCHEMITER_RIGHT_SIDE.getDefaultState().with(BlockAlchemiter.FACING, facing.rotateY()));
			world.setBlockState(pos.offset(facing,1).offset(facing.rotateY(),2), MinestuckBlocks.ALCHEMITER_CENTER.getDefaultState().with(BlockAlchemiter.FACING, facing.rotateYCCW()));
			world.setBlockState(pos.offset(facing,1).offset(facing.rotateY(),3), MinestuckBlocks.ALCHEMITER_LEFT_SIDE.getDefaultState().with(BlockAlchemiter.FACING, facing.rotateYCCW()));
			world.setBlockState(pos.offset(facing,2).offset(facing.rotateY(),0), MinestuckBlocks.ALCHEMITER_LEFT_SIDE.getDefaultState().with(BlockAlchemiter.FACING, facing.rotateY()));
			world.setBlockState(pos.offset(facing,2).offset(facing.rotateY(),1), MinestuckBlocks.ALCHEMITER_CENTER.getDefaultState().with(BlockAlchemiter.FACING, facing.rotateY()));
			world.setBlockState(pos.offset(facing,2).offset(facing.rotateY(),2), MinestuckBlocks.ALCHEMITER_CENTER.getDefaultState().with(BlockAlchemiter.FACING, facing.getOpposite()));
			world.setBlockState(pos.offset(facing,2).offset(facing.rotateY(),3), MinestuckBlocks.ALCHEMITER_RIGHT_SIDE.getDefaultState().with(BlockAlchemiter.FACING, facing.rotateYCCW()));
			world.setBlockState(pos.offset(facing,3).offset(facing.rotateY(),0), MinestuckBlocks.ALCHEMITER_CORNER.getDefaultState().with(BlockAlchemiter.FACING, facing.rotateY()));
			world.setBlockState(pos.offset(facing,3).offset(facing.rotateY(),1), MinestuckBlocks.ALCHEMITER_RIGHT_SIDE.getDefaultState().with(BlockAlchemiter.FACING, facing.getOpposite()));
			world.setBlockState(pos.offset(facing,3).offset(facing.rotateY(),2), MinestuckBlocks.ALCHEMITER_LEFT_SIDE.getDefaultState().with(BlockAlchemiter.FACING, facing.getOpposite()));
			world.setBlockState(pos.offset(facing,3).offset(facing.rotateY(),3), MinestuckBlocks.ALCHEMITER_CORNER.getDefaultState().with(BlockAlchemiter.FACING, facing.getOpposite()));
			
			if(player instanceof EntityPlayerMP)
				CriteriaTriggers.PLACED_BLOCK.trigger((EntityPlayerMP) player, pos, context.getItem());
		}
		return true;
	}
}