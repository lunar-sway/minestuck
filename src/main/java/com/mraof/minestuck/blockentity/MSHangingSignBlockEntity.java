package com.mraof.minestuck.blockentity;

import com.mraof.minestuck.blockentity.MSBlockEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.SignBlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class MSHangingSignBlockEntity extends SignBlockEntity
{
	public MSHangingSignBlockEntity(BlockPos blockPos, BlockState blockState)
	{
		super(MSBlockEntityTypes.MOD_HANGING_SIGN.get(), blockPos, blockState);
	}
	
	@Override
	public BlockEntityType<?> getType()
	{
		return MSBlockEntityTypes.MOD_HANGING_SIGN.get();
	}
}

