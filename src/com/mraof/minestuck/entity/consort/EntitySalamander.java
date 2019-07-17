package com.mraof.minestuck.entity.consort;

import com.mraof.minestuck.util.MinestuckSoundHandler;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;

public class EntitySalamander extends EntityConsort
{
	public EntitySalamander(World world)
	{
		super(world);
	}
	
	protected SoundEvent getAmbientSound()
	{
		return MinestuckSoundHandler.soundSalamanderAmbient;
	}
	
	protected SoundEvent getHurtSound(DamageSource damageSourceIn)
	{
		return MinestuckSoundHandler.soundSalamanderHurt;
	}
	
	protected SoundEvent getDeathSound()
	{
		return MinestuckSoundHandler.soundSalamanderDeath;
	}
	
	@Override
	public String getTexture()
	{
		return "textures/mobs/salamander.png";
	}
	
	@Override
	public EnumConsort getConsortType()
	{
		return EnumConsort.SALAMANDER;
	}
}