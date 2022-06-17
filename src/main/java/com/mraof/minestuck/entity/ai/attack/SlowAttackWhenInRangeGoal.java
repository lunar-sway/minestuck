package com.mraof.minestuck.entity.ai.attack;

import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.Goal;

import javax.annotation.Nonnull;

/**
 * A goal for performing a slow melee attack when within hitting range.
 * The attack has a preparation phase that delays the actual attack from the moment when the target is first in range.
 * The goal updates the attack state of the attacker accordingly, so that the state can be used for animations and other things.
 */
public class SlowAttackWhenInRangeGoal<T extends CreatureEntity & AttackState.Holder> extends Goal
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
	
	private int attackDuration = -1, recoverDuration = -1;
	
	public SlowAttackWhenInRangeGoal(T entity, int attackDelay, int attackRecovery)
	{
		this.entity = entity;
		this.attackDelay = attackDelay;
		this.attackRecovery = attackRecovery;
	}
	
	@Override
	public boolean canUse()
	{
		LivingEntity target = this.entity.getTarget();
		return target != null && this.isValidTarget(target) && this.entity.getSensing().canSee(target);
	}
	
	@Override
	public boolean canContinueToUse()
	{
		return attackDuration > 0 || recoverDuration > 0;
	}
	
	@Override
	public void start()
	{
		this.attackDuration = this.attackDelay;
		this.entity.setAttackState(AttackState.PREPARATION);
	}
	
	@Override
	public void stop()
	{
		this.attackDuration = -1;
		this.recoverDuration = -1;
		this.entity.setAttackState(AttackState.NONE);
	}
	
	@Override
	public void tick()
	{
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
			this.entity.setAttackState(AttackState.RECOVERY);
		}
		
		if(this.recoverDuration == 0)
		{
			this.entity.setAttackState(AttackState.NONE);
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
