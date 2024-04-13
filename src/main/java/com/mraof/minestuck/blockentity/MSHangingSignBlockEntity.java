package com.mraof.minestuck.blockentity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.HangingSignBlockEntity;
import net.minecraft.world.level.block.entity.SignBlockEntity;
import net.minecraft.world.level.block.state.BlockState;

/**
 * A version of {@link HangingSignBlockEntity} for minestuck signs.
 * To add new sign blocks, they need to use a new block entity type, which is why this class exists.
 */
//todo add gui textures and extend HangingSignBlockEntity to use them
public class MSHangingSignBlockEntity extends SignBlockEntity
{
	public MSHangingSignBlockEntity(BlockPos blockPos, BlockState blockState)
	{
		super(MSBlockEntityTypes.HANGING_SIGN.get(), blockPos, blockState);
	}
	
	@Override
	public BlockEntityType<?> getType()
	{
		return MSBlockEntityTypes.HANGING_SIGN.get();
	}
	
	@Override
	public int getTextLineHeight()
	{
		return 9;
	}
	
	@Override
	public int getMaxTextLineWidth()
	{
		return 60;
	}
}

