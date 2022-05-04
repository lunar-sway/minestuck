package com.mraof.minestuck.entity.ai;

import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Hand;

public class DelayedMeleeAttackGoal extends CustomMeleeAttackGoal
{
	private final int delay;
	private boolean attackStarted;
	private int remainingTicks;

	public DelayedMeleeAttackGoal(CreatureEntity entity, float speed, boolean useMemory, int attackRate, float distanceMultiplier, int delay)
	{
		super(entity, speed, useMemory, attackRate, distanceMultiplier);
		this.delay = delay;
	}

	@Override
	public void tick() {
		super.tick();
		if (attackStarted) {
			remainingTicks--;
			if (remainingTicks <= 0) {
				attackStarted = false;
				this.attacker.attackEntityAsMob(this.attacker.getAttackTarget());
			}
		}
	}

	@Override
	protected void checkAndPerformAttack(LivingEntity enemy, double distToEnemySqr) {
		double reach = this.getAttackReachSqr(enemy);
		if (distToEnemySqr <= reach && this.attackTick <= 0) {
			this.attackTick = this.attackRate;
			this.attacker.swingArm(Hand.MAIN_HAND);

			if (!attackStarted) {
				attackStarted = true;
				remainingTicks = delay;
			}
		}
	}
}
