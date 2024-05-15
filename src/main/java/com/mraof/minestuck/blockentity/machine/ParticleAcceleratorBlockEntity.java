package com.mraof.minestuck.blockentity.machine;

import com.mraof.minestuck.blockentity.MSBlockEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.items.ItemStackHandler;


public class ParticleAcceleratorBlockEntity extends MachineProcessBlockEntity
{
	public ParticleAcceleratorBlockEntity(BlockPos pos, BlockState state)
	{
		super(MSBlockEntityTypes.PARTICLE_ACCELERATOR.get(), pos, state);
	}
	
	@Override
	protected ItemStackHandler createItemHandler()
	{
		return new CustomHandler(0, (n,item) -> false);
	}
	
	@Override
	protected void tick()
	{
	
	}
}
