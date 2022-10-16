package com.mraof.minestuck.client.util;


import com.mojang.math.Vector3f;
import com.mraof.minestuck.util.MSSoundEvents;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.phys.Vec3;

import java.util.function.Supplier;

public class MagicEffect
{
	public enum Type
	{
		GREEN(() -> new DustParticleOptions(new Vector3f(0F, 1F, 0F), 2F), false, false),
		CRIT(() -> ParticleTypes.ENCHANTED_HIT, false, false),
		ENCHANT(() -> ParticleTypes.ENCHANT, false, true),
		RED(() -> new DustParticleOptions(new Vector3f(20F, 0F, 0F), 2F), false, false),
		INK(() -> ParticleTypes.SQUID_INK, true, false),
		ZILLY(() -> new DustParticleOptions(new Vector3f(20F, 20F, 20F), 2F), true, false),
		ECHIDNA(() -> ParticleTypes.END_ROD, true, false);
		
		private final Supplier<ParticleOptions> particle;
		private final boolean explosiveFinish;
		private final boolean extraParticles;
		
		Type(Supplier<ParticleOptions> particle, boolean explosiveFinish, boolean extraParticles)
		{
			this.particle = particle;
			this.explosiveFinish = explosiveFinish;
			this.extraParticles = extraParticles;
		}
		
		public int toInt()
		{
			return ordinal();
		}
		
		public static Type fromInt(int i)
		{
			if(i >= 0 && i < values().length)
				return values()[i];
			else return ENCHANT;
		}
	}
	
	public static void particleEffect(Type type, ClientLevel level, Vec3 pos, Vec3 lookVector, int length, boolean collides)
	{
		particleEffect(type.particle.get(), type.explosiveFinish, type.extraParticles, level, pos, lookVector, length, collides);
	}
	
	public static void particleEffect(ParticleOptions particle, boolean explosiveFinish, boolean extraParticles, ClientLevel level, Vec3 pos, Vec3 lookVector, int length, boolean collides)
	{
		for(int step = 0; step <= length; step++)
		{
			pathParticles(particle, extraParticles, level, pos.add(lookVector.scale(step / 2D)), step);
			
			if(collides && step == length)
			{
				// uses the vector to a prior position before it was inside a block/entity so that the flash particle is not obscured and particles can fly out
				collisionEffect(particle, explosiveFinish, level, pos.add(lookVector.scale((step - 1) / 2D)));
			}
		}
	}
	
	private static void pathParticles(ParticleOptions particle, boolean extraParticles, ClientLevel level, Vec3 vecPos, int i)
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
	
	private static void collisionEffect(ParticleOptions particle, boolean explosiveFinish, ClientLevel level, Vec3 vecPos)
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
}