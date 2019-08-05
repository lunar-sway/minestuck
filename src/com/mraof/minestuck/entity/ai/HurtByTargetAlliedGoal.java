package com.mraof.minestuck.entity.ai;

import com.google.common.base.Predicate;
import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityPredicate;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.goal.TargetGoal;
import net.minecraft.util.math.AxisAlignedBB;

import java.util.EnumSet;
import java.util.Iterator;
import java.util.List;

public class HurtByTargetAlliedGoal extends TargetGoal
{
	Predicate<Entity> entitySelector;
	private int revengeTimer;

	public HurtByTargetAlliedGoal(CreatureEntity par1EntityCreature, Predicate<Entity> entitySelector)
	{
		super(par1EntityCreature, false);
		this.entitySelector = entitySelector;
		this.setMutexFlags(EnumSet.of(Flag.TARGET));
	}

	/**
	 * Returns whether the EntityAIBase should begin execution.
	 */
	@Override
	public boolean shouldExecute()
	{
		int i = this.goalOwner.getRevengeTimer();
		return i != this.revengeTimer && this.isSuitableTarget(this.goalOwner.getAttackTarget(), EntityPredicate.DEFAULT);
	}

	/**
	 * Execute a one shot task or start executing a continuous task
	 */
	@Override
	public void startExecuting()
	{
		this.goalOwner.setAttackTarget(this.goalOwner.getAttackTarget());
		this.revengeTimer = this.goalOwner.getRevengeTimer();
		
		double d0 = this.getTargetDistance();
		List<CreatureEntity> list = this.goalOwner.world.getEntitiesWithinAABB(CreatureEntity.class, new AxisAlignedBB(this.goalOwner.posX, this.goalOwner.posY, this.goalOwner.posZ, this.goalOwner.posX + 1.0D, this.goalOwner.posY + 1.0D, this.goalOwner.posZ + 1.0D).grow(d0, 10.0D, d0), entitySelector);
		Iterator<CreatureEntity> iterator = list.iterator();
		
		while (iterator.hasNext())
		{
			CreatureEntity creature = iterator.next();
			
			if (this.goalOwner != creature && creature.getAttackTarget() == null && !creature.isOnSameTeam(this.goalOwner.getAttackTarget()))
			{
				creature.setAttackTarget(this.goalOwner.getAttackTarget());
			}
		}

		super.startExecuting();
	}
}