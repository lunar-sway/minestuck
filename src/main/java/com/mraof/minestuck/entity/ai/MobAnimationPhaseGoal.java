package com.mraof.minestuck.entity.ai;

import com.mojang.math.Vector3d;
import com.mraof.minestuck.entity.AnimatedPathfinderMob;
import com.mraof.minestuck.entity.animation.MobAnimation;
import com.mraof.minestuck.entity.animation.MobAnimationPhases;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.Goal;

import java.util.EnumSet;
import java.util.Objects;

/**
 * A goal for performing a slow melee attack when within hitting range.
 * The attack has a preparation phase that delays the actual attack from the moment when the target is first in range.
 * The goal updates the attack state of the attacker accordingly, so that the state can be used for animations and other things.
 */
public abstract class MobAnimationPhaseGoal<T extends PathfinderMob & MobAnimationPhases.Phases.Holder> extends Goal
{
	protected final T entity;
	
	/**
	 * The delay after an attack and before another start
	 */
	//protected final int attackRecovery;
	
	protected final MobAnimation animation;
	
	protected final MobAnimationPhases phases;
	
	protected int time = 0;
	
	//protected int attackDuration = -1, recoverDuration = -1;
	
	protected Vector3d lookTarget;
	
	public MobAnimationPhaseGoal(T entity,/* int attackDelay, int attackRecovery, */MobAnimation animation, MobAnimationPhases phases)
	{
		this.entity = entity;
		//this.attackRecovery = attackRecovery;
		this.animation = animation;
		this.phases = phases;
		
		//code block has some redundancy with the tick function in AnimatedPathfinderMob, however entities who are not members of that class are expected to be able to use this goal
		if(animation.freezesMovement() && !animation.freezesSight())
			this.setFlags(EnumSet.of(Flag.MOVE));
		else if(!animation.freezesMovement() && animation.freezesSight())
			this.setFlags(EnumSet.of(Flag.LOOK));
		else if(animation.freezesMovement() && animation.freezesSight())
			this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
	}
	
	/*@Override
	public boolean canUse()
	{
		LivingEntity target = this.entity.getTarget();
		return target != null && this.isValidTarget(target) && this.entity.getSensing().hasLineOfSight(target);
	}*/
	
	@Override
	public boolean canUse()
	{
		return true;
	}
	
	@Override
	public boolean canContinueToUse()
	{
		return phases.getCurrentPhase(time) != MobAnimationPhases.Phases.NEUTRAL; //attackDuration > 0 || recoverDuration > 0;
	}
	
	@Override
	public void start()
	{
		if(entity instanceof AnimatedPathfinderMob animatedMob)
			animatedMob.setCurrentAnimation(animation);
		
		if(animation.freezesSight())
		{
			//the target should be guaranteed to be non-null because canUse() requires it to be non-null.
			LivingEntity target = Objects.requireNonNull(this.entity.getTarget());
			this.lookTarget = new Vector3d(target.getX(), target.getEyeY(), target.getZ());
		}
		
		//this.attackDuration = this.attackDelay;
		this.entity.setAnimationPhase(MobAnimationPhases.Phases.ANTICIPATION, animation.getAction());
	}
	
	@Override
	public void stop()
	{
		this.time = 0;
		//this.attackDuration = -1;
		//this.recoverDuration = -1;
		if(entity instanceof AnimatedPathfinderMob animatedMob)
			animatedMob.endCurrentAction();
	}
	
	@Override
	public void tick()
	{
		super.tick(); //make sure the super function is ran
		
		if(animation.freezesSight())
			this.entity.getLookControl().setLookAt(lookTarget.x, lookTarget.y, lookTarget.z, 30.0F, 30.0F);
		
		this.time++;
		phases.attemptPhaseChange(time, entity, animation);
		
		
		//this.attackDuration = Math.max(this.attackDuration - 1, -1);
		//this.recoverDuration = Math.max(this.recoverDuration - 1, -1);
		
		/*if(this.attackDuration == 0)
		{
			LivingEntity target = this.entity.getTarget();
			if(target != null && this.isValidTarget(target))
			{
				this.entity.doHurtTarget(target);
				// TODO: AOE bounding box collision checks + aoe flag
			}
			this.recoverDuration = this.attackRecovery;
			this.entity.setAnimationPhase(MobAnimationPhases.Phases.RECOVERY, animation.getAction()); //TODO add contact phase
		}
		
		if(this.recoverDuration == 0)
		{
			this.entity.setAnimationPhase(MobAnimationPhases.Phases.NEUTRAL, animation.getAction());
		}*/
	}
}