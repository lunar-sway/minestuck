package com.mraof.minestuck.entry;

import com.mraof.minestuck.skaianet.SkaianetHandler;
import com.mraof.minestuck.tileentity.ComputerTileEntity;
import net.minecraft.block.BlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;

import javax.annotation.Nullable;

public class ComputerBlockProcess implements EntryBlockProcessing
{
	@Override
	public void copyOver(ServerWorld oldWorld, BlockPos oldPos, ServerWorld newWorld, BlockPos newPos, BlockState state, @Nullable TileEntity oldTE, @Nullable TileEntity newTE)
	{
		if(oldTE instanceof ComputerTileEntity && newTE instanceof ComputerTileEntity)
			SkaianetHandler.get(oldWorld).movingComputer((ComputerTileEntity) oldTE, (ComputerTileEntity) newTE);
	}
}