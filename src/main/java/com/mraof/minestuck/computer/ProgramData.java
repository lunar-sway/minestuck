package com.mraof.minestuck.computer;

import com.mraof.minestuck.blockentity.ComputerBlockEntity;
import com.mraof.minestuck.computer.editmode.EditmodeLocations;
import com.mraof.minestuck.item.MSItems;
import com.mraof.minestuck.skaianet.ComputerInteractions;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Objects;
import java.util.Optional;
import java.util.OptionalInt;

/**
 * A quickfix to some sidedness issues
 * Contains common and server program data, unlike {@link ComputerProgram} which should now ONLY contain client-sided stuff
 */
public final class ProgramData
{
	private static final HashMap<Integer, ProgramHandler> programHandlers = new HashMap<>();
	
	private static final HashMap<Integer, ItemStack> disks = new HashMap<>();
	
	public static void init()
	{
		ProgramData.registerProgram(0, new ItemStack(MSItems.CLIENT_DISK.get()), CLIENT_HANDLER);
		ProgramData.registerProgram(1, new ItemStack(MSItems.SERVER_DISK.get()), SERVER_HANDLER);
	}
	
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
	public static void registerProgram(int id, ItemStack disk, ProgramHandler programHandler)
	{
		if(disks.containsKey(id) || id == -1)
			throw new IllegalArgumentException("Program id " + id + " is already used!");
		programHandlers.put(id, programHandler);
		disks.put(id, disk);
	}
	
	public static Optional<ProgramHandler> getHandler(int id)
	{
		return Optional.ofNullable(programHandlers.get(id));
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
	
	public interface ProgramHandler
	{
		default void onDiskInserted(ComputerBlockEntity computer)
		{}
		
		default void onLoad(ComputerBlockEntity computer)
		{}
		
		default void onClosed(ComputerBlockEntity computer)
		{}
	}
	
	private static final ProgramHandler CLIENT_HANDLER = new ProgramHandler()
	{
		@Override
		public void onDiskInserted(ComputerBlockEntity computer)
		{
			EditmodeLocations.addBlockSourceIfValid(computer);
		}
		
		@Override
		public void onLoad(ComputerBlockEntity computer)
		{
			EditmodeLocations.addBlockSourceIfValid(computer);
		}
		
		@Override
		public void onClosed(ComputerBlockEntity computer)
		{
			if(computer.getLevel() instanceof ServerLevel level && computer.getOwner() != null)
			{
				ComputerInteractions.get(level.getServer()).closeClientConnection(computer);	//Can safely be done even if this computer isn't in a connection
				
				EditmodeLocations.removeBlockSource(level.getServer(), computer.getOwner(), level.dimension(), computer.getBlockPos());
			}
		}
	};
	
	private static final ProgramHandler SERVER_HANDLER = new ProgramHandler()
	{
		@Override
		public void onClosed(ComputerBlockEntity computer)
		{
			Objects.requireNonNull(computer.getLevel());
			MinecraftServer mcServer = Objects.requireNonNull(computer.getLevel().getServer());
			ComputerInteractions.get(mcServer).closeServerConnection(computer);
		}
	};
}