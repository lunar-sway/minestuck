package com.mraof.minestuck.computer;

import com.mraof.minestuck.blockentity.ComputerBlockEntity;
import com.mraof.minestuck.skaianet.SkaianetHandler;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Objects;
import java.util.function.Consumer;

/**
 * A quickfix to some sidedness issues
 * Contains common and server program data, unlike {@link ComputerProgram} which should now ONLY contain client-sided stuff
 */
public class ProgramData
{
	private static final HashMap<Integer, Consumer<ComputerBlockEntity>> closeFunctions = new HashMap<>();
	
	private static final HashMap<Integer, ItemStack> disks = new HashMap<>();
	
	
	/**
	 * Registers a program class to the list.
	 *
	 * @param id
	 *            The program id. If it is already used, the method will throw
	 *            an IllegalArgumentException.
	 * @param disk
	 *            The item that will serve as the disk that installs the
	 *            program.
	 */
	public static void registerProgram(int id, ItemStack disk, Consumer<ComputerBlockEntity> closeFunction)
	{
		if(disks.containsKey(id) || id == -1 || id == -2)
			throw new IllegalArgumentException("Program id " + id + " is already used!");
		closeFunctions.put(id, closeFunction);
		disks.put(id, disk);
	}
	
	public static void closeProgram(int id, ComputerBlockEntity computer)
	{
		Consumer<ComputerBlockEntity> closeFunction = closeFunctions.get(id);
		if(closeFunction != null)
			closeFunction.accept(computer);
	}
	
	/**
	 * Returns the id of the program. Note that it returns -2 if the item does
	 * not correspond to any program.
	 */
	public static int getProgramID(ItemStack item)
	{
		if(item.isEmpty())
			return -2;
		item = item.copy();
		item.setCount(1);
		for(int id : disks.keySet())
			if(disks.get(id).sameItem(item))
				return id;
		return -2;
	}
	
	@Nonnull
	public static ItemStack getItem(int id)
	{
		if(id == 2)
			return ItemStack.EMPTY;
		return disks.get(id).copy();
	}
	
	public static void onClientClosed(ComputerBlockEntity be)
	{
		Objects.requireNonNull(be.getLevel());
		SkaianetHandler.get(be.getLevel()).closeClientConnection(be);	//Can safely be done even if this computer isn't in a connection
	}
	
	public static void onServerClosed(ComputerBlockEntity be)
	{
		Objects.requireNonNull(be.getLevel());
		SkaianetHandler.get(be.getLevel()).closeServerConnection(be);
	}
}