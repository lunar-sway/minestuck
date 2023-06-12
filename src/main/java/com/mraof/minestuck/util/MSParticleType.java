package com.mraof.minestuck.util;

import com.mraof.minestuck.Minestuck;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

/**
 * Registered Minestuck particles, check {@link com.mraof.minestuck.client.ClientProxy} for particle provider
 */
public class MSParticleType
{
	public static final DeferredRegister<ParticleType<?>> REGISTER = DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES, Minestuck.MOD_ID);
	
	public static final RegistryObject<SimpleParticleType> TRANSPORTALIZER = REGISTER.register("transportalizer", () -> new SimpleParticleType(false));
	public static final RegistryObject<SimpleParticleType> PLASMA = REGISTER.register("plasma", () -> new SimpleParticleType(false));
}
