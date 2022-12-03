package com.mraof.minestuck.particle;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.particle.custom.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.ParticleFactoryRegisterEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

@Mod.EventBusSubscriber(modid = Minestuck.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
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
	
	@OnlyIn(Dist.CLIENT)
	@SubscribeEvent
	public static void registerFactories(ParticleFactoryRegisterEvent event) {
		ParticleEngine particles = Minecraft.getInstance().particleEngine;
		
		particles.register(MSParticleTypes.LOW_ROLL.get(), LowRollParticle.Provider::new);
		particles.register(MSParticleTypes.CAEGER.get(), CaegerParticle.Provider::new);
		particles.register(MSParticleTypes.MID_ROLL.get(), MidRollParticle.Provider::new);
		particles.register(MSParticleTypes.HIGH_ROLL.get(), HighRollParticle.Provider::new);
		particles.register(MSParticleTypes.CRIT_13.get(), Crit13Particle.Provider::new);
		particles.register(MSParticleTypes.SLASH.get(), SlashParticle.Provider::new);
		particles.register(MSParticleTypes.CAEGER_SCRATCH.get(), CaegerScratchParticle.Provider::new);
	}
}
