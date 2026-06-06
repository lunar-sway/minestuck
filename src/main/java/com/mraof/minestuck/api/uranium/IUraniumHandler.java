package com.mraof.minestuck.api.uranium;

import net.neoforged.neoforge.energy.IEnergyStorage;

/**
 * Works similarly to {@link IEnergyStorage} but with uranium
 */
public interface IUraniumHandler
{
	/**
	 * Adds power to the storage. Returns the amount of power that was accepted.
	 *
	 * @param toReceive The amount of power being received.
	 * @param simulate  If true, the insertion will only be simulated, meaning {@link #getUraniumStored()} will not change.
	 * @return Amount of power that was (or would have been, if simulated) accepted by the storage.
	 */
	int receiveUranium(int toReceive, boolean simulate);
	
	/**
	 * Removes power from the storage. Returns the amount of power that was removed.
	 *
	 * @param toExtract The amount of power being extracted.
	 * @param simulate  If true, the extraction will only be simulated, meaning {@link #getUraniumStored()} will not change.
	 * @return Amount of power that was (or would have been, if simulated) extracted from the storage.
	 */
	int extractUranium(int toExtract, boolean simulate);
	
	/**
	 * Returns the amount of power currently stored.
	 */
	int getUraniumStored();
	
	/**
	 * Returns the maximum amount of power that can be stored.
	 */
	int getMaxUraniumStored();
	
	/**
	 * Returns if this storage can have power extracted.
	 * If this is false, then any calls to extractUranium will return 0.
	 */
	boolean canExtractUranium();
	
	/**
	 * Used to determine if this storage can receive power.
	 * If this is false, then any calls to receiveUranium will return 0.
	 */
	boolean canReceiveUranium();
}
