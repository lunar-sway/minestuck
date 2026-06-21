package com.mraof.minestuck.entity.ai.brain;

import com.mojang.serialization.Codec;
import com.mraof.minestuck.Minestuck;
import net.minecraft.core.GlobalPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

public class MSMemoryModuleType
{
	public static final DeferredRegister<MemoryModuleType<?>> REGISTER = DeferredRegister.create(BuiltInRegistries.MEMORY_MODULE_TYPE, Minestuck.MOD_ID);
	
	public static final Supplier<MemoryModuleType<List<GlobalPos>>> MACHINE_LOCATIONS = REGISTER.register("machine_locations", () -> new MemoryModuleType<>(Optional.of(Codec.list(GlobalPos.CODEC))));
}
