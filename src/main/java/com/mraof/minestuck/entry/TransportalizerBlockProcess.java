package com.mraof.minestuck.entry;

import com.mraof.minestuck.tileentity.TransportalizerTileEntity;
import com.mraof.minestuck.world.storage.TransportalizerSavedData;
import net.minecraft.block.BlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.GlobalPos;
import net.minecraft.world.server.ServerWorld;

import javax.annotation.Nullable;

public class TransportalizerBlockProcess implements EntryBlockProcessing
{
	@Override
	public void copyOver(ServerWorld oldWorld, BlockPos oldPos, ServerWorld newWorld, BlockPos newPos, BlockState state, @Nullable TileEntity oldTE, @Nullable TileEntity newTE)
	{
		if(oldTE instanceof TransportalizerTileEntity && newTE instanceof TransportalizerTileEntity)
		{
			TransportalizerSavedData.get(oldWorld).replace(((TransportalizerTileEntity) newTE).getId(), GlobalPos.of(oldWorld.dimension.getType(), oldPos), GlobalPos.of(newWorld.dimension.getType(), newPos));
			if(((TransportalizerTileEntity) oldTE).isActive())
				((TransportalizerTileEntity) newTE).tryReactivate();
		}
	}
}