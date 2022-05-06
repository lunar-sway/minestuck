package com.mraof.minestuck.entity.underling;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

public abstract class HeavyUnderlingEntity extends UnderlingEntity
{
	private static final DataParameter<Boolean> DATA_IS_ATTACKING = EntityDataManager.defineId(HeavyUnderlingEntity.class, DataSerializers.BOOLEAN);

	private int heavyAttackTicks = 0;
	private int recoveryTicks = 0;
	private final int heavyAttackDelay;
	private final int heavyAttackRecovery;

	/**
	 * Used to apply a delay before and after each attacks.
	 * Mainly used to sync attack animations.
	 * @param consortRep the consort reputation bonus if killed
	 * @param attackDelay the delay in game ticks before the damage is applied
	 * @param attackRecovery the recovery time in game ticks (used to finish the animation before another can start)
	 */
	public HeavyUnderlingEntity(EntityType<? extends HeavyUnderlingEntity> type, World world, int consortRep, int attackDelay, int attackRecovery)
	{
		super(type, world, consortRep);
		this.maxUpStep = 1.0F;
		this.heavyAttackDelay = attackDelay;
		this.heavyAttackRecovery = attackRecovery;
	}

	protected void defineSynchedData() {
		super.defineSynchedData();
		this.entityData.define(DATA_IS_ATTACKING, false);
	}

	/**
	 * Checks if performing a delayed attack.
	 * Can be used client side to trigger animations.
	 * @return true if there is a delayed attack in progress
	 */
	public boolean isPerformingHeavyAttack() {
		return this.entityData.get(DATA_IS_ATTACKING);
	}
	
	@Override
	protected void registerGoals()
	{
		super.registerGoals();
		goalSelector.addGoal(3, new HeavyAttackGoal(this, 1F, false));
	}

	@Override
	public void tick() {
		super.tick();
		if (heavyAttackTicks > 0) {
			heavyAttackTicks--;
			if (heavyAttackTicks == 0) {
				recoveryTicks = heavyAttackRecovery;
				if (getTarget() != null && isInRange(getTarget())) {
					doHurtTarget(getTarget());
				}
			}
		}
		if (recoveryTicks > 0) {
			recoveryTicks--;
			if (recoveryTicks == 0) {
				entityData.set(DATA_IS_ATTACKING, false);
			}
		}
	}

	private boolean isInRange(LivingEntity target) {
		double reach = this.getBbWidth() * 2.0F * this.getBbWidth() * 2.0F + target.getBbWidth();
		return this.distanceToSqr(target) <= reach;
	}

	private void startHeavyAttack() {
		if (heavyAttackTicks <= 0 && recoveryTicks <= 0) {
			heavyAttackTicks = heavyAttackDelay;
			entityData.set(DATA_IS_ATTACKING, true);
		}
	}

	private static class HeavyAttackGoal extends MeleeAttackGoal
	{
		private final HeavyUnderlingEntity entity;

		public HeavyAttackGoal(HeavyUnderlingEntity entity, float speed, boolean useMemory)
		{
			super(entity, speed, useMemory);
			this.entity = entity;
		}

		@Override
		protected void checkAndPerformAttack(LivingEntity enemy, double distToEnemySqr) {
			double reach = this.getAttackReachSqr(enemy);
			if (distToEnemySqr <= reach && this.ticksUntilNextAttack <= 0) {
				this.resetAttackCooldown();
				this.mob.swing(Hand.MAIN_HAND);
				this.mob.getNavigation().stop();
				entity.startHeavyAttack();
			}
		}
	}
}