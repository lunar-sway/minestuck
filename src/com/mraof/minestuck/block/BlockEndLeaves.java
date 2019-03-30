package com.mraof.minestuck.block;

import java.util.Random;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Items;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReaderBase;
import net.minecraft.world.World;

public class BlockEndLeaves extends BlockMinestuckLeaves
{
	public static final int LEAF_SUSTAIN_DISTANCE = 5;
	
	public BlockEndLeaves(Properties properties)
	{
		super(properties, () -> MinestuckBlocks.END_SAPLING.asItem(), false, () -> Items.CHORUS_FRUIT);
	}
	
	@Override
	public boolean getTickRandomly(IBlockState state)
	{
		return state.get(DISTANCE) >= LEAF_SUSTAIN_DISTANCE && !state.get(PERSISTENT);
	}
	
	@Override
	public void randomTick(IBlockState state, World worldIn, BlockPos pos, Random random)
	{
		if(!state.get(PERSISTENT) && state.get(DISTANCE) >= LEAF_SUSTAIN_DISTANCE)
		{
			state.dropBlockAsItem(worldIn, pos, 0);
			worldIn.removeBlock(pos);
		}
	}
	
	@Override
	public IBlockState updatePostPlacement(IBlockState stateIn, EnumFacing facing, IBlockState facingState, IWorld worldIn, BlockPos currentPos, BlockPos facingPos)
	{
		int i = getDistance(facingState) + 1;
		if(i != 1 || stateIn.get(DISTANCE) != i)
			worldIn.getPendingBlockTicks().scheduleTick(currentPos, this, 1);
		
		return stateIn;
	}
	
	protected IBlockState updateDistance(IBlockState state, IWorld world, BlockPos pos) {
		int i = 7;
		
		try (BlockPos.PooledMutableBlockPos mutablePos = BlockPos.PooledMutableBlockPos.retain())
		{
			for(EnumFacing facing : EnumFacing.values())
			{
				mutablePos.setPos(pos).move(facing);
				int axisDecrease = facing.getAxis() == EnumFacing.Axis.X ? 2 : 1;
				i = Math.min(i, getDistance(world.getBlockState(mutablePos)) + axisDecrease);
				if(i == 1)
					break;
			}
		}
		
		return state.with(DISTANCE, i);
	}
	
	protected int getDistance(IBlockState neighbor)
	{
		if(neighbor.getBlock() == MinestuckBlocks.END_LOG)
		{
			return 0;
		} else
		{
			return neighbor.getBlock() == this ? neighbor.get(DISTANCE) : 7;
		}
	}
	
	@Override
	public IBlockState getStateForPlacement(BlockItemUseContext context)
	{
		return updateDistance(this.getDefaultState().with(PERSISTENT, true), context.getWorld(), context.getPos());
	}
	
	@Override
	public boolean canBeReplacedByLeaves(IBlockState state, IWorldReaderBase world, BlockPos pos)
	{
		return false;
	}
	
	@Override
	public int getFlammability(IBlockState state, IBlockReader world, BlockPos pos, EnumFacing face)
	{
		return 1;
	}
	
	@Override
	public int getFireSpreadSpeed(IBlockState state, IBlockReader world, BlockPos pos, EnumFacing face)
	{
		return 250;
	}
}