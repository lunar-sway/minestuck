package com.mraof.minestuck.util;

import com.mraof.minestuck.Minestuck;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

/**
 * Registered Minestuck particles, check {@link com.mraof.minestuck.client.ClientProxy} for particle provider
 */
public class MSParticleType
{
	public static final DeferredRegister<ParticleType<?>> REGISTER = DeferredRegister.create(BuiltInRegistries.PARTICLE_TYPE, Minestuck.MOD_ID);
	
	public static final Supplier<SimpleParticleType> TRANSPORTALIZER = REGISTER.register("transportalizer", () -> new SimpleParticleType(false));
	public static final Supplier<SimpleParticleType> PLASMA = REGISTER.register("plasma", () -> new SimpleParticleType(false));
	public static final Supplier<SimpleParticleType> EXHAUST = REGISTER.register("exhaust", () -> new SimpleParticleType(false));
}
