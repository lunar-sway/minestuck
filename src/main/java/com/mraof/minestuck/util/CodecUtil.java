package com.mraof.minestuck.util;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;

import java.util.function.Supplier;

public class CodecUtil
{
	public static <T extends IForgeRegistryEntry<T>> Codec<T> registryCodec(Supplier<IForgeRegistry<T>> registry)
	{
		return ResourceLocation.CODEC.comapFlatMap(name -> {
			if(registry.get().containsKey(name))
				return DataResult.success(registry.get().getValue(name));
			else return DataResult.error("Unknown registry key: " + name);
		}, IForgeRegistryEntry::getRegistryName);
	}
}
