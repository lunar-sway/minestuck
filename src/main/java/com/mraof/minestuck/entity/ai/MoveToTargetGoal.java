package com.mraof.minestuck.entity.ai;

import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.MeleeAttackGoal;

/**
 * The same as MeleeAttackGoal, except that the goal does not handle the actual attack.
 */
public class MoveToTargetGoal extends MeleeAttackGoal
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
