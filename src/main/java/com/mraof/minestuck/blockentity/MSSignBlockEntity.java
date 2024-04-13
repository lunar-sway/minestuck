package com.mraof.minestuck.blockentity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.SignBlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class MSSignBlockEntity extends SignBlockEntity
{
	public MSSignBlockEntity(BlockPos pPos, BlockState pBlockState)
	{
		super(MSBlockEntityTypes.SIGN.get(), pPos, pBlockState);
	}
	
	@Override
	public BlockEntityType<?> getType()
	{
		return MSBlockEntityTypes.SIGN.get();
	}
}
