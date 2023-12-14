package com.mraof.minestuck.computer;

import com.mraof.minestuck.blockentity.ComputerBlockEntity;
import com.mraof.minestuck.computer.editmode.EditmodeLocations;
import com.mraof.minestuck.skaianet.SburbConnection;
import com.mraof.minestuck.skaianet.SkaianetHandler;
import net.minecraft.core.GlobalPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Objects;
import java.util.OptionalInt;
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
		if(disks.containsKey(id) || id == -1)
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
	 * Returns the id of the program corresponding to the given item.
	 */
	public static OptionalInt getProgramID(ItemStack item)
	{
		if(item.isEmpty())
			return OptionalInt.empty();
		item = item.copy();
		item.setCount(1);
		for(int id : disks.keySet())
			if(ItemStack.isSameItem(disks.get(id), item))
				return OptionalInt.of(id);
		return OptionalInt.empty();
	}
	
	@Nonnull
	public static ItemStack getItem(int id)
	{
		if(id == 2 || id==3)
			return ItemStack.EMPTY;
		return disks.get(id).copy();
	}
	
	public static void onClientClosed(ComputerBlockEntity be)
	{
		if(be.getLevel() != null)
		{
			Level level = be.getLevel();
			SkaianetHandler handler = SkaianetHandler.get(level);
			
			SburbConnection c = handler.getClientConnection(be);
			if(c != null)
			{
				EditmodeLocations.removeBlockSource(((ServerLevel) level).getServer(), c.getClientIdentifier(), GlobalPos.of(level.dimension(), be.getBlockPos()));
			}
			
			handler.closeClientConnection(be);	//Can safely be done even if this computer isn't in a connection
		}
	}
	
	public static void onServerClosed(ComputerBlockEntity be)
	{
		Objects.requireNonNull(be.getLevel());
		SkaianetHandler.get(be.getLevel()).closeServerConnection(be);
	}
}