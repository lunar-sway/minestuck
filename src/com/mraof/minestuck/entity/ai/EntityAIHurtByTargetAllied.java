package com.mraof.minestuck.entity.ai;

import com.google.common.base.Predicate;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAITarget;
import net.minecraft.util.math.AxisAlignedBB;

import java.util.Iterator;
import java.util.List;

public class EntityAIHurtByTargetAllied extends EntityAITarget {
	Predicate<Entity> entitySelector;
	private int revengeTimer;

	public EntityAIHurtByTargetAllied(EntityCreature par1EntityCreature, Predicate<Entity> entitySelector)
	{
		super(par1EntityCreature, false);
		this.entitySelector = entitySelector;
		this.setMutexBits(1);
	}

	/**
	 * Returns whether the EntityAIBase should begin execution.
	 */
	@Override
	public boolean shouldExecute()
	{
		int i = this.taskOwner.getRevengeTimer();
		return i != this.revengeTimer && this.isSuitableTarget(this.taskOwner.getAttackTarget(), false);
	}

	/**
	 * Execute a one shot task or start executing a continuous task
	 */
	@Override
	public void startExecuting()
	{
		this.taskOwner.setAttackTarget(this.taskOwner.getAttackTarget());
		this.revengeTimer = this.taskOwner.getRevengeTimer();
		
		double d0 = this.getTargetDistance();
		List<?> list = this.taskOwner.world.getEntitiesWithinAABB(EntityLivingBase.class, new AxisAlignedBB(this.taskOwner.posX, this.taskOwner.posY, this.taskOwner.posZ, this.taskOwner.posX + 1.0D, this.taskOwner.posY + 1.0D, this.taskOwner.posZ + 1.0D).grow(d0, 10.0D, d0), entitySelector);
		Iterator<?> iterator = list.iterator();
		
		while (iterator.hasNext())
		{
			EntityCreature entitycreature = (EntityCreature)iterator.next();
			
			if (this.taskOwner != entitycreature && entitycreature.getAttackTarget() == null && !entitycreature.isOnSameTeam(this.taskOwner.getAttackTarget()))
			{
				entitycreature.setAttackTarget(this.taskOwner.getAttackTarget());
			}
		}

		super.startExecuting();
	}
}
