package com.mraof.minestuck.entity.ai.brain;

import com.mojang.serialization.Codec;
import com.mraof.minestuck.Minestuck;
import net.minecraft.core.GlobalPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.sensing.SensorType;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

public class MSSensorType
{
	public static final DeferredRegister<SensorType<?>> REGISTER = DeferredRegister.create(BuiltInRegistries.SENSOR_TYPE, Minestuck.MOD_ID);
	
	public static final Supplier<SensorType<MachineSensor>> MACHINE_SENSOR = REGISTER.register("machine_sensor", () -> new SensorType<>(MachineSensor::new));
}
