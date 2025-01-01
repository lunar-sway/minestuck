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
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/**
 * A quickfix to some sidedness issues
 * Contains common and server program data, unlike {@link ComputerProgram} which should now ONLY contain client-sided stuff
 */
public final class ProgramData
{
	private static final Map<ProgramType, ProgramHandler> programHandlers = new HashMap<>();
	
	private static final Map<ProgramType, ItemStack> disks = new HashMap<>();
	
	public static void init()
	{
		ProgramData.registerProgram(ProgramType.CLIENT, new ItemStack(MSItems.CLIENT_DISK.get()), CLIENT_HANDLER);
		ProgramData.registerProgram(ProgramType.SERVER, new ItemStack(MSItems.SERVER_DISK.get()), SERVER_HANDLER);
	}
	
	/**
	 * Registers a program class to the list.
	 *
	 * @param programType
	 *            The program type. If it is already registered, the method will throw
	 *            an IllegalArgumentException.
	 * @param disk
	 *            The item that will serve as the disk that installs the
	 *            program.
	 */
	public static void registerProgram(ProgramType programType, ItemStack disk, ProgramHandler programHandler)
	{
		if(disks.containsKey(programType))
			throw new IllegalArgumentException("Program type " + programType.name() + " is already registered!");
		programHandlers.put(programType, programHandler);
		disks.put(programType, disk);
	}
	
	public static Optional<ProgramHandler> getHandler(ProgramType programType)
	{
		return Optional.ofNullable(programHandlers.get(programType));
	}
	
	/**
	 * Returns the type of the program corresponding to the given item.
	 */
	public static Optional<ProgramType> getProgramType(ItemStack item)
	{
		if(item.isEmpty())
			return Optional.empty();
		item = item.copy();
		item.setCount(1);
		for(Map.Entry<ProgramType, ItemStack> entry : disks.entrySet())
			if(ItemStack.isSameItem(entry.getValue(), item))
				return Optional.of(entry.getKey());
		return Optional.empty();
	}
	
	@Nonnull
	public static ItemStack getItem(ProgramType programType)
	{
		if(programType == ProgramType.DISK_BURNER || programType == ProgramType.SETTINGS)
			return ItemStack.EMPTY;
		return disks.get(programType).copy();
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
