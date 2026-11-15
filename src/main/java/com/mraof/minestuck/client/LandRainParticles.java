package com.mraof.minestuck.client;

import com.mraof.minestuck.Minestuck;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.WaterDropParticle;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.SimpleParticleType;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterParticleProvidersEvent;

/**
 * Replaces the provider of the splashes of rain on the ground with one that gives them the color of the liquid of the land, see {@link LandRainTint}.
 */
@EventBusSubscriber(value = Dist.CLIENT, bus = EventBusSubscriber.Bus.MOD, modid = Minestuck.MOD_ID)
public final class LandRainParticles
{
	private LandRainParticles()
	{
	}
	
	@SubscribeEvent
	public static void registerProviders(RegisterParticleProvidersEvent event)
	{
		event.registerSpriteSet(ParticleTypes.RAIN, sprites -> {
			ParticleProvider<SimpleParticleType> vanilla = new WaterDropParticle.Provider(sprites);
			return (type, level, x, y, z, xSpeed, ySpeed, zSpeed) -> {
				Particle particle = vanilla.createParticle(type, level, x, y, z, xSpeed, ySpeed, zSpeed);
				if(particle != null)
					LandRainTint.tintSplash(level, particle);
				return particle;
			};
		});
	}
}
