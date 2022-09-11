package com.mraof.minestuck.entry;

import com.mraof.minestuck.skaianet.SkaianetHandler;
import com.mraof.minestuck.blockentity.ComputerBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;

public class ComputerBlockProcess implements EntryBlockProcessing
{
	@Override
	public void copyOver(ServerLevel oldWorld, BlockPos oldPos, ServerLevel newWorld, BlockPos newPos, BlockState state, @Nullable BlockEntity oldTE, @Nullable BlockEntity newTE)
	{
		if(oldTE instanceof ComputerBlockEntity && newTE instanceof ComputerBlockEntity)
			SkaianetHandler.get(oldWorld).movingComputer((ComputerBlockEntity) oldTE, (ComputerBlockEntity) newTE);
	}
}