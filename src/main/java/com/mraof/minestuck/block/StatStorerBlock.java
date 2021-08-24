package com.mraof.minestuck.block;

import com.mraof.minestuck.tileentity.StatStorerTileEntity;
import com.mraof.minestuck.util.Debug;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.tileentity.LecternTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class StatStorerBlock extends Block
{
	public StatStorerBlock(Properties properties)
	{
		super(properties);
	}
	
	@Override
	public void updateNeighbors(BlockState stateIn, IWorld worldIn, BlockPos pos, int flags)
	{
		Debug.debugf("updateNeighbors");
		super.updateNeighbors(stateIn, worldIn, pos, flags);
		
		for(int i = 0; i < 4; i++) //TODO I want this to update the neighbors for redstone, but I might need those neighbor blocks themselves to get updated
		{
			worldIn.notifyNeighbors(pos.offset(Direction.byHorizontalIndex(i)), stateIn.getBlock());
		}
		worldIn.notifyNeighbors(pos.down(), stateIn.getBlock());
	}
	
	@Override
	public int getComparatorInputOverride(BlockState blockState, World worldIn, BlockPos pos)
	{
		TileEntity tileentity = worldIn.getTileEntity(pos);
		if(tileentity instanceof StatStorerTileEntity)
		{
			return Math.min(16, (int) ((StatStorerTileEntity) tileentity).getStoredStatValue() / 2);
		}
		
		return super.getComparatorInputOverride(blockState, worldIn, pos);
	}
	
	@Override
	public boolean hasComparatorInputOverride(BlockState state)
	{
		return true;
	}
	
	@Override
	public boolean canProvidePower(BlockState state)
	{
		return true;
	}
	
	@Override
	public boolean canConnectRedstone(BlockState state, IBlockReader world, BlockPos pos, @Nullable Direction side)
	{
		return true;
	}
	
	
	
	/*@Override
	public int getComparatorInputOverride(BlockState blockState, World worldIn, BlockPos pos)
	{
		Debug.debugf("getComparatorInputOverride");
		TileEntity tileentity = worldIn.getTileEntity(pos);
		if(tileentity instanceof StatStorerTileEntity)
		{
			return (int) ((StatStorerTileEntity) tileentity).getStoredStatValue() / 15;
		}
		return super.getComparatorInputOverride(blockState, worldIn, pos);
	}*/
	
	@Override
	public boolean hasTileEntity(BlockState state)
	{
		return true;
	}
	
	@Nullable
	@Override
	public TileEntity createTileEntity(BlockState state, IBlockReader world)
	{
		return new StatStorerTileEntity();
	}
}