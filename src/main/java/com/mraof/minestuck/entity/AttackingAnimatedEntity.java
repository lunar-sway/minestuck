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
 */
public abstract class AttackingAnimatedEntity extends AnimatedPathfinderMob implements MobAnimationPhases.Phases.Holder
{
	private static final EntityDataAccessor<Integer> CURRENT_ACTION = SynchedEntityData.defineId(AttackingAnimatedEntity.class, EntityDataSerializers.INT);
	
	public static final MobAnimation MELEE_ANIMATION = new MobAnimation(MobAnimation.Actions.MELEE, 20, true, false);
	
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
		
		if(phase == MobAnimationPhases.Phases.ANTICIPATION)
			anticipationPhaseStart(animation);
		else if(phase == MobAnimationPhases.Phases.INITIATION)
			initiationPhaseStart(animation);
		else if(phase == MobAnimationPhases.Phases.CONTACT)
			contactPhaseStart(animation);
		else if(phase == MobAnimationPhases.Phases.RECOVERY)
			recoveryPhaseStart(animation);
		else if(phase == MobAnimationPhases.Phases.NEUTRAL)
			neutralPhaseStart(animation);
	}
	
	public void anticipationPhaseStart(MobAnimation.Actions animation)
	{
		if(animation == MobAnimation.Actions.PUNCH)
		{
			Level level = this.level;
			BlockPos entityPos = this.blockPosition();
			level.playSound(null, entityPos, SoundEvents.UI_BUTTON_CLICK, SoundSource.HOSTILE, 1.0F, 2.0F);
		}
	}
	
	public void initiationPhaseStart(MobAnimation.Actions animation)
	{
		if(animation.isAttack())
		{
			Level level = this.level;
			BlockPos entityPos = this.blockPosition();
			level.playSound(null, entityPos, SoundEvents.UI_BUTTON_CLICK, SoundSource.HOSTILE, 1.0F, 2.0F);
		}
	}
	
	public void contactPhaseStart(MobAnimation.Actions animation)
	{
		if(animation.isAttack())
		{
			Level level = this.level;
			BlockPos entityPos = this.blockPosition();
			level.playSound(null, entityPos, SoundEvents.UI_BUTTON_CLICK, SoundSource.HOSTILE, 1.0F, 2.0F);
		}
	}
	
	public void recoveryPhaseStart(MobAnimation.Actions animation)
	{
		if(animation.isAttack())
		{
			Level level = this.level;
			BlockPos entityPos = this.blockPosition();
			level.playSound(null, entityPos, SoundEvents.UI_BUTTON_CLICK, SoundSource.HOSTILE, 1.0F, 2.0F);
		}
	}
	
	public void neutralPhaseStart(MobAnimation.Actions animation)
	{
	}
}
