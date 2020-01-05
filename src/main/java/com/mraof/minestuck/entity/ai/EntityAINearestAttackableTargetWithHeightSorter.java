package com.mraof.minestuck.entity.ai;

import net.minecraft.entity.Entity;

import java.util.Comparator;

public class EntityAINearestAttackableTargetWithHeightSorter implements Comparator<Entity> 
{
	private Entity theEntity;

	final NearestAttackableTargetWithHeightGoal parent;

	public EntityAINearestAttackableTargetWithHeightSorter(NearestAttackableTargetWithHeightGoal par1EntityAINearestAttackableTarget, Entity par2Entity)
	{
		this.parent = par1EntityAINearestAttackableTarget;
		this.theEntity = par2Entity;
	}
	
	public int compareDistanceSq(Entity par1Entity, Entity par2Entity)
	{
		double d0 = this.theEntity.getDistanceSq(par1Entity);
		double d1 = this.theEntity.getDistanceSq(par2Entity);
		return Double.compare(d0, d1);
	}
	
	@Override
	public int compare(Entity entity1, Entity entity2)
	{
		return this.compareDistanceSq(entity1, entity2);
	}
}
