package com.mraof.minestuck.entity;

import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

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
	 * The attack state is meant to be set exclusively by {@link MoveToTargetGoal}.
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
	 * The same as MeleeAttackGoal, except that the goal does not handle the actual attack.
	 */
	protected static class MoveToTargetGoal extends MeleeAttackGoal
	{
		public MoveToTargetGoal(CreatureEntity entity, float speed, boolean followsUnseenTarget)
		{
			super(entity, speed, followsUnseenTarget);
		}
		
		@Override
		protected void checkAndPerformAttack(LivingEntity enemy, double distToEnemySqr)
		{
		}
	}
	
	/**
	 * A goal for performing a slow melee attack when within hitting range.
	 * The attack has a preparation phase that delays the actual attack from the moment when the target is first in range.
	 * The goal updates the attack state of the attacker accordingly, so that the state can be used for animations and other things.
	 */
	protected static class SlowAttackWhenInRangeGoal extends Goal
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
		
		public SlowAttackWhenInRangeGoal(AttackingAnimatedEntity entity, boolean attackStopsMovement, int attackDelay, int attackRecovery)
		{
			this.entity = entity;
			this.attackStopsMovement = attackStopsMovement;
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
			if(this.attackStopsMovement)
			{
				// Meant to stop the entity while performing its attack animation
				//TODO not done yet
				entity.getNavigation().stop();
			}
			
			this.attackDuration = this.attackDelay;
			this.entity.setAttackState(AttackState.ATTACK);
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
				this.entity.setAttackState(AttackState.ATTACK_RECOVERY);
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
		
		protected double getAttackReachSqr(LivingEntity target) {
			return this.entity.getBbWidth() * 2.0F * this.entity.getBbWidth() * 2.0F + target.getBbWidth();
		}
	}
}
