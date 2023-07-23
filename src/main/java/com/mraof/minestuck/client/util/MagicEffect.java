package com.mraof.minestuck.client.util;


import com.mraof.minestuck.util.MSSoundEvents;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;

import java.util.function.Supplier;

public class MagicEffect
{
	public enum RangedType
	{
		GREEN(() -> new DustParticleOptions(new Vector3f(0F, 1F, 0F), 2F), false, false),
		CRIT(() -> ParticleTypes.ENCHANTED_HIT, false, false),
		ENCHANT(() -> ParticleTypes.ENCHANT, false, true),
		RED(() -> new DustParticleOptions(new Vector3f(20F, 0F, 0F), 2F), false, false),
		INK(() -> ParticleTypes.SQUID_INK, true, false),
		ZILLY(() -> new DustParticleOptions(new Vector3f(20F, 20, 20F), 2F), true, false),
		ECHIDNA(() -> ParticleTypes.END_ROD, true, false),
		WATER(() -> ParticleTypes.DRIPPING_WATER, false, true),
		FIRE(() -> ParticleTypes.DRIPPING_LAVA, false, true);
		
		private final Supplier<ParticleOptions> particle;
		private final boolean explosiveFinish;
		private final boolean extraParticles;
		
		RangedType(Supplier<ParticleOptions> particle, boolean explosiveFinish, boolean extraParticles)
		{
			this.particle = particle;
			this.explosiveFinish = explosiveFinish;
			this.extraParticles = extraParticles;
		}
		
		public int toInt()
		{
			return ordinal();
		}
		
		public static RangedType fromInt(int i)
		{
			if(i >= 0 && i < values().length)
				return values()[i];
			else return ENCHANT;
		}
	}
	
	public enum AOEType
	{
		ENCHANT(() -> ParticleTypes.ENCHANT, true, false),
		WATER(() -> ParticleTypes.SPLASH, true, true),
		FIRE(() -> ParticleTypes.FLAME, true, true);
		
		private final Supplier<ParticleOptions> particle;
		private final boolean perimeterParticles;
		private final boolean extraParticles;
		
		AOEType(Supplier<ParticleOptions> particle, boolean perimeterParticles, boolean extraParticles)
		{
			this.particle = particle;
			this.perimeterParticles = perimeterParticles;
			this.extraParticles = extraParticles;
		}
		
		public int toInt()
		{
			return ordinal();
		}
		
		public static AOEType fromInt(int i)
		{
			if(i >= 0 && i < values().length)
				return values()[i];
			else return ENCHANT;
		}
	}
	
	public static void rangedParticleEffect(RangedType type, ClientLevel level, Vec3 pos, Vec3 lookVector, int length, boolean collides)
	{
		rangedParticleEffect(type.particle.get(), type.explosiveFinish, type.extraParticles, level, pos, lookVector, length, collides);
	}
	
	public static void rangedParticleEffect(ParticleOptions particle, boolean explosiveFinish, boolean extraParticles, ClientLevel level, Vec3 pos, Vec3 lookVector, int length, boolean collides)
	{
		for(int step = 0; step <= length; step++)
		{
			rangedPathParticles(particle, extraParticles, level, pos.add(lookVector.scale(step / 2D)), step);
			
			if(collides && step == length)
			{
				// uses the vector to a prior position before it was inside a block/entity so that the flash particle is not obscured and particles can fly out
				rangedCollisionEffect(particle, explosiveFinish, level, pos.add(lookVector.scale((step - 1) / 2D)));
			}
		}
	}
	
	private static void rangedPathParticles(ParticleOptions particle, boolean extraParticles, ClientLevel level, Vec3 vecPos, int i)
	{
		// starts creating particle trail along vector path after a few runs, its away from the players vision so they do not obscure everything
		if(i >= 5)
		{
			float offsetX = (level.random.nextFloat() - .5F) / 4;
			float offsetY = (level.random.nextFloat() - .5F) / 4;
			float offsetZ = (level.random.nextFloat() - .5F) / 4;
			level.addParticle(particle, true, vecPos.x + offsetX, vecPos.y + offsetY, vecPos.z + offsetZ, 0.0D, 0.0D, 0.0D);
			
			if(extraParticles) //particle is hard to see so this increases visibility
			{
				for(float a = 0; a < 4; a++)
				{
					float extraOffsetX = (level.random.nextFloat() - .5F) / 5;
					float extraOffsetY = (level.random.nextFloat() - .5F) / 5;
					float extraOffsetZ = (level.random.nextFloat() - .5F) / 5;
					level.addParticle(particle, true, vecPos.x + extraOffsetX, vecPos.y + extraOffsetY, vecPos.z + extraOffsetZ, 0.0D, 0.0D, 0.0D);
				}
			}
		}
	}
	
