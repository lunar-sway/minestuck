package com.mraof.minestuck.entity.consort;

import com.mraof.minestuck.util.ModSoundEvents;
import net.minecraft.entity.EntityType;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;

public class TurtleEntity extends ConsortEntity
{
	public TurtleEntity(EntityType<? extends TurtleEntity> type, World world)
	{
		super(type, world);
	}
	
	protected SoundEvent getHurtSound(DamageSource damageSourceIn)
	{
		return ModSoundEvents.ENTITY_TURTLE_HURT;
	}
	protected SoundEvent getDeathSound()
	{
		return ModSoundEvents.ENTITY_TURTLE_DEATH;
	}
	
	@Override
	public String getTexture()
	{
		return "textures/entity/turtle.png";
	}
	
	@Override
	public EnumConsort getConsortType()
	{
		return EnumConsort.TURTLE;
	}
}