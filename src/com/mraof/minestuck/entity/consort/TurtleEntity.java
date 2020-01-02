package com.mraof.minestuck.entity.consort;

import com.mraof.minestuck.util.MSSoundEvents;
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
		return MSSoundEvents.ENTITY_TURTLE_HURT;
	}
	protected SoundEvent getDeathSound()
	{
		return MSSoundEvents.ENTITY_TURTLE_DEATH;
	}
	
	@Override
	public EnumConsort getConsortType()
	{
		return EnumConsort.TURTLE;
	}
}