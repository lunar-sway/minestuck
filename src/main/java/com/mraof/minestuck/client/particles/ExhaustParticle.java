package com.mraof.minestuck.client.particles;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;

public class ExhaustParticle extends TextureSheetParticle
{
	private final SpriteSet spriteSet;
	
	ExhaustParticle(ClientLevel clientLevel, double x, double y, double z, SpriteSet spriteSet)
	{
		super(clientLevel, x, y, z);
		
		this.scale(3.5f);
		this.lifetime = 12;
		
		this.spriteSet = spriteSet;
		this.setSpriteFromAge(this.spriteSet);
	}
	
	@Override
	public ParticleRenderType getRenderType()
	{
		return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
	}
	
	@Override
	public void tick()
	{
		super.tick();
		setSpriteFromAge(this.spriteSet);
	}
	
	public static class Provider implements ParticleProvider<SimpleParticleType>
	{
		private final SpriteSet spriteSet;
		
		public Provider(SpriteSet spriteSet)
		{
			this.spriteSet = spriteSet;
		}
		
		@Override
		public Particle createParticle(SimpleParticleType pType, ClientLevel pLevel, double pX, double pY, double pZ, double pXSpeed, double pYSpeed, double pZSpeed)
		{
			ExhaustParticle exhaustParticle = new ExhaustParticle(pLevel, pX, pY, pZ, this.spriteSet);
			exhaustParticle.pickSprite(spriteSet);
			return exhaustParticle;
		}
	}
}
