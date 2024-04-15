package com.mraof.minestuck.entity.ai.attack;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;

/**
 * The same as {@link MeleeAttackGoal}, except that the goal does not handle the actual attack.
 */
public class MoveToTargetGoal extends MeleeAttackGoal
{
	public MoveToTargetGoal(PathfinderMob entity, float speed, boolean followsUnseenTarget)
	{
		super(entity, speed, followsUnseenTarget);
	}
	
	@Override
	protected void checkAndPerformAttack(LivingEntity enemy)
	{
	}
}
