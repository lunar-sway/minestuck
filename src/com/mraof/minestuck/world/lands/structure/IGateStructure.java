package com.mraof.minestuck.world.lands.structure;

import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public interface IGateStructure
{
	/**
	 * Generates a gate structure at the given coordinates, and then returns the coordinates for where the gate should be placed.
	 * @param world The world to generate in.
	 * @param pos The position to generate at. Note that you should only use x and z from this position.
	 * @return The position that the gate should be placed at.
	 */
	public BlockPos generateGateStructure(World world, BlockPos pos);
}
