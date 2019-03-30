package com.mraof.minestuck.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockDirectional;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.StateContainer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReaderBase;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class BlockVein extends BlockDirectional
{

	protected BlockVein(Properties properties)
	{
		super(properties);
		setDefaultState(stateContainer.getBaseState().with(FACING, EnumFacing.NORTH));
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

	   /* if (iblockstate.getBlock() == MinestuckBlocks.vein || iblockstate.getBlock() == MinestuckBlocks.veinCorner)
		{
			EnumFacing enumfacing = (EnumFacing)iblockstate.getValue(FACING);

			if (enumfacing == facing)
			{
				return this.getDefaultState().withProperty(FACING, facing.getOpposite());
			}
		}*/

		return this.getDefaultState().with(FACING, context.getFace());
	}
	
	@Override
	protected void fillStateContainer(StateContainer.Builder<Block, IBlockState> builder)
	{
		builder.add(FACING);
	}
}