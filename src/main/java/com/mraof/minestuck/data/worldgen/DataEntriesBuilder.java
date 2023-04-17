package com.mraof.minestuck.data.worldgen;

import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

public final class DataEntriesBuilder<T>
{
	private final Map<ResourceLocation, T> map = new HashMap<>();
	
	public void add(ResourceLocation location, T entry)
	{
		if(map.containsKey(location))
			throw new IllegalArgumentException("Name \"" + location + "\" is already used");
		else
			map.put(location, entry);
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
