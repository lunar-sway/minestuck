package com.mraof.minestuck.client.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class FireflyParticle extends TextureSheetParticle
{
	FireflyParticle(ClientLevel clientLevel, double x, double y, double z)
	{
		super(clientLevel, x, y, z);
		
		this.lifetime = 15;
		this.hasPhysics = true;
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
		setAlpha((float) Math.sin(age / 4.75)); //simulated light flicker, fully opaque at the middle of the wave/lifetime and fully transparent at the beginning and close to the end
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
			FireflyParticle fireflyParticle = new FireflyParticle(pLevel, pX, pY, pZ);
			fireflyParticle.pickSprite(spriteSet);
			return fireflyParticle;
		}
	}
}