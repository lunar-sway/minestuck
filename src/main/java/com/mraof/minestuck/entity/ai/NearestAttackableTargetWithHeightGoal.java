package com.mraof.minestuck.entity.ai;

import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.EntityPredicate;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.TargetGoal;
import net.minecraft.entity.player.PlayerEntity;

import java.util.Collections;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.List;
import java.util.function.Predicate;

public class NearestAttackableTargetWithHeightGoal extends TargetGoal
{

	LivingEntity targetEntity;
	Class<? extends LivingEntity> targetClass;
	int targetChance;
	private float targetHeightDistance;
	private final Predicate targetPredicate;

	/** Instance of EntityAINearestAttackableTargetSorter. */
	private EntityAINearestAttackableTargetWithHeightSorter theNearestAttackableTargetWithHeightSorter;
	private float targetDistance;

	public NearestAttackableTargetWithHeightGoal(CreatureEntity owner, Class<? extends LivingEntity> par2Class, float par3, int par4, boolean par5)
	{
		this(owner, par2Class, par3, par4, par5, false);
	}

	public NearestAttackableTargetWithHeightGoal(CreatureEntity owner, Class<? extends LivingEntity> par2Class, float par3, int par4, boolean par5, boolean par6)
	{
		this(owner, par2Class, par3, par4, par5, par6, null);
	}

	public NearestAttackableTargetWithHeightGoal(CreatureEntity owner,
												 Class<? extends LivingEntity> target, float par3, int par4, boolean par5, boolean par6, Predicate par7IEntitySelector)
	{
		super(owner, par5, par6);
		this.targetClass = target;
		this.targetDistance = par3;
		this.targetHeightDistance = 4;
		this.targetChance = par4;
		this.theNearestAttackableTargetWithHeightSorter = new EntityAINearestAttackableTargetWithHeightSorter(this, owner);
		this.targetPredicate = par7IEntitySelector;
		this.setMutexFlags(EnumSet.of(Flag.TARGET));
	}

	/**
	 * Returns whether the EntityAIBase should begin execution.
	 */
	@Override
	public boolean shouldExecute()
	{
		if (this.targetChance > 0 && this.goalOwner.getRNG().nextInt(this.targetChance) != 0)
		{
			return false;
		}
		else
		{
			if (this.targetClass == PlayerEntity.class)
			{
				PlayerEntity entityplayer = this.goalOwner.world.getClosestPlayer(this.goalOwner, (double)this.targetDistance);	//Was closest vulnerable player

				if (this.isSuitableTarget(entityplayer, EntityPredicate.DEFAULT))
				{
					this.targetEntity = entityplayer;
					return true;
				}
			}
			else
			{
				List<? extends LivingEntity> list = this.goalOwner.world.getEntitiesWithinAABB(this.targetClass, this.goalOwner.getBoundingBox().grow((double)this.targetDistance, this.targetHeightDistance, (double)this.targetDistance), targetPredicate);
				Collections.sort(list, this.theNearestAttackableTargetWithHeightSorter);
				Iterator<? extends LivingEntity> iterator = list.iterator();
				
				while(iterator.hasNext())
				{
					LivingEntity entity = iterator.next();

					if (this.isSuitableTarget(entity, EntityPredicate.DEFAULT))
					{
						this.targetEntity = entity;
						return true;
					}
				}
			}

			return false;
		}
	}

	/**
	 * Execute a one shot task or start executing a continuous task
	 */
	@Override
	public void startExecuting()
	{
		this.goalOwner.setAttackTarget(this.targetEntity);
		super.startExecuting();
	}
	public void setTargetHeightDistance(float targetHeightDistance) 
	{
		this.targetHeightDistance = targetHeightDistance;
	}
}