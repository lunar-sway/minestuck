package com.mraof.minestuck.computer;

import com.mraof.minestuck.skaianet.SkaianetHandler;
import com.mraof.minestuck.tileentity.ComputerTileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

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
	private static final HashMap<Integer, Consumer<ComputerTileEntity>> closeFunctions = new HashMap<>();
	
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
	public static void registerProgram(int id, ItemStack disk, Consumer<ComputerTileEntity> closeFunction)
	{
		if(disks.containsKey(id) || id == -1 || id == -2)
			throw new IllegalArgumentException("Program id " + id + " is already used!");
		closeFunctions.put(id, closeFunction);
		disks.put(id, disk);
	}
	
	public static void closeProgram(int id, ComputerTileEntity computer)
	{
		Consumer<ComputerTileEntity> closeFunction = closeFunctions.get(id);
		if(closeFunction != null)
			closeFunction.accept(computer);
	}
	
	/**
	 * Returns the id of the program. Note that it returns -2 if the item does
	 * not correspond to any program, as -1 is used for an easter egg.
	 */
	public static int getProgramID(ItemStack item)
	{
		if(item.isEmpty())
			return -2;
		item = item.copy();
		if(item.getItem().equals(Items.MUSIC_DISC_11))
			return -1;
		item.setCount(1);
		for(int id : disks.keySet())
			if(disks.get(id).isItemEqual(item))
				return id;
		return -2;
	}
	
	@Nonnull
	public static ItemStack getItem(int id)
	{
		if(id == -1)
			return new ItemStack(Items.MUSIC_DISC_11);
		return disks.get(id).copy();
	}
	
	public static void onClientClosed(ComputerTileEntity te)
	{
		Objects.requireNonNull(te.getWorld());
		SkaianetHandler.get(te.getWorld()).closeClientConnection(te);	//Can safely be done even if this computer isn't in a connection
	}
	
	public static void onServerClosed(ComputerTileEntity te)
	{
		Objects.requireNonNull(te.getWorld());
		SkaianetHandler.get(te.getWorld()).closeServerConnection(te);
	}
}