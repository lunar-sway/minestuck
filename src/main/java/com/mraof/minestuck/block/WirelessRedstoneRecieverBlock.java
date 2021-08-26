package com.mraof.minestuck.block;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.mraof.minestuck.util.Debug;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Set;

public class WirelessRedstoneRecieverBlock extends Block
{
	public static final IntegerProperty POWER = BlockStateProperties.POWER_0_15;
	//private final Set<BlockPos> blocksNeedingUpdate = Sets.newHashSet();
	
	public WirelessRedstoneRecieverBlock(Properties properties)
	{
		super(properties);
		setDefaultState(getDefaultState().with(POWER, 0));
	}
	
	/*@Override
	public void updateNeighbors(BlockState stateIn, IWorld worldIn, BlockPos pos, int flags)
	{
		Debug.debugf("reciever updateNeighbors");
		super.updateNeighbors(stateIn, worldIn, pos, flags);
		
		if(this.isValidPosition(stateIn, worldIn, pos))
		{
			this.updateSurroundingBlocks(worldIn.getWorld(), pos, stateIn);
		}
		
		for(Direction direction : Direction.values())
		{
			worldIn.setBlockState(pos.offset(direction), worldIn.getBlockState(pos.offset(direction)), Constants.BlockFlags.BLOCK_UPDATE);
		}
		
		*//*
		for(Direction direction : Direction.values()) {
					worldIn.notifyNeighborsOfStateChange(pos.offset(direction), this);
				}
		 *//*
	}*/
	
	/*public BlockState updateSurroundingBlocks(World worldIn, BlockPos pos, BlockState state)
	{
		Debug.debugf("reciever updateSurroundingBlocks");
		List<BlockPos> list = Lists.newArrayList(this.blocksNeedingUpdate);
		this.blocksNeedingUpdate.clear();
		
		for(BlockPos blockpos : list)
		{
			worldIn.notifyNeighborsOfStateChange(blockpos, this);
		}
		
		return state;
	}*/
	
	@Override
	public int getWeakPower(BlockState blockState, IBlockReader blockAccess, BlockPos pos, Direction side)
	{
		return blockState.get(POWER);
	}
	
	@Override
	public boolean canConnectRedstone(BlockState state, IBlockReader world, BlockPos pos, @Nullable Direction side)
	{
		return true;
	}
	
	@Override
	public boolean canProvidePower(BlockState state)
	{
		return true;
	}
	
	/*@Override
	public int getComparatorInputOverride(BlockState blockState, World worldIn, BlockPos pos)
	{
		return blockState.get(POWER);
	}
	
	@Override
	public boolean hasComparatorInputOverride(BlockState state)
	{
		return true;
	}*/
	
	@Override
	protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder)
	{
		super.fillStateContainer(builder);
		builder.add(POWER);
	}
}
