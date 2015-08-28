package com.mraof.minestuck.world.lands.structure;

import com.mraof.minestuck.world.lands.gen.ChunkProviderLands;

import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class GateStructurePillar implements IGateStructure
{

	@Override
	public BlockPos generateGateStructure(World world, BlockPos pos, ChunkProviderLands provider)
	{
		pos = world.getTopSolidOrLiquidBlock(pos);
		return null;
	}
}