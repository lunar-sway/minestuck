package com.mraof.minestuck.entity;

import com.mraof.minestuck.entity.animation.MobAnimation;
import com.mraof.minestuck.entity.animation.PhasedMobAnimation;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

/**
 * A base class for animated entities with a potentially delayed attack.
 * Through the use of PhasedMobAnimation, additional code can be deployed at the changing point of each animation phase of specific animations.
 */
public abstract class AttackingAnimatedEntity extends AnimatedPathfinderMob implements PhasedMobAnimation.Phases.Holder
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
		entityData.define(CURRENT_ACTION, MobAnimation.IDLE_ACTION.ordinal());
	}
	
	@Override
	public PhasedMobAnimation.Phases getPhase()
	{
		return PhasedMobAnimation.Phases.values()[this.entityData.get(CURRENT_ACTION)];
	}
	
	@Override
	public void setAnimationPhase(PhasedMobAnimation.Phases phase, MobAnimation.Actions animation)
	{
		this.entityData.set(CURRENT_ACTION, phase.ordinal());
		
		if(phase == PhasedMobAnimation.Phases.ANTICIPATION) //first tick of animation
			anticipationPhaseStart(animation);
		else if(phase == PhasedMobAnimation.Phases.INITIATION)
			initiationPhaseStart(animation);
		else if(phase == PhasedMobAnimation.Phases.CONTACT)
			contactPhaseStart(animation);
		else if(phase == PhasedMobAnimation.Phases.RECOVERY)
			recoveryPhaseStart(animation);
		else if(phase == PhasedMobAnimation.Phases.NEUTRAL) //last tick of animation
			neutralPhaseStart(animation);
	}
	
	public void anticipationPhaseStart(MobAnimation.Actions animation)
	{
	}
	
	public void initiationPhaseStart(MobAnimation.Actions animation)
	{
	}
	
	public void contactPhaseStart(MobAnimation.Actions animation)
	{
	}
	
	public void recoveryPhaseStart(MobAnimation.Actions animation)
	{
	}
	
	public void neutralPhaseStart(MobAnimation.Actions animation)
	{
	}
}
