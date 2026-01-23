package com.mraof.minestuck.computer;

import com.mraof.minestuck.blockentity.ComputerBlockEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;

import java.util.function.Function;

public final class ProgramType<D extends ProgramType.Data>
{
	private final EventHandler eventHandler;
	private final Function<Runnable, D> dataConstructor;
	
	public ProgramType(EventHandler eventHandler, Function<Runnable, D> dataConstructor)
	{
		this.eventHandler = eventHandler;
		this.dataConstructor = dataConstructor;
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
	
	public MutableComponent name()
	{
		ResourceLocation key = ProgramTypes.REGISTRY.getKey(this);
		return key != null ? Component.translatable(key.getNamespace() + ".program." + key.getPath()) : Component.literal("Unknown Program");
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
