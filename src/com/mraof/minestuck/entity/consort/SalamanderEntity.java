package com.mraof.minestuck.entity.consort;

import com.mraof.minestuck.util.ModSoundEvents;
import net.minecraft.entity.EntityType;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;

public class SalamanderEntity extends ConsortEntity
{
	public SalamanderEntity(EntityType<? extends SalamanderEntity> type, World world)
	{
		super(type, world);
	}
	
	protected SoundEvent getAmbientSound()
	{
		return ModSoundEvents.ENTITY_SALAMANDER_AMBIENT;
	}
	
	protected SoundEvent getHurtSound(DamageSource damageSourceIn)
	{
		return ModSoundEvents.ENTITY_SALAMANDER_HURT;
	}
	
	protected SoundEvent getDeathSound()
	{
		return ModSoundEvents.ENTITY_SALAMANDER_DEATH;
	}
	
	@Override
	public String getTexture()
	{
		return "textures/entity/salamander.png";
	}
	
	@Override
	public EnumConsort getConsortType()
	{
		return EnumConsort.SALAMANDER;
	}
}