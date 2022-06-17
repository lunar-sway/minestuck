package com.mraof.minestuck.entity.ai;

import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.attributes.ModifiableAttributeInstance;
import net.minecraft.entity.ai.goal.Goal;

import javax.annotation.Nonnull;
import java.util.EnumSet;
import java.util.UUID;

/**
 * A goal for performing a slow melee attack when within hitting range.
 * The attack has a preparation phase that delays the actual attack from the moment when the target is first in range.
 * The goal updates the attack state of the attacker accordingly, so that the state can be used for animations and other things.
 */
public class SlowAttackWhenInRangeGoal<T extends CreatureEntity & SlowAttackWhenInRangeGoal.AttackStateHolder> extends Goal
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
	
	protected double getAttackReachSqr(LivingEntity target)
	{
		return this.entity.getBbWidth() * 2.0F * this.entity.getBbWidth() * 2.0F + target.getBbWidth();
	}
	
	public enum AttackState
	{
		NONE,
		ATTACK,
		ATTACK_RECOVERY
	}
	
	/**
	 * Like {@link SlowAttackWhenInRangeGoal}, but interrupts any movement and look goals to stand still and look at the target.
	 */
	public static class InPlace<T extends CreatureEntity & AttackStateHolder> extends SlowAttackWhenInRangeGoal<T>
	{
		public InPlace(T entity, int attackDelay, int attackRecovery)
		{
			super(entity, attackDelay, attackRecovery);
			// Will stop any other goal with movement or looking if this goal activates
			this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
		}
		
		@Override
		public void tick()
		{
			LivingEntity target = this.entity.getTarget();
			if(target != null)
				this.entity.getLookControl().setLookAt(target, 30.0F, 30.0F);
			super.tick();
		}
	}
	
	/**
	 * An alternative to {@link InPlace}.
	 * While {@link InPlace} stops movement by interrupting the movement goal,
	 * this goal avoids that by instead applying a movement modifier that sets movement speed to 0.
	 * Also, while {@link InPlace} is used instead of {@link SlowAttackWhenInRangeGoal},
	 * this goal is used on top of {@link SlowAttackWhenInRangeGoal}.
	 */
	public static class ZeroMovementDuringAttack<T extends CreatureEntity & AttackStateHolder> extends Goal
	{
		private static final UUID MOVEMENT_MODIFIER_ATTACKING_UUID = UUID.fromString("a2793876-ee17-11ec-8ea0-0242ac120002");
		private static final AttributeModifier MOVEMENT_MODIFIER_ATTACKING = new AttributeModifier(MOVEMENT_MODIFIER_ATTACKING_UUID, "Attacking movement reduction", -1, AttributeModifier.Operation.MULTIPLY_TOTAL);
		
		private final T entity;
		
		public ZeroMovementDuringAttack(T entity)
		{
			this.entity = entity;
		}
		
		@Override
		public boolean canUse()
		{
			return this.entity.isAttacking();
		}
		
		@Override
		public void start()
		{
			ModifiableAttributeInstance instance = this.entity.getAttributes().getInstance(Attributes.MOVEMENT_SPEED);
			if(instance != null && !instance.hasModifier(MOVEMENT_MODIFIER_ATTACKING))
				instance.addTransientModifier(MOVEMENT_MODIFIER_ATTACKING);
		}
		
		@Override
		public void stop()
		{
			ModifiableAttributeInstance instance = this.entity.getAttributes().getInstance(Attributes.MOVEMENT_SPEED);
			if(instance != null)
				instance.removeModifier(MOVEMENT_MODIFIER_ATTACKING);
		}
	}
	
	public interface AttackStateHolder
	{
		/**
		 * @return the current state of the entity's melee attack
		 */
		AttackState getAttackState();
		
		/**
		 * Used to set the entity's attack state.
		 * The attack state is meant to be set exclusively by {@link MoveToTargetGoal}.
		 *
		 * @param state The new state of the entity's melee attack
		 */
		void setAttackState(AttackState state);
		
		
		/**
		 * @return true during the attack animation right before an attack lands.
		 */
		default boolean isPreparingToAttack()
		{
			return this.getAttackState() == AttackState.ATTACK;
		}
		
		/**
		 * Used to start animations and other things
		 *
		 * @return true if the entity performing an attack
		 */
		default boolean isAttacking()
		{
			AttackState state = this.getAttackState();
			return state == AttackState.ATTACK || state == AttackState.ATTACK_RECOVERY;
		}
	}
}
