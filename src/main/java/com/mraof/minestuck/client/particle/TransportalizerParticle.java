package com.mraof.minestuck.client.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class TransportalizerParticle extends TextureSheetParticle
{
	private final SpriteSet spriteSet;
	
	TransportalizerParticle(ClientLevel clientLevel, double x, double y, double z, SpriteSet spriteSet)
	{
		super(clientLevel, x, y, z);
		
		this.spriteSet = spriteSet;
		this.setSpriteFromAge(this.spriteSet);
		this.lifetime = 4;
		this.scale(5); //still has some size variation
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
	
	@OnlyIn(Dist.CLIENT)
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
			TransportalizerParticle transportalizerParticle = new TransportalizerParticle(pLevel, pX, pY, pZ, this.spriteSet);
			transportalizerParticle.pickSprite(spriteSet);
			return transportalizerParticle;
		}
	}
}