package com.mraof.minestuck.particle;

import com.mraof.minestuck.Minestuck;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class MSParticles
{
	public static final DeferredRegister<ParticleType<?>> PARTICLE_TYPES =
		DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES, Minestuck.MOD_ID);

	public static final RegistryObject<SimpleParticleType> LowRollParticle =
			PARTICLE_TYPES.register("low_roll_particle", () -> new SimpleParticleType(true));
	
	public static final RegistryObject<SimpleParticleType> CaegerParticle =
			PARTICLE_TYPES.register("CaegerParticle", () -> new SimpleParticleType(true));
	
	public static final RegistryObject<SimpleParticleType> MidRollParticle =
			PARTICLE_TYPES.register("mid_roll_particle", () -> new SimpleParticleType(true));
	
	public static final RegistryObject<SimpleParticleType> HighRollParticle =
			PARTICLE_TYPES.register("high_roll_particle", () -> new SimpleParticleType(true));
	
	public static final RegistryObject<SimpleParticleType> Crit13Particle =
			PARTICLE_TYPES.register("crit_13_particle", () -> new SimpleParticleType(true));
	
	public static final RegistryObject<SimpleParticleType> SlashParticle =
			PARTICLE_TYPES.register("slash_particle", () -> new SimpleParticleType(true));
	
	public static final RegistryObject<SimpleParticleType> CaegerScratchParticle =
			PARTICLE_TYPES.register("slash_particle", () -> new SimpleParticleType(true));

	public static void register(IEventBus eventBus)
	{
		PARTICLE_TYPES.register(eventBus);
	}
}
