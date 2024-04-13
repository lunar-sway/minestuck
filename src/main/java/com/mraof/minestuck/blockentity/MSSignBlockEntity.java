package com.mraof.minestuck.blockentity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.SignBlockEntity;
import net.minecraft.world.level.block.state.BlockState;

/**
 * A version of {@link SignBlockEntity} for minestuck signs.
 * To add new sign blocks, they need to use a new block entity type, which is why this class exists.
 */
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
