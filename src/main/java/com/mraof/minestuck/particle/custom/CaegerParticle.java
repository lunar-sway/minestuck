package com.mraof.minestuck.particle.custom;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class CaegerParticle extends TextureSheetParticle
{
	
	protected CaegerParticle(ClientLevel level, double xCoord, double yCoord, double zCoord,
							 SpriteSet spriteSet, double xs, double ys, double zs) {
		super(level, xCoord, yCoord, zCoord, xs, ys, zs);
		
		//location and movement. S refering to speed this controls the velocity of the particle
		this.friction = 0.0F;
		this.xd = xs;
		this.yd = ys;
		this.zd = zs;
		
		//size and lifetime
		this.quadSize *= 0.05F;
		this.lifetime = 40;
		
		//makes the game not crash
		this.setSpriteFromAge(spriteSet);
		
		//color
		this.rCol = 1F;
		this.gCol = 1F;
		this.bCol = 1F;
	}
	
	@Override
	public void tick()
	{
		super.tick();
		alphaFade();
	}
	
	private void alphaFade()
	{
		this.alpha = (-(1/(float)lifetime) * age + 1);
	}
	
	@Override
	public ParticleRenderType getRenderType()
	{
		return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
	}
	
	@OnlyIn(Dist.CLIENT)
	public static class Provider implements ParticleProvider<SimpleParticleType>
	{
		private final SpriteSet sprites;
		
		public Provider(SpriteSet spriteSet)
		{
			this.sprites = spriteSet;
		}
		
		public Particle createParticle(SimpleParticleType particleType, ClientLevel level, double x, double y, double z,
									   double dx, double dy, double dz)
		{
			return new CaegerParticle(level, x, y, z, this.sprites, dx, dy, dz);
		}
	}
}