	private static void rangedCollisionEffect(ParticleOptions particle, boolean explosiveFinish, ClientLevel level, Vec3 vecPos)
	{
		int particles = 25 + level.random.nextInt(10);
		
		//if a block or entity has been hit and the wand is true for explosiveFinish boolean, adds a sound and flash
		if(explosiveFinish)
		{
			level.playLocalSound(vecPos.x, vecPos.y, vecPos.z, MSSoundEvents.ITEM_MAGIC_HIT.get(), SoundSource.BLOCKS, 1.2F, 0.6F, false);
			
			level.addParticle(ParticleTypes.FLASH, vecPos.x, vecPos.y, vecPos.z, 0.0D, 0.0D, 0.0D);
			for(int a = 0; a < particles; a++)
				level.addParticle(particle, true, vecPos.x, vecPos.y, vecPos.z, level.random.nextGaussian() * 0.12D, level.random.nextGaussian() * 0.12D, level.random.nextGaussian() * 0.12D);
		} else  //if a block or entity has been hit and the wand is false for explosiveFinish boolean, adds a small particle effect
		{
			for(int a = 0; a < particles; a++)
				level.addParticle(ParticleTypes.CRIT, true, vecPos.x, vecPos.y, vecPos.z, level.random.nextGaussian(), level.random.nextGaussian(), level.random.nextGaussian());
		}
	}
	
	public static void AOEParticleEffect(AOEType type, ClientLevel level, Vec3 minAOEBound, Vec3 maxAOEBound)
	{
		AOEParticleEffect(type.particle.get(), type.perimeterParticles, type.extraParticles, level, minAOEBound, maxAOEBound);
	}
	
	public static void AOEParticleEffect(ParticleOptions particle, boolean perimeterParticles, boolean extraParticles, ClientLevel level, Vec3 minAOEBound, Vec3 maxAOEBound)
	{
		if(!perimeterParticles)
			return;
		
		double minX = minAOEBound.x;
		double minZ = minAOEBound.z;
		double maxX = maxAOEBound.x;
		double maxZ = maxAOEBound.z;
		double y = (maxAOEBound.y + minAOEBound.y) / 2 + 4;
		
		for(double x = minX; x < maxX; x += 0.5D)
		{
			level.addParticle(particle, true, x, y + randomVariation(level), minZ, 0.0D, 0.0D, 0.0D);
			level.addParticle(particle, true, x, y + randomVariation(level), maxZ, 0.0D, 0.0D, 0.0D);
			
			if(extraParticles)
			{
				extraParticles(particle, level, new Vec3(x, y, minZ));
				extraParticles(particle, level, new Vec3(x, y, maxZ));
			}
		}
		
		for(double z = minZ; z < maxZ; z += 0.5D)
		{
			level.addParticle(particle, true, minX, y + randomVariation(level), z, 0.0D, 0.0D, 0.0D);
			level.addParticle(particle, true, maxX, y + randomVariation(level), z, 0.0D, 0.0D, 0.0D);
			
			if(extraParticles)
			{
				extraParticles(particle, level, new Vec3(minX, y, z));
				extraParticles(particle, level, new Vec3(maxX, y, z));
			}
		}
	}
	
	private static void extraParticles(ParticleOptions particle, ClientLevel level, Vec3 vecPos)
	{
		for(float a = 0; a < 4; a++)
		{
			level.addParticle(particle, true, vecPos.x, vecPos.y + randomVariation(level), vecPos.z, 0.0D, 0.0D, 0.0D);
		}
	}
	
	/**
	 * Returns a random float between -4 and 4 for use in position offsetting
	 */
	private static float randomVariation(ClientLevel level)
	{
		return level.random.nextFloat() - .5F * 8;
	}
}
