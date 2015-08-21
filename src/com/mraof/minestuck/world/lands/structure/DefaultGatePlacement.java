package com.mraof.minestuck.world.lands.structure;

import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

/**
 * Simply places the gate five blocks above ground, without any sort of structure
 */
public class DefaultGatePlacement implements IGateStructure
{
	
	@Override
	public BlockPos generateGateStructure(World world, BlockPos pos)
	{
		pos = world.getTopSolidOrLiquidBlock(pos).up(5);
		return pos;
	}

}
