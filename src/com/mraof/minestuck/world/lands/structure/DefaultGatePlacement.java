package com.mraof.minestuck.world.lands.structure;

import com.mraof.minestuck.world.lands.gen.ChunkProviderLands;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.Heightmap;

/**
 * Simply places the gate five blocks above ground, without any sort of structure
 */
public class DefaultGatePlacement implements IGateStructure
{
	
	@Override
	public BlockPos generateGateStructure(World world, BlockPos pos, ChunkProviderLands provider)
	{
		pos = world.getHeight(Heightmap.Type.WORLD_SURFACE_WG, pos).up(5);
		return pos;
	}

}
