package com.mraof.minestuck.entity.consort;

import com.mraof.minestuck.entity.ModEntityTypes;
import com.mraof.minestuck.util.MinestuckSoundHandler;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class EntityNakagator extends EntityConsort
{
	public EntityNakagator(World world)
	{
		super(ModEntityTypes.NAKAGATOR, world);
	}
	
	protected SoundEvent getAmbientSound()
	{
		return MinestuckSoundHandler.soundNakagatorAmbient;
	}
	
	protected SoundEvent getHurtSound(DamageSource damageSourceIn)
	{
		return MinestuckSoundHandler.soundNakagatorHurt;
	}
	
	protected SoundEvent getDeathSound()
	{
		return MinestuckSoundHandler.soundNakagatorDeath;
	}
	
	@Override
	public String getTexture()
	{
		return "textures/entity/nakagator.png";
	}
	
	@Override
	public EnumConsort getConsortType()
	{
		return EnumConsort.NAKAGATOR;
	}
}