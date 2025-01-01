package com.mraof.minestuck.computer;

import com.mojang.serialization.Codec;
import com.mraof.minestuck.blockentity.ComputerBlockEntity;
import com.mraof.minestuck.computer.editmode.EditmodeLocations;
import com.mraof.minestuck.item.MSItems;
import com.mraof.minestuck.skaianet.ComputerInteractions;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.Holder;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.ByIdMap;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nullable;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;

public enum ProgramType implements StringRepresentable
{
	CLIENT(MSItems.CLIENT_DISK, Handlers.CLIENT),
	SERVER(MSItems.SERVER_DISK, Handlers.SERVER),
	DISK_BURNER(null, Handlers.EMPTY),
	SETTINGS(null, Handlers.EMPTY);
	
	public static final Codec<ProgramType> CODEC = StringRepresentable.fromEnum(ProgramType::values);
	public static final StreamCodec<ByteBuf, ProgramType> STREAM_CODEC = ByteBufCodecs.idMapper(
			ByIdMap.continuous(ProgramType::ordinal, ProgramType.values(), ByIdMap.OutOfBoundsStrategy.ZERO), ProgramType::ordinal);
	
	/**
	 * Returns the type of the program corresponding to the given item.
	 */
	public static Optional<ProgramType> getForDisk(ItemStack stack)
	{
		if(stack.isEmpty())
			return Optional.empty();
		for(ProgramType type : ProgramType.values())
			if(type.diskItem != null && stack.is(type.diskItem))
				return Optional.of(type);
		return Optional.empty();
	}
	
	@Nullable
	private final Holder<Item> diskItem;
	private final EventHandler eventHandler;
	
	ProgramType(@Nullable Holder<Item> diskItem, EventHandler eventHandler)
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
	public String getSerializedName()
	{
		return this.name().toLowerCase(Locale.ROOT);
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
