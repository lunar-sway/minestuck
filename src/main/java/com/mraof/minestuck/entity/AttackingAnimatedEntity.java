package com.mraof.minestuck.entity;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.World;

/**
 * A base class for animated entities with a potentially delayed attack.
 */
public abstract class AttackingAnimatedEntity extends AnimatedCreatureEntity
{
	/**
	 * The delay between the start of the animation and the moment the damage lands
	 */
	protected int attackDelay;
	
	/**
	 * The delay after an attack and before another start
	 */
	protected int attackRecovery;
	
	protected AttackingAnimatedEntity(EntityType<? extends AttackingAnimatedEntity> type, World world)
	{
		super(type, world);
	}
	
	@Override
	protected void endTimedAction(Actions action)
	{
		if(action == Actions.ATTACK)
		{
			this.setCurrentAction(Actions.ATTACK_RECOVERY, attackRecovery);
			performAttack();
		} else
			super.endTimedAction(action);
	}
	
	private void performAttack()
	{
		this.onAttackEnd();
		
		if(getTarget() != null && isInRange(getTarget()))
		{
			doHurtTarget(getTarget());
			// TODO: AOE bounding box collision checks + aoe flag
		}
	}
	
	private boolean isInRange(LivingEntity target)
	{
		double reach = this.getBbWidth() * 2.0F * this.getBbWidth() * 2.0F + target.getBbWidth();
		return this.distanceToSqr(target) <= reach;
	}
	
	/**
	 * Starts a long attack against the current target if this entity isn't already performing some action with a duration.
	 * Useful to sync animations and add extra delays
	 */
	private void startAttack()
	{
		if(!this.hasTimedAction())
		{
			this.setCurrentAction(Actions.ATTACK, attackDelay);
			
			this.onAttackStart();
		}
	}
	
	/**
	 * Is called when an attack starts. Can be extended to apply effects during an attack.
	 */
	protected void onAttackStart()
	{
	}
	
	/**
	 * Is called when an attack ends. Can be extended to remove effects once an attack has ended.
	 * The end of an attack does not necessarily equate to an attack hit.
	 */
	protected void onAttackEnd()
	{
	}
	
	protected static class DelayedAttackGoal extends MeleeAttackGoal
	{
		private final AttackingAnimatedEntity entity;
		
		/**
		 * The same as MeleeAttackGoal but it does not apply damage immediately when performing an attack
		 * Should be used only internally by AnimatedCreatureEntity
		 */
		public DelayedAttackGoal(AttackingAnimatedEntity entity, float speed, boolean useMemory)
		{
			super(entity, speed, useMemory);
			this.entity = entity;
		}
		
		@Override
		protected void checkAndPerformAttack(LivingEntity enemy, double distToEnemySqr)
		{
			double reach = this.getAttackReachSqr(enemy);
			if(distToEnemySqr <= reach && this.isTimeToAttack())
			{
				this.resetAttackCooldown();
				entity.startAttack();
			}
		}
	}
}
