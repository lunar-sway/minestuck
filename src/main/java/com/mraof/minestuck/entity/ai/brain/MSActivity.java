package com.mraof.minestuck.entity.ai.brain;

import com.mraof.minestuck.Minestuck;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.ai.sensing.SensorType;
import net.minecraft.world.entity.schedule.Activity;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class MSActivity
{
	public static final DeferredRegister<Activity> REGISTER = DeferredRegister.create(BuiltInRegistries.ACTIVITY, Minestuck.MOD_ID);
	
	public static final Supplier<Activity> FIND_MACHINE = REGISTER.register("find_machine", () -> new Activity("find_machine"));
}
