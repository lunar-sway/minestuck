package com.mraof.minestuck.entity.ai.attack;

import com.mojang.math.Vector3d;
import com.mraof.minestuck.entity.animation.MSMobAnimation;
import com.mraof.minestuck.entity.animation.MobAnimationPhases;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.Goal;

import javax.annotation.Nonnull;
import java.util.EnumSet;
import java.util.Objects;

/**
 * A goal for performing a slow melee attack when within hitting range.
 * The attack has a preparation phase that delays the actual attack from the moment when the target is first in range.
 * The goal updates the attack state of the attacker accordingly, so that the state can be used for animations and other things.
 */
public class SlowAttackWhenInRangeGoal<T extends PathfinderMob & MobAnimationPhases.Holder> extends Goal
{
	protected final T entity;
	/**
	 * The delay between the start of the animation and the moment the damage lands
	 */
	private final int attackDelay;
	/**
	 * The delay after an attack and before another start
	 */
	private final int attackRecovery;
	
	private final MSMobAnimation animation;
	
	private int attackDuration = -1, recoverDuration = -1;
	
	private Vector3d lookTarget;
	
	public SlowAttackWhenInRangeGoal(T entity, int attackDelay, int attackRecovery, MSMobAnimation animation)
	{
		this.entity = entity;
		this.attackDelay = attackDelay;
		this.attackRecovery = attackRecovery;
		this.animation = animation;
		
		//code block has some redundancy with the tick function in AnimatedPathfinderMob, however entities who are not members of that class are expected to be able to use this goal
		if(animation.freezesMovement() && !animation.freezesSight())
			this.setFlags(EnumSet.of(Flag.MOVE));
		else if(!animation.freezesMovement() && animation.freezesSight())
			this.setFlags(EnumSet.of(Flag.LOOK));
		else if(animation.freezesMovement() && animation.freezesSight())
			this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
	}
	
	@Override
	public boolean canUse()
	{
		LivingEntity target = this.entity.getTarget();
		return target != null && this.isValidTarget(target) && this.entity.getSensing().hasLineOfSight(target);
	}
	
	@Override
	public boolean canContinueToUse()
	{
		return attackDuration > 0 || recoverDuration > 0;
	}
	
	@Override
	public void start()
	{
		if(animation.freezesSight())
		{
			//the target should be guaranteed to be non-null because canUse() requires it to be non-null.
			LivingEntity target = Objects.requireNonNull(this.entity.getTarget());
			this.lookTarget = new Vector3d(target.getX(), target.getEyeY(), target.getZ());
		}
		
		this.attackDuration = this.attackDelay;
		this.entity.setAnimationPhase(MobAnimationPhases.ANTICIPATION, animation.getAction());
	}
	
	@Override
	public void stop()
	{
		this.attackDuration = -1;
		this.recoverDuration = -1;
		this.entity.setAnimationPhase(MobAnimationPhases.NEUTRAL, MSMobAnimation.Actions.IDLE);
	}
	
	@Override
	public void tick()
	{
		if(animation.freezesSight())
			this.entity.getLookControl().setLookAt(lookTarget.x, lookTarget.y, lookTarget.z, 30.0F, 30.0F);
		
		this.attackDuration = Math.max(this.attackDuration - 1, -1);
		this.recoverDuration = Math.max(this.recoverDuration - 1, -1);
		
		if(this.attackDuration == 0)
		{
			LivingEntity target = this.entity.getTarget();
			if(target != null && this.isValidTarget(target))
			{
				this.entity.doHurtTarget(target);
				// TODO: AOE bounding box collision checks + aoe flag
			}
			this.recoverDuration = this.attackRecovery;
			this.entity.setAnimationPhase(MobAnimationPhases.RECOVERY, animation.getAction());
		}
		
		if(this.recoverDuration == 0)
		{
			this.entity.setAnimationPhase(MobAnimationPhases.NEUTRAL, animation.getAction());
		}
	}
	
	protected boolean isValidTarget(@Nonnull LivingEntity target)
	{
		return target.isAlive() && this.getAttackReachSqr(target) >= this.entity.distanceToSqr(target);
	}
	
	protected double getAttackReachSqr(LivingEntity target)
	{
		return this.entity.getBbWidth() * 2.0F * this.entity.getBbWidth() * 2.0F + target.getBbWidth();
	}
	
}
