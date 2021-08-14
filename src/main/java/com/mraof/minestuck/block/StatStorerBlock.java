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
		worldIn.notifyNeighbors(pos.down(), stateIn.getBlock());
	}
	
	/*@Override
	public boolean canProvidePower(BlockState state)
	{
		return true;
	}
	
	@Override
	public boolean canConnectRedstone(BlockState state, IBlockReader world, BlockPos pos, @Nullable Direction side)
	{
		return true;
	}
	
	@Override
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