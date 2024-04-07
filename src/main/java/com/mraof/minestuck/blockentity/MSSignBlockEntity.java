package com.mraof.minestuck.blockentity;

import com.mraof.minestuck.blockentity.MSBlockEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.SignBlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class MSSignBlockEntity extends SignBlockEntity
{
	public MSSignBlockEntity(BlockPos pPos, BlockState pBlockState)
	{
		super(MSBlockEntityTypes.MOD_SIGN.get(), pPos, pBlockState);
	}
	
	@Override
	public BlockEntityType<?> getType()
	{
		return MSBlockEntityTypes.MOD_SIGN.get();
	}
}
