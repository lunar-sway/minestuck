package com.mraof.minestuck.entity;

import com.mraof.minestuck.entity.animation.MSMobAnimation;
import com.mraof.minestuck.entity.animation.MobAnimationPhases;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

/**
 * A base class for animated entities with a potentially delayed attack.
 */
public abstract class AttackingAnimatedEntity extends AnimatedPathfinderMob implements MobAnimationPhases.Holder
{
	private static final EntityDataAccessor<Integer> CURRENT_ACTION = SynchedEntityData.defineId(AttackingAnimatedEntity.class, EntityDataSerializers.INT);
	
	protected AttackingAnimatedEntity(EntityType<? extends AttackingAnimatedEntity> type, Level level)
	{
		super(type, level);
	}
	
	@Override
	protected void defineSynchedData()
	{
		super.defineSynchedData();
		entityData.define(CURRENT_ACTION, MSMobAnimation.IDLE_ACTION.ordinal());
	}
	
	@Override
	public MobAnimationPhases getPhase()
	{
		return MobAnimationPhases.values()[this.entityData.get(CURRENT_ACTION)];
	}
	
	@Override
	public void setAnimationPhase(MobAnimationPhases phase, MSMobAnimation.Actions animation)
	{
		this.entityData.set(CURRENT_ACTION, phase.ordinal());
		
		if(phase == MobAnimationPhases.ANTICIPATION)
			anticipationPhaseStart(animation);
		else if(phase == MobAnimationPhases.INITIATION)
			initiationPhaseStart(animation);
		else if(phase == MobAnimationPhases.CONTACT)
			contactPhaseStart(animation);
		else if(phase == MobAnimationPhases.RECOVERY)
			recoveryPhaseStart(animation);
		else if(phase == MobAnimationPhases.NEUTRAL)
			neutralPhaseStart(animation);
	}
	
	public void anticipationPhaseStart(MSMobAnimation.Actions animation)
	{
	}
	
	public void initiationPhaseStart(MSMobAnimation.Actions animation)
	{
	}
	
	public void contactPhaseStart(MSMobAnimation.Actions animation)
	{
	}
	
	public void recoveryPhaseStart(MSMobAnimation.Actions animation)
	{
	}
	
	public void neutralPhaseStart(MSMobAnimation.Actions animation)
	{
	}
}
