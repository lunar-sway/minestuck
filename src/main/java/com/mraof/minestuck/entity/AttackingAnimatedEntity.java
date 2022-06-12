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
	
	protected static class DelayedAttackGoal extends MeleeAttackGoal
	{
		private final AttackingAnimatedEntity entity;
		private final boolean attackStopsMovement;
		
		private int attackDuration = -1, recoverDuration = -1;
		
		/**
		 * The same as MeleeAttackGoal but it does not apply damage immediately when performing an attack
		 * Should be used only internally by AnimatedCreatureEntity
		 */
		public DelayedAttackGoal(AttackingAnimatedEntity entity, float speed, boolean attackStopsMovement)
		{
			super(entity, speed, true);	// If this boolean is false, the goal will stop when the navigation is stopped, which is not what we want to happen
			this.entity = entity;
			this.attackStopsMovement = attackStopsMovement;
		}
		
		@Override
		protected void checkAndPerformAttack(LivingEntity enemy, double distToEnemySqr)
		{
			this.attackDuration = Math.max(this.attackDuration - 1, -1);
			this.recoverDuration = Math.max(this.recoverDuration - 1, -1);
			
			double reach = this.getAttackReachSqr(enemy);
			
			//Check attack start
			if(distToEnemySqr <= reach && this.attackDuration < 0 && this.recoverDuration < 0)
			{
				if(this.attackStopsMovement)
				{
					// Meant to stop the entity while performing its attack animation
					entity.getNavigation().stop();
				}
				
				this.attackDuration = entity.attackDelay;
				entity.setCurrentAction(Actions.ATTACK);
			}
			
			//Check attack end
			if(this.attackDuration == 0)
			{
				if(distToEnemySqr <= reach)
				{
					entity.doHurtTarget(enemy);
					// TODO: AOE bounding box collision checks + aoe flag
				}
				this.recoverDuration = entity.attackRecovery;
				entity.setCurrentAction(Actions.ATTACK_RECOVERY);
			}
			
			if(this.recoverDuration == 0)
			{
				entity.setCurrentAction(Actions.NONE);
			}
		}
		
		@Override
		public void stop()
		{
			this.attackDuration = -1;
			this.recoverDuration = -1;
			entity.setCurrentAction(Actions.NONE);
		}
	}
}
