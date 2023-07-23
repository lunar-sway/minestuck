package com.mraof.minestuck.entity.ai;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.target.TargetGoal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.phys.AABB;

import java.util.EnumSet;
import java.util.List;
import java.util.function.Predicate;

public class HurtByTargetAlliedGoal extends TargetGoal
{
	private final Predicate<Entity> alliedPredicate;
	private int revengeTimer;

	public HurtByTargetAlliedGoal(Mob mob, Predicate<Entity> alliedPredicate)
	{
		super(mob, false);
		this.alliedPredicate = alliedPredicate;
		this.setFlags(EnumSet.of(Flag.TARGET));
	}

	/**
	 * Returns whether the EntityAIBase should begin execution.
	 */
	@Override
	public boolean canUse()
	{
		int i = this.mob.getLastHurtByMobTimestamp();
		return i != this.revengeTimer && this.canAttack(this.mob.getLastHurtByMob(), TargetingConditions.DEFAULT);
	}

	/**
	 * Execute a one shot task or start executing a continuous task
	 */
	@Override
	public void start()
	{
		this.mob.setTarget(this.mob.getLastHurtByMob());
		this.revengeTimer = this.mob.getLastHurtByMobTimestamp();
		
		double d0 = this.getFollowDistance();
		List<Mob> list = this.mob.level().getEntitiesOfClass(Mob.class, new AABB(this.mob.getX(), this.mob.getY(), this.mob.getZ(), this.mob.getX() + 1.0D, this.mob.getY() + 1.0D, this.mob.getZ() + 1.0D).inflate(d0, 10.0D, d0), alliedPredicate);
		
		for(Mob creature : list)
		{
			if(this.mob != creature && creature.getLastHurtByMob() == null && !creature.isAlliedTo(this.mob.getLastHurtByMob()))
			{
				creature.setTarget(this.mob.getLastHurtByMob());
			}
		}

		super.start();
	}
}