package com.mraof.minestuck.entry;

import com.mraof.minestuck.blockentity.ComputerBlockEntity;
import com.mraof.minestuck.skaianet.ComputerInteractions;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;

public class ComputerBlockProcess implements BlockCopier.CopyStep
{
	@Override
	public void copyOver(ServerLevel oldWorld, BlockPos oldPos, ServerLevel newWorld, BlockPos newPos, BlockState state, @Nullable BlockEntity oldBE, @Nullable BlockEntity newBE)
	{
		if(oldBE instanceof ComputerBlockEntity oldComputer && newBE instanceof ComputerBlockEntity newComputer)
			ComputerInteractions.get(oldWorld.getServer()).movingComputer(oldComputer, newComputer);
	}
}