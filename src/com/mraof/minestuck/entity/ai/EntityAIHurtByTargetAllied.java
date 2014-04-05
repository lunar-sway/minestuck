package com.mraof.minestuck.entity.ai;

import java.util.Iterator;
import java.util.List;

import net.minecraft.command.IEntitySelector;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.ai.EntityAITarget;
import net.minecraft.util.AxisAlignedBB;

public class EntityAIHurtByTargetAllied extends EntityAITarget {
	IEntitySelector entitySelector;	
	private int field_142052_b;

	public EntityAIHurtByTargetAllied(EntityCreature par1EntityCreature, IEntitySelector entitySelector)
	{
		super(par1EntityCreature, false);
		this.entitySelector = entitySelector;
		this.setMutexBits(1);
	}

	/**
	 * Returns whether the EntityAIBase should begin execution.
	 */
	public boolean shouldExecute()
	{
		int i = this.taskOwner.func_142015_aE();
		return i != this.field_142052_b && this.isSuitableTarget(this.taskOwner.getAITarget(), false);
	}

	/**
	 * Execute a one shot task or start executing a continuous task
	 */
	public void startExecuting()
	{
		this.taskOwner.setAttackTarget(this.taskOwner.getAITarget());
		this.field_142052_b = this.taskOwner.func_142015_aE();

		double d0 = this.getTargetDistance();
		List<?> list = this.taskOwner.worldObj.selectEntitiesWithinAABB(this.taskOwner.getClass(), AxisAlignedBB.getAABBPool().getAABB(this.taskOwner.posX, this.taskOwner.posY, this.taskOwner.posZ, this.taskOwner.posX + 1.0D, this.taskOwner.posY + 1.0D, this.taskOwner.posZ + 1.0D).expand(d0, 10.0D, d0), entitySelector);
		Iterator<?> iterator = list.iterator();

		while (iterator.hasNext())
		{
			EntityCreature entitycreature = (EntityCreature)iterator.next();

			if (this.taskOwner != entitycreature && entitycreature.getAttackTarget() == null && !entitycreature.isOnSameTeam(this.taskOwner.getAITarget()))
			{
				entitycreature.setAttackTarget(this.taskOwner.getAITarget());
			}
		}

		super.startExecuting();
	}
}
