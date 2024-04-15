package com.mraof.minestuck.world.gen;

import com.mojang.serialization.Codec;
import com.mraof.minestuck.Minestuck;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.neoforged.neoforge.registries.DeferredRegister;

public class MSWorldGenTypes
{
	public static final DeferredRegister<Codec<? extends ChunkGenerator>> REGISTER = DeferredRegister.create(Registries.CHUNK_GENERATOR, Minestuck.MOD_ID);
	
	static {
		REGISTER.register("land", () -> LandChunkGenerator.CODEC);
	}
}