package com.mraof.minestuck.entity.ai;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import com.google.common.base.Predicate;

import net.minecraft.command.IEntitySelector;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAITarget;
import net.minecraft.entity.player.EntityPlayer;

public class EntityAINearestAttackableTargetWithHeight extends EntityAITarget 
{

	EntityLivingBase targetEntity;
	Class<? extends Entity> targetClass;
	int targetChance;
	private float targetHeightDistance;
	private final Predicate field_82643_g;

	/** Instance of EntityAINearestAttackableTargetSorter. */
	private EntityAINearestAttackableTargetWithHeightSorter theNearestAttackableTargetWithHeightSorter;
	private float targetDistance;

	public EntityAINearestAttackableTargetWithHeight(EntityCreature owner, Class<? extends Entity> par2Class, float par3, int par4, boolean par5)
	{
		this(owner, par2Class, par3, par4, par5, false);
	}

	public EntityAINearestAttackableTargetWithHeight(EntityCreature owner, Class<? extends Entity> par2Class, float par3, int par4, boolean par5, boolean par6)
	{
		this(owner, par2Class, par3, par4, par5, par6, (Predicate)null);
	}

	public EntityAINearestAttackableTargetWithHeight(EntityCreature owner,
			Class<? extends Entity> target, float par3, int par4, boolean par5, boolean par6, Predicate par7IEntitySelector)
	{
		super(owner, par5, par6);
		this.targetClass = target;
		this.targetDistance = par3;
		this.targetHeightDistance = 4;
		this.targetChance = par4;
		this.theNearestAttackableTargetWithHeightSorter = new EntityAINearestAttackableTargetWithHeightSorter(this, owner);
		this.field_82643_g = par7IEntitySelector;
		this.setMutexBits(1);
	}

	/**
	 * Returns whether the EntityAIBase should begin execution.
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public boolean shouldExecute()
	{
		if (this.targetChance > 0 && this.taskOwner.getRNG().nextInt(this.targetChance) != 0)
		{
			return false;
		}
		else
		{
			if (this.targetClass == EntityPlayer.class)
			{
				EntityPlayer entityplayer = this.taskOwner.worldObj.getClosestPlayerToEntity(this.taskOwner, (double)this.targetDistance);	//Was closest vulnerable player

				if (this.isSuitableTarget(entityplayer, false))
				{
					this.targetEntity = entityplayer;
					return true;
				}
			}
			else
			{
				List list = this.taskOwner.worldObj.getEntitiesWithinAABB(this.targetClass, this.taskOwner.getEntityBoundingBox().expand((double)this.targetDistance, this.targetHeightDistance, (double)this.targetDistance));
				Collections.sort(list, this.theNearestAttackableTargetWithHeightSorter);
				Iterator iterator = list.iterator();
				
				while (iterator.hasNext())
				{
					Entity entity = (Entity)iterator.next();
					EntityLivingBase entityliving = (EntityLivingBase)entity;

					if (this.isSuitableTarget(entityliving, false))
					{
						this.targetEntity = entityliving;
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
	public void startExecuting()
	{
		this.taskOwner.setAttackTarget(this.targetEntity);
		super.startExecuting();
	}
	public void setTargetHeightDistance(float targetHeightDistance) 
	{
		this.targetHeightDistance = targetHeightDistance;
	}

}
