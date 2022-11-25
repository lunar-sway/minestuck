package com.mraof.minestuck.entity;

import com.mraof.minestuck.entity.animation.MobAnimation;
import com.mraof.minestuck.entity.animation.MobAnimationPhases;
import net.minecraft.core.BlockPos;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

/**
 * A base class for animated entities with a potentially delayed attack.
 * Through the use of MobAnimationPhases, additional code can be deployed at the changing point of each animation phase of specific animations.
 */
public abstract class AttackingAnimatedEntity extends AnimatedPathfinderMob implements MobAnimationPhases.Phases.Holder
{
	private static final EntityDataAccessor<Integer> CURRENT_ACTION = SynchedEntityData.defineId(AttackingAnimatedEntity.class, EntityDataSerializers.INT);
	
	public static final MobAnimationPhases MELEE_PHASES = new MobAnimationPhases(2, 4, 5, 10);
	public static final MobAnimation MELEE_ANIMATION = new MobAnimation(MobAnimation.Actions.MELEE, MELEE_PHASES.getTotalAnimationLength(), true, false);
	
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
	public MobAnimation getDefaultAttackAnimation()
	{
		return MELEE_ANIMATION;
	}
	
	@Override
	public MobAnimationPhases.Phases getPhase()
	{
		return MobAnimationPhases.Phases.values()[this.entityData.get(CURRENT_ACTION)];
	}
	
	@Override
	public void setAnimationPhase(MobAnimationPhases.Phases phase, MobAnimation.Actions animation)
	{
		this.entityData.set(CURRENT_ACTION, phase.ordinal());
		
		if(phase == MobAnimationPhases.Phases.ANTICIPATION) //first tick of animation
			anticipationPhaseStart(animation);
		else if(phase == MobAnimationPhases.Phases.INITIATION)
			initiationPhaseStart(animation);
		else if(phase == MobAnimationPhases.Phases.CONTACT)
			contactPhaseStart(animation);
		else if(phase == MobAnimationPhases.Phases.RECOVERY)
			recoveryPhaseStart(animation);
		else if(phase == MobAnimationPhases.Phases.NEUTRAL) //last tick of animation
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
