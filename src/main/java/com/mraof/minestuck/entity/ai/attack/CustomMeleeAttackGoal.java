package com.mraof.minestuck.entity.ai.attack;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;

/**
 * Used instead of {@link MeleeAttackGoal} for when you want a custom attack rate or distance multiplier
 */
public class CustomMeleeAttackGoal extends MeleeAttackGoal
{
	private final int attackRate;
	private final float distanceMultiplier;
	
	public CustomMeleeAttackGoal(PathfinderMob entity, float speed, boolean useMemory, int attackRate)
	{
		this(entity, speed, useMemory, attackRate, 2.0F);
	}
	
	public CustomMeleeAttackGoal(PathfinderMob entity, float speed, boolean useMemory, int attackRate, float distanceMultiplier)
	{
		super(entity, speed, useMemory);
		this.attackRate = attackRate;
		this.distanceMultiplier = distanceMultiplier;
	}
	
	
	@Override
	protected void resetAttackCooldown()
	{
		ticksUntilNextAttack = this.adjustedTickDelay(attackRate);
	}
	
	@Override
	protected double getAttackReachSqr(LivingEntity attackTarget)
	{
		return mob.getBbWidth() * distanceMultiplier * mob.getBbWidth() * distanceMultiplier + attackTarget.getBbWidth();
	}
}
