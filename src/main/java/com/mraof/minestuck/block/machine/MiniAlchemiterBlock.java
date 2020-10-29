package com.mraof.minestuck.block.machine;

import com.mraof.minestuck.block.MSBlockShapes;
import com.mraof.minestuck.tileentity.MSTileEntityTypes;
import com.mraof.minestuck.tileentity.machine.MiniAlchemiterTileEntity;
import net.minecraft.block.BlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class MiniAlchemiterBlock extends SmallMachineBlock<MiniAlchemiterTileEntity>
{
	public MiniAlchemiterBlock(Properties properties)
	{
		super(MSBlockShapes.SMALL_ALCHEMITER.createRotatedShapes(), MSTileEntityTypes.MINI_ALCHEMITER, properties);
	}
	
	@Override
	@SuppressWarnings("deprecation")
	public boolean hasComparatorInputOverride(BlockState state)
	{
		return true;
	}
	
	// Will provide a redstone signal through a comparator with the output level corresponding to how many items can be alchemized with the player's current grist cache.
	// If no item can be alchemized, it will provide no signal to the comparator.
	@Override
	@SuppressWarnings("deprecation")
	public int getComparatorInputOverride(BlockState blockState, World worldIn, BlockPos pos)
	{
		TileEntity tileEntity = worldIn.getTileEntity(pos);
		if(tileEntity instanceof MiniAlchemiterTileEntity)
			return ((MiniAlchemiterTileEntity) tileEntity).comparatorValue();
		return 0;
	}
	
	@Override
	public boolean canConnectRedstone(BlockState state, IBlockReader world, BlockPos pos, @Nullable Direction side)
	{
		return side != null;
	}
}