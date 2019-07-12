package com.mraof.minestuck.item.block;

import com.mraof.minestuck.block.AlchemiterBlock;
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
		EnumFacing sideFace = context.getFace();
		BlockPos pos = context.getPos();
		EntityPlayer player = context.getPlayer();
		if (world.isRemote)
		{
			return EnumActionResult.SUCCESS;
		} else if (sideFace != EnumFacing.UP)
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
			
			EnumFacing facing = context.getPlacementHorizontalFacing();
			ItemStack itemstack = context.getItem();
			
			pos = pos.offset(facing.rotateYCCW());
			
			if(facing == EnumFacing.WEST && context.getHitZ() >= 0.5F || facing == EnumFacing.EAST && context.getHitZ() < 0.5F
					|| facing == EnumFacing.NORTH && context.getHitX() < 0.5F || facing == EnumFacing.SOUTH && context.getHitX() >= 0.5F)
				pos = pos.offset(facing.rotateYCCW());
			
			if (!itemstack.isEmpty())
			{
				if(!canPlaceAt(context, pos, facing))
					return EnumActionResult.FAIL;
				
				IBlockState state = getBlock().getDefaultState().with(AlchemiterBlock.FACING, facing);
				this.placeBlock(context, state);
				return EnumActionResult.SUCCESS;
			}
			return EnumActionResult.FAIL;
		}
	}
	

	public static boolean canPlaceAt(BlockItemUseContext context, BlockPos pos, EnumFacing facing)
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
		EntityPlayer player = context.getPlayer();
		if(!world.isRemote)
		{
			EnumFacing facing = context.getPlacementHorizontalFacing();
			
			pos = pos.offset(facing.rotateYCCW());
			
			if(facing == EnumFacing.WEST && context.getHitZ() >= 0.5F || facing == EnumFacing.EAST && context.getHitZ() < 0.5F
					|| facing == EnumFacing.NORTH && context.getHitX() < 0.5F || facing == EnumFacing.SOUTH && context.getHitX() >= 0.5F)
				pos = pos.offset(facing.rotateYCCW());
			
			world.setBlockState(pos.offset(facing,3).offset(facing.rotateY(),3).up(0), MinestuckBlocks.ALCHEMITER.TOTEM_CORNER.getDefaultState().with(AlchemiterBlock.FACING, facing));
			world.setBlockState(pos.offset(facing,3).offset(facing.rotateY(),3).up(1), MinestuckBlocks.ALCHEMITER.TOTEM_PAD.getDefaultState().with(AlchemiterBlock.FACING, facing));
			world.setBlockState(pos.offset(facing,3).offset(facing.rotateY(),3).up(2), MinestuckBlocks.ALCHEMITER.LOWER_ROD.getDefaultState().with(AlchemiterBlock.FACING, facing));
			world.setBlockState(pos.offset(facing,3).offset(facing.rotateY(),3).up(3), MinestuckBlocks.ALCHEMITER.UPPER_ROD.getDefaultState().with(AlchemiterBlock.FACING, facing));
			
			world.setBlockState(pos.offset(facing,0).offset(facing.rotateY(),0), MinestuckBlocks.ALCHEMITER.CORNER.getDefaultState().with(AlchemiterBlock.FACING, facing.getOpposite()));
			world.setBlockState(pos.offset(facing,0).offset(facing.rotateY(),1), MinestuckBlocks.ALCHEMITER.LEFT_SIDE.getDefaultState().with(AlchemiterBlock.FACING, facing.getOpposite()));
			world.setBlockState(pos.offset(facing,0).offset(facing.rotateY(),2), MinestuckBlocks.ALCHEMITER.RIGHT_SIDE.getDefaultState().with(AlchemiterBlock.FACING, facing.getOpposite()));
			world.setBlockState(pos.offset(facing,1).offset(facing.rotateY(),1), MinestuckBlocks.ALCHEMITER.CENTER.getDefaultState().with(AlchemiterBlock.FACING, facing.getOpposite()));
			world.setBlockState(pos.offset(facing,0).offset(facing.rotateY(),3), MinestuckBlocks.ALCHEMITER.CORNER.getDefaultState().with(AlchemiterBlock.FACING, facing.rotateY()));
			world.setBlockState(pos.offset(facing,1).offset(facing.rotateY(),2), MinestuckBlocks.ALCHEMITER.CENTER.getDefaultState().with(AlchemiterBlock.FACING, facing.rotateY()));
			world.setBlockState(pos.offset(facing,1).offset(facing.rotateY(),3), MinestuckBlocks.ALCHEMITER.LEFT_SIDE.getDefaultState().with(AlchemiterBlock.FACING, facing.rotateY()));
			world.setBlockState(pos.offset(facing,2).offset(facing.rotateY(),3), MinestuckBlocks.ALCHEMITER.RIGHT_SIDE.getDefaultState().with(AlchemiterBlock.FACING, facing.rotateY()));
			world.setBlockState(pos.offset(facing,1).offset(facing.rotateY(),0), MinestuckBlocks.ALCHEMITER.RIGHT_SIDE.getDefaultState().with(AlchemiterBlock.FACING, facing.rotateYCCW()));
			world.setBlockState(pos.offset(facing,2).offset(facing.rotateY(),0), MinestuckBlocks.ALCHEMITER.LEFT_SIDE.getDefaultState().with(AlchemiterBlock.FACING, facing.rotateYCCW()));
			world.setBlockState(pos.offset(facing,2).offset(facing.rotateY(),1), MinestuckBlocks.ALCHEMITER.CENTER.getDefaultState().with(AlchemiterBlock.FACING, facing.rotateYCCW()));
			world.setBlockState(pos.offset(facing,3).offset(facing.rotateY(),0), MinestuckBlocks.ALCHEMITER.CORNER.getDefaultState().with(AlchemiterBlock.FACING, facing.rotateYCCW()));
			world.setBlockState(pos.offset(facing,2).offset(facing.rotateY(),2), MinestuckBlocks.ALCHEMITER.CENTER.getDefaultState().with(AlchemiterBlock.FACING, facing));
			world.setBlockState(pos.offset(facing,3).offset(facing.rotateY(),1), MinestuckBlocks.ALCHEMITER.RIGHT_SIDE.getDefaultState().with(AlchemiterBlock.FACING, facing));
			world.setBlockState(pos.offset(facing,3).offset(facing.rotateY(),2), MinestuckBlocks.ALCHEMITER.LEFT_SIDE.getDefaultState().with(AlchemiterBlock.FACING, facing));
			
			if(player instanceof EntityPlayerMP)
				CriteriaTriggers.PLACED_BLOCK.trigger((EntityPlayerMP) player, pos, context.getItem());
		}
		return true;
	}
}