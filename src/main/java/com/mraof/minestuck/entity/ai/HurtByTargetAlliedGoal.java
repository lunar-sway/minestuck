package com.mraof.minestuck.entity.ai;

import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityPredicate;
import net.minecraft.entity.ai.goal.TargetGoal;
import net.minecraft.util.math.AxisAlignedBB;

import java.util.EnumSet;
import java.util.List;
import java.util.function.Predicate;

public class HurtByTargetAlliedGoal extends TargetGoal
{
	private final Predicate<Entity> alliedPredicate;
	private int revengeTimer;

	public HurtByTargetAlliedGoal(CreatureEntity par1EntityCreature, Predicate<Entity> alliedPredicate)
	{
		super(par1EntityCreature, false);
		this.alliedPredicate = alliedPredicate;
		this.setMutexFlags(EnumSet.of(Flag.TARGET));
	}

	/**
	 * Returns whether the EntityAIBase should begin execution.
	 */
	@Override
	public boolean shouldExecute()
	{
		int i = this.goalOwner.getRevengeTimer();
		return i != this.revengeTimer && this.isSuitableTarget(this.goalOwner.getRevengeTarget(), EntityPredicate.DEFAULT);
	}

	/**
	 * Execute a one shot task or start executing a continuous task
	 */
	@Override
	public void startExecuting()
	{
		this.goalOwner.setAttackTarget(this.goalOwner.getRevengeTarget());
		this.revengeTimer = this.goalOwner.getRevengeTimer();
		
		double d0 = this.getTargetDistance();
		List<CreatureEntity> list = this.goalOwner.world.getEntitiesWithinAABB(CreatureEntity.class, new AxisAlignedBB(this.goalOwner.getPosX(), this.goalOwner.getPosY(), this.goalOwner.getPosZ(), this.goalOwner.getPosX() + 1.0D, this.goalOwner.getPosY() + 1.0D, this.goalOwner.getPosZ() + 1.0D).grow(d0, 10.0D, d0), alliedPredicate);
		
		for(CreatureEntity creature : list)
		{
			if(this.goalOwner != creature && creature.getRevengeTarget() == null && !creature.isOnSameTeam(this.goalOwner.getRevengeTarget()))
			{
				creature.setAttackTarget(this.goalOwner.getRevengeTarget());
			}
		}

		super.startExecuting();
	}
}