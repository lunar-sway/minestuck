package com.mraof.minestuck.entry;

import com.mraof.minestuck.skaianet.SkaianetHandler;
import com.mraof.minestuck.tileentity.ComputerTileEntity;
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
		if(oldTE instanceof ComputerTileEntity && newTE instanceof ComputerTileEntity)
			SkaianetHandler.get(oldWorld).movingComputer((ComputerTileEntity) oldTE, (ComputerTileEntity) newTE);
	}
}