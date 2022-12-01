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
	
	public static final RegistryObject<SimpleParticleType> LowRollParticle =
			REGISTER.register("LOW_ROLL", () -> new SimpleParticleType(true));
	
	public static final RegistryObject<SimpleParticleType> CaegerParticle =
			REGISTER.register("CAEGER", () -> new SimpleParticleType(true));
	
	public static final RegistryObject<SimpleParticleType> MidRollParticle =
			REGISTER.register("MID_ROLL", () -> new SimpleParticleType(true));
	
	public static final RegistryObject<SimpleParticleType> HighRollParticle =
			REGISTER.register("HIGH_ROLL", () -> new SimpleParticleType(true));
	
	public static final RegistryObject<SimpleParticleType> Crit13Particle =
			REGISTER.register("CRIT_13", () -> new SimpleParticleType(true));
	
	public static final RegistryObject<SimpleParticleType> SlashParticle =
			REGISTER.register("SLASH", () -> new SimpleParticleType(true));
	
	public static final RegistryObject<SimpleParticleType> CaegerScratchParticle =
			REGISTER.register("CAEGER_SCRATCH", () -> new SimpleParticleType(true));

	public static void register(IEventBus eventBus)
	{
		REGISTER.register(eventBus);
	}
}
