package com.mraof.minestuck.block;

import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.state.properties.Half;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;

public class BlockVeinCorner extends Block
{

	public static final EnumProperty<Half> HALF = BlockStateProperties.HALF;
	public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
	
	protected BlockVeinCorner(Properties properties)
    {
		super(properties);
		setDefaultState(stateContainer.getBaseState().with(FACING, EnumFacing.NORTH).with(HALF, Half.BOTTOM));
	}
	
	@Override
	public void dropBlockAsItemWithChance(IBlockState state, World worldIn, BlockPos pos, float chancePerItem, int fortune)
	{
		if(!worldIn.isRemote && !worldIn.restoringBlockSnapshots)
		{
			Material material = worldIn.getBlockState(pos.down()).getMaterial();
			
			if((material.blocksMovement() || material.isLiquid()) && worldIn.rand.nextFloat() <= chancePerItem)
			{
				worldIn.setBlockState(pos, MinestuckBlocks.blockBlood.getDefaultState());
			}
		}
	}
	
	@Override
	public IBlockState rotate(IBlockState state, IWorld world, BlockPos pos, Rotation direction)
	{
		return state.with(FACING, direction.rotate(state.get(FACING)));
	}
	
	@Override
	public IBlockState mirror(IBlockState state, Mirror mirrorIn)
	{
		return state.with(FACING, mirrorIn.mirror(state.get(FACING)));
	}
	
	@Nullable
	@Override
	public IBlockState getStateForPlacement(BlockItemUseContext context)
	{
		//IBlockState iblockstate = worldIn.getBlockState(pos.offset(facing.getOpposite()));
		Half half = context.getFace() == EnumFacing.UP ? Half.TOP : Half.BOTTOM;
		EnumFacing facing = context.getPlacementHorizontalFacing();
		
	   /* if (iblockstate.getBlock() == MinestuckBlocks.vein || iblockstate.getBlock() == MinestuckBlocks.veinCorner)
		{
			EnumFacing enumfacing = (EnumFacing)iblockstate.getValue(FACING);

			if (enumfacing == facing)
			{
				return this.getDefaultState().withProperty(FACING, facing.getOpposite());
			}
		}*/

		return this.getDefaultState().with(FACING, facing).with(HALF, half);
	}
	
	@Override
	protected void fillStateContainer(StateContainer.Builder<Block, IBlockState> builder)
	{
		builder.add(FACING, HALF);
	}
}