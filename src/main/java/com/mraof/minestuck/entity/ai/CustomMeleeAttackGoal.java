package com.mraof.minestuck.entity.ai;

import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.MeleeAttackGoal;

/**
 * Used instead of {@link MeleeAttackGoal} for when you want a custom attack rate or distance multiplier
 */
public class CustomMeleeAttackGoal extends MeleeAttackGoal
{
	private final int attackRate;
	private final float distanceMultiplier;
	
	public CustomMeleeAttackGoal(CreatureEntity entity, float speed, boolean useMemory, int attackRate)
	{
		this(entity, speed, useMemory, attackRate, 2.0F);
	}
	
	public CustomMeleeAttackGoal(CreatureEntity entity, float speed, boolean useMemory, int attackRate, float distanceMultiplier)
	{
		super(entity, speed, useMemory);
		this.attackRate = attackRate;
		this.distanceMultiplier = distanceMultiplier;
	}
	
	
	@Override
	protected void resetAttackCooldown()
	{
		ticksUntilNextAttack = attackRate;
	}
	
	@Override
	protected double getAttackReachSqr(LivingEntity attackTarget)
	{
		return mob.getBbWidth() * distanceMultiplier * mob.getBbWidth() * distanceMultiplier + attackTarget.getBbWidth();
	}
}
