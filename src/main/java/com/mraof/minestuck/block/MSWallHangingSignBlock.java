package com.mraof.minestuck.block;

import com.mraof.minestuck.blockentity.MSHangingSignBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.WallHangingSignBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.WoodType;

/**
 * A version of {@link WallHangingSignBlock} for minestuck signs.
 * To add new sign blocks, they need to use a new block entity type, which is why this class exists.
 */

public class MSWallHangingSignBlock extends WallHangingSignBlock
{
	public MSWallHangingSignBlock(WoodType type, Properties properties)
	{
		super(type, properties);
	}
	
	@Override
	public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState)
	{
		return new MSHangingSignBlockEntity(pPos, pState);
	}
}
