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
	
	protected static class DelayedAttackGoal extends MeleeAttackGoal
	{
		private final AttackingAnimatedEntity entity;
		private final boolean attackStopsMovement;
		
		/**
		 * The same as MeleeAttackGoal but it does not apply damage immediately when performing an attack
		 * Should be used only internally by AnimatedCreatureEntity
		 */
		public DelayedAttackGoal(AttackingAnimatedEntity entity, float speed, boolean useMemory, boolean attackStopsMovement)
		{
			super(entity, speed, useMemory);
			this.entity = entity;
			this.attackStopsMovement = attackStopsMovement;
		}
		
		@Override
		protected void checkAndPerformAttack(LivingEntity enemy, double distToEnemySqr)
		{
			double reach = this.getAttackReachSqr(enemy);
			if(distToEnemySqr <= reach && !entity.hasTimedAction())
			{
				if(this.attackStopsMovement)
				{
					// Meant to stop the entity while performing its attack animation
					entity.getNavigation().stop();
				}
				entity.setCurrentAction(Actions.ATTACK, entity.attackDelay);
			}
		}
		
		@Override
		public void stop()
		{
			entity.setCurrentAction(Actions.NONE);
		}
	}
}
