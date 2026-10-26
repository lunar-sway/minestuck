package com.mraof.minestuck.blockentity.machine;

import com.mraof.minestuck.api.uranium.UraniumCapabilities;

/**
 * Used to keep track of block entities that store uranium fuel and apply external sources of fuel.
 * 
 * Deprecated, use {@link UraniumCapabilities} instead
 */
@Deprecated
public interface UraniumPowered
{
	@Deprecated
	void addFuel(short fuelAmount);
	
	@Deprecated
	boolean atMaxFuel();
}