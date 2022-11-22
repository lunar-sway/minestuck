package com.mraof.minestuck.block.machine;

import com.mraof.minestuck.block.MSBlockShapes;
import com.mraof.minestuck.blockentity.MSBlockEntityTypes;
import com.mraof.minestuck.blockentity.machine.MiniAlchemiterBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;

public class MiniAlchemiterBlock extends SmallMachineBlock<MiniAlchemiterBlockEntity>
{
	public MiniAlchemiterBlock(Properties properties)
	{
		super(MSBlockShapes.SMALL_ALCHEMITER.createRotatedShapes(), MSBlockEntityTypes.MINI_ALCHEMITER, properties);
	}
	
	@Override
	@SuppressWarnings("deprecation")
	public boolean hasAnalogOutputSignal(BlockState state)
	{
		return true;
	}
	
	// Will provide a redstone signal through a comparator with the output level corresponding to how many items can be alchemized with the player's current grist cache.
	// If no item can be alchemized, it will provide no signal to the comparator.
	@Override
	@SuppressWarnings("deprecation")
	public int getAnalogOutputSignal(BlockState blockState, Level level, BlockPos pos)
	{
		if(level.getBlockEntity(pos) instanceof MiniAlchemiterBlockEntity alchemiter)
			return alchemiter.comparatorValue();
		return 0;
	}
	
	@Override
	public boolean canConnectRedstone(BlockState state, BlockGetter level, BlockPos pos, @Nullable Direction side)
	{
		return side != null;
	}
}