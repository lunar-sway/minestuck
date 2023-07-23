package com.mraof.minestuck.util;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.IForgeRegistry;

import java.util.function.Supplier;

public class CodecUtil
{
	public static <T> Codec<T> registryCodec(Supplier<IForgeRegistry<T>> registry)
	{
		return ResourceLocation.CODEC.comapFlatMap(name -> {
			if(registry.get().containsKey(name))
				return DataResult.success(registry.get().getValue(name));
			else return DataResult.error(() -> "Unknown registry key: " + name);
		}, t -> registry.get().getKey(t));
	}
}
