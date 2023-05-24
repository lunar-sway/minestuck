package com.mraof.minestuck.block.machine;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

/**
 * An interface for blocks that have special behavior when destroyed by an editmode player.
 * For example multiblock machines may be destroyed in their entirety.
 */
public interface EditmodeDestroyable
{
	void destroyFull(BlockState state, Level level, BlockPos pos);
}
