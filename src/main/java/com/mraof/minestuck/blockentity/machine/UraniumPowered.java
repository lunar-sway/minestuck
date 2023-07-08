package com.mraof.minestuck.blockentity.machine;

/**
 * Used to keep track of block entities that store uranium fuel and apply external sources of fuel.
 */
public interface UraniumPowered
{
	void addFuel(short fuelAmount);
	
	boolean atMaxFuel();
}