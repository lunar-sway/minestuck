package com.mraof.minestuck.client.particles;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;

public class PlasmaParticle extends TextureSheetParticle
{
	private final SpriteSet spriteSet;
	
	PlasmaParticle(ClientLevel clientLevel, double x, double y, double z, SpriteSet spriteSet)
	{
		super(clientLevel, x, y, z);
		
		this.spriteSet = spriteSet;
		this.setSpriteFromAge(this.spriteSet);
		this.lifetime = 6;
		this.scale(8);
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
			PlasmaParticle plasmaParticle = new PlasmaParticle(pLevel, pX, pY, pZ, this.spriteSet);
			plasmaParticle.pickSprite(spriteSet);
			return plasmaParticle;
		}
	}
}