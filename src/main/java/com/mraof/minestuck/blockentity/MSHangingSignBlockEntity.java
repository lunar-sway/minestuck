package com.mraof.minestuck.blockentity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.HangingSignBlockEntity;
import net.minecraft.world.level.block.state.BlockState;

/**
 * A version of {@link HangingSignBlockEntity} for minestuck signs.
 * To add new sign blocks, they need to use a new block entity type, which is why this class exists.
 */
public class MSHangingSignBlockEntity extends HangingSignBlockEntity
{
	public MSHangingSignBlockEntity(BlockPos blockPos, BlockState blockState)
	{
		super(blockPos, blockState);
	}
	
	@Override
	public BlockEntityType<?> getType()
	{
		return MSBlockEntityTypes.HANGING_SIGN.get();
	}
}

