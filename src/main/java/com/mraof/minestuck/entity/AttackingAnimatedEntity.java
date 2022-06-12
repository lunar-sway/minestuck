package com.mraof.minestuck.entity;

import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.world.World;

/**
 * A base class for animated entities with a potentially delayed attack.
 */
public abstract class AttackingAnimatedEntity extends CreatureEntity
{
	private static final DataParameter<Integer> CURRENT_ACTION = EntityDataManager.defineId(AttackingAnimatedEntity.class, DataSerializers.INT);
	
	protected AttackingAnimatedEntity(EntityType<? extends AttackingAnimatedEntity> type, World world)
	{
		super(type, world);
	}
	
	@Override
	protected void defineSynchedData()
	{
		super.defineSynchedData();
		entityData.define(CURRENT_ACTION, AnimatedCreatureEntity.Actions.NONE.ordinal());
	}
	
	/**
	 * Used to start animations
	 *
	 * @return true if the entity performing an attack
	 */
	protected boolean isAttacking()
	{
		AttackState state = this.getAttackState();
		return state == AttackState.ATTACK || state == AttackState.ATTACK_RECOVERY;
	}
	
	/**
	 * @return true during the attack animation right before an attack lands.
	 */
	protected boolean isPreparingToAttack()
	{
		return this.getAttackState() == AttackState.ATTACK;
	}
	
	/**
	 * @return the current state of the entity's melee attack
	 */
	protected AttackState getAttackState()
	{
		return AttackState.values()[this.entityData.get(CURRENT_ACTION)];
	}
	
	/**
	 * Used to set the entity's attack state.
	 * The attack state is meant to be set exclusively by {@link DelayedAttackGoal}.
	 *
	 * @param state The new state of the entity's melee attack
	 */
	public void setAttackState(AttackState state)
	{
		this.entityData.set(CURRENT_ACTION, state.ordinal());
	}
	
	public enum AttackState
	{
		NONE,
		ATTACK,
		ATTACK_RECOVERY
	}
	
	/**
	 * The same as MeleeAttackGoal, except that the moment of attack is not instantaneous.
	 * Instead, the attack has a preparation phase that delays the actual attack from the moment when the target is first in range.
	 * The goal updates the attack state of the attacker accordingly, so that the state can be used for animations and other things.
	 */
	protected static class DelayedAttackGoal extends MeleeAttackGoal
	{
		private final AttackingAnimatedEntity entity;
		private final boolean attackStopsMovement;
		/**
		 * The delay between the start of the animation and the moment the damage lands
		 */
		private final int attackDelay;
		/**
		 * The delay after an attack and before another start
		 */
		private final int attackRecovery;
		
		private int attackDuration = -1, recoverDuration = -1;
		
		public DelayedAttackGoal(AttackingAnimatedEntity entity, float speed, boolean attackStopsMovement, int attackDelay, int attackRecovery)
		{
			super(entity, speed, true);	// If this boolean is false, the goal will stop when the navigation is stopped, which is not what we want to happen
			this.entity = entity;
			this.attackStopsMovement = attackStopsMovement;
			this.attackDelay = attackDelay;
			this.attackRecovery = attackRecovery;
		}
		
		@Override
		protected void checkAndPerformAttack(LivingEntity enemy, double distToEnemySqr)
		{
			this.attackDuration = Math.max(this.attackDuration - 1, -1);
			this.recoverDuration = Math.max(this.recoverDuration - 1, -1);
			
			double reach = this.getAttackReachSqr(enemy);
			
			//Check for attack start
			if(distToEnemySqr <= reach && this.attackDuration < 0 && this.recoverDuration < 0)
			{
				if(this.attackStopsMovement)
				{
					// Meant to stop the entity while performing its attack animation
					//TODO not done yet
					entity.getNavigation().stop();
				}
				
				this.attackDuration = this.attackDelay;
				entity.setAttackState(AttackState.ATTACK);
			}
			
			//Check for attack end
			if(this.attackDuration == 0)
			{
				if(distToEnemySqr <= reach)
				{
					entity.doHurtTarget(enemy);
					// TODO: AOE bounding box collision checks + aoe flag
				}
				this.recoverDuration = this.attackRecovery;
				entity.setAttackState(AttackState.ATTACK_RECOVERY);
			}
			
			//Check for attack recovery end
			if(this.recoverDuration == 0)
			{
				entity.setAttackState(AttackState.NONE);
			}
		}
		
		@Override
		public void stop()
		{
			this.attackDuration = -1;
			this.recoverDuration = -1;
			entity.setAttackState(AttackState.NONE);
		}
	}
}
