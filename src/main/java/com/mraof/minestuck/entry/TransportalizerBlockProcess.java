package com.mraof.minestuck.entry;

import com.mraof.minestuck.blockentity.TransportalizerBlockEntity;
import com.mraof.minestuck.world.storage.TransportalizerSavedData;
import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;

public class TransportalizerBlockProcess implements BlockCopier.CopyStep
{
	@Override
	public void copyOver(ServerLevel oldLevel, BlockPos oldPos, ServerLevel newLevel, BlockPos newPos, BlockState state, @Nullable BlockEntity oldBE, @Nullable BlockEntity newBE)
	{
		if(oldBE instanceof TransportalizerBlockEntity && newBE instanceof TransportalizerBlockEntity)
		{
			TransportalizerSavedData.get(oldLevel).replace(((TransportalizerBlockEntity) newBE).getId(), GlobalPos.of(oldLevel.dimension(), oldPos), GlobalPos.of(newLevel.dimension(), newPos));
			if(((TransportalizerBlockEntity) oldBE).isActive())
				((TransportalizerBlockEntity) newBE).tryReactivate();
		}
	}
}