package com.mraof.minestuck.data.worldgen;

import com.mojang.serialization.Lifecycle;
import net.minecraft.core.WritableRegistry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

public final class DataEntriesBuilder<T>
{
	@Nullable
	private final WritableRegistry<T> registry;
	private final Map<ResourceLocation, T> map = new HashMap<>();
	
	public DataEntriesBuilder()
	{
		this.registry = null;
	}
	
	public DataEntriesBuilder(@Nonnull WritableRegistry<T> registry)
	{
		this.registry = registry;
	}
	
	public void add(ResourceLocation location, T entry)
	{
		if(map.containsKey(location))
			throw new IllegalArgumentException("Name \"" + location + "\" is already used");
		else
			map.put(location, entry);
		if(registry != null)
			registry.register(ResourceKey.create(registry.key(), location), entry, Lifecycle.stable());
	}
	
	public void add(ResourceKey<T> key, T entry)
	{
		add(key.location(), entry);
	}
	
	public BiConsumer<String, T> consumerForNamespace(String namespace)
	{
		return (name, entry) -> add(new ResourceLocation(namespace, name), entry);
	}
	
	public Map<ResourceLocation, T> getMap()
	{
		return map;
	}
}
