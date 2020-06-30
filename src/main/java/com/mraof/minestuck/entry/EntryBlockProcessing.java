package com.mraof.minestuck.entry;

import net.minecraft.block.BlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;

import javax.annotation.Nullable;

public interface EntryBlockProcessing
{
	void copyOver(ServerWorld oldWorld, BlockPos oldPos, ServerWorld newWorld, BlockPos newPos, BlockState state, @Nullable TileEntity oldTE, @Nullable TileEntity newTE);
}
