package com.mraof.minestuck.particle;

import com.mraof.minestuck.Minestuck;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class MSParticleTypes
{
	public static final DeferredRegister<ParticleType<?>> REGISTER =
		DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES, Minestuck.MOD_ID);
	
	public static final RegistryObject<SimpleParticleType> LOW_ROLL =
			REGISTER.register("low_roll", () -> new SimpleParticleType(true));
	
	public static final RegistryObject<SimpleParticleType> CAEGER =
			REGISTER.register("caeger", () -> new SimpleParticleType(true));
	
	public static final RegistryObject<SimpleParticleType> MID_ROLL =
			REGISTER.register("mid_roll", () -> new SimpleParticleType(true));
	
	public static final RegistryObject<SimpleParticleType> HIGH_ROLL =
			REGISTER.register("high_roll", () -> new SimpleParticleType(true));
	
	public static final RegistryObject<SimpleParticleType> CRIT_13 =
			REGISTER.register("crit_13", () -> new SimpleParticleType(true));
	
	public static final RegistryObject<SimpleParticleType> SLASH =
			REGISTER.register("slash", () -> new SimpleParticleType(true));
	
	public static final RegistryObject<SimpleParticleType> CAEGER_SCRATCH =
			REGISTER.register("caeger_scratch", () -> new SimpleParticleType(true));

	public static void register(IEventBus eventBus)
	{
		REGISTER.register(eventBus);
	}
}
