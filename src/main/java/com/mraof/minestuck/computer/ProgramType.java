package com.mraof.minestuck.computer;

import com.google.common.collect.BiMap;
import com.google.common.collect.ImmutableBiMap;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mraof.minestuck.blockentity.ComputerBlockEntity;
import com.mraof.minestuck.computer.editmode.EditmodeLocations;
import com.mraof.minestuck.item.MSItems;
import com.mraof.minestuck.skaianet.ComputerInteractions;
import net.minecraft.core.Holder;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nullable;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public final class ProgramType
{
	public static final ProgramType CLIENT = new ProgramType(MSItems.CLIENT_DISK, Handlers.CLIENT);
	public static final ProgramType SERVER = new ProgramType(MSItems.SERVER_DISK, Handlers.SERVER);
	public static final ProgramType DISK_BURNER = new ProgramType(null, Handlers.EMPTY);
	public static final ProgramType SETTINGS = new ProgramType(null, Handlers.EMPTY);
	
	private static final BiMap<String, ProgramType> REGISTRY = ImmutableBiMap.of("client", CLIENT, "server", SERVER, "disk_burner", DISK_BURNER, "settings", SETTINGS);
	
	public static final Codec<ProgramType> CODEC = Codec.STRING.flatXmap(
			name -> Optional.ofNullable(REGISTRY.get(name)).map(DataResult::success).orElse(DataResult.error(() -> "Unknown program name " + name)),
			type -> Optional.ofNullable(REGISTRY.inverse().get(type)).map(DataResult::success).orElse(DataResult.error(() -> "Unknown program type " + type)));
	public static final Codec<List<ProgramType>> LIST_CODEC = CODEC.listOf();
	
	private static final List<ProgramType> DISPLAY_ORDER = List.of(SETTINGS, DISK_BURNER, SERVER, CLIENT);
	public static final Comparator<ProgramType> DISPLAY_ORDER_SORTER = Comparator.comparing(type -> {
		int index = DISPLAY_ORDER.indexOf(type);
		if(index != -1)
			return index;
		else
			return DISPLAY_ORDER.size();
	});
	
	/**
	 * Returns the type of the program corresponding to the given item.
	 */
	public static Optional<ProgramType> getForDisk(ItemStack stack)
	{
		if(stack.isEmpty())
			return Optional.empty();
		for(ProgramType type : REGISTRY.values())
			if(type.diskItem != null && stack.is(type.diskItem))
				return Optional.of(type);
		return Optional.empty();
	}
	
	@Nullable
	private final Holder<Item> diskItem;
	private final EventHandler eventHandler;
	
	private ProgramType(@Nullable Holder<Item> diskItem, EventHandler eventHandler)
	{
		this.diskItem = diskItem;
		this.eventHandler = eventHandler;
	}
	
	public Optional<Item> diskItem()
	{
		return Optional.ofNullable(this.diskItem).map(Holder::value);
	}
	
	public EventHandler eventHandler()
	{
		return this.eventHandler;
	}
	
	@Override
	public String toString()
	{
		return Objects.requireNonNullElse(REGISTRY.inverse().get(this), "unknown");
	}
	
	public interface EventHandler
	{
		default void onDiskInserted(ComputerBlockEntity computer)
		{
		}
		
		default void onLoad(ComputerBlockEntity computer)
		{
		}
		
		default void onClosed(ComputerBlockEntity computer)
		{
		}
	}
	
	private static final class Handlers
	{
		static final EventHandler EMPTY = new EventHandler()
		{
		};
		
		static final EventHandler CLIENT = new EventHandler()
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
					ComputerInteractions.get(level.getServer()).closeClientConnection(computer);    //Can safely be done even if this computer isn't in a connection
					
					EditmodeLocations.removeBlockSource(level.getServer(), computer.getOwner(), level.dimension(), computer.getBlockPos());
				}
			}
		};
		
		static final EventHandler SERVER = new EventHandler()
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
}