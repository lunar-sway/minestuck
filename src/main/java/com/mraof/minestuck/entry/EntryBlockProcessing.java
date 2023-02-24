package com.mraof.minestuck.entry;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;

public interface EntryBlockProcessing
{
	void copyOver(ServerLevel oldWorld, BlockPos oldPos, ServerLevel newWorld, BlockPos newPos, BlockState state, @Nullable BlockEntity oldBE, @Nullable BlockEntity newBE);
}
