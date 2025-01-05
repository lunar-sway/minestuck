package com.mraof.minestuck.computer;

import com.mojang.serialization.Codec;
import com.mraof.minestuck.blockentity.ComputerBlockEntity;
import net.minecraft.core.Holder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.function.Function;

public final class ProgramType<D extends ProgramType.Data>
{
	public static final Codec<ProgramType<?>> CODEC = ProgramTypes.REGISTRY.byNameCodec();
	
	/**
	 * Returns the type of the program corresponding to the given item.
	 */
	public static Optional<ProgramType<?>> getForDisk(ItemStack stack)
	{
		if(stack.isEmpty())
			return Optional.empty();
		for(ProgramType<?> type : ProgramTypes.REGISTRY)
			if(type.diskItem != null && stack.is(type.diskItem))
				return Optional.of(type);
		return Optional.empty();
	}
	
	@Nullable
	private final Holder<Item> diskItem;
	private final EventHandler eventHandler;
	private final Function<Runnable, D> dataConstructor;
	
	public ProgramType(@Nullable Holder<Item> diskItem, EventHandler eventHandler, Function<Runnable, D> dataConstructor)
	{
		this.diskItem = diskItem;
		this.eventHandler = eventHandler;
		this.dataConstructor = dataConstructor;
	}
	
	public Optional<Item> diskItem()
	{
		return Optional.ofNullable(this.diskItem).map(Holder::value);
	}
	
	public EventHandler eventHandler()
	{
		return this.eventHandler;
	}
	
	public D newDataInstance(Runnable markDirty)
	{
		return this.dataConstructor.apply(markDirty);
	}
	
	@Override
	public String toString()
	{
		ResourceLocation key = ProgramTypes.REGISTRY.getKey(this);
		return key != null ? key.toString() : "unknown";
	}
	
	public interface Data
	{
		void read(CompoundTag tag);
		
		CompoundTag write();
		
		default CompoundTag writeForSync(ISburbComputer computer, MinecraftServer mcServer)
		{
			return write();
		}
	}
	
	public enum EmptyData implements Data
	{
		INSTANCE;
		
		@Override
		public void read(CompoundTag tag)
		{
		}
		
		@Override
		public CompoundTag write()
		{
			return new CompoundTag();
		}
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
}
