package com.mraof.minestuck.entity.ai;

import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.pathfinding.Path;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

import java.util.EnumSet;

public class AttackOnCollideWithRateGoal extends Goal
{
	World worldObj;
	CreatureEntity attacker;
	LivingEntity entityTarget;

	/**
	 * An amount of decrementing ticks that allows the entity to attack once the tick reaches 0.
	 */
	int attackTick;
	float movementSpeed;
	boolean willSearch;

	/** The PathEntity of our entity. */
	Path entityPath;
	EntityType<?> targetType;
	private int movementTime;
	private int attackRate;
	private float distanceMultiplier = 2.0F;

	public AttackOnCollideWithRateGoal(CreatureEntity entity, EntityType<?> type, float par3, int attackRate, boolean par4)
	{
		this(entity, par3, attackRate, par4);
		this.targetType = type;
	}

	public AttackOnCollideWithRateGoal(CreatureEntity entity, float par2, int attackRate, boolean par3)
	{
		this.attackTick = 0;
		this.attacker = entity;
		this.worldObj = entity.world;
		this.movementSpeed = par2;
		this.attackRate = attackRate;
		this.willSearch = par3;
		this.setMutexFlags(EnumSet.of(Flag.TARGET, Flag.MOVE));
	}

	/**
	 * Returns whether the EntityAIBase should begin execution.
	 */
	@Override
	public boolean shouldExecute()
	{
		LivingEntity target = this.attacker.getAttackTarget();

		if(target == null)
		{
			return false;
		}
		else if(this.targetType != null && this.targetType != target.getType())
		{
			return false;
		}
		else
		{
			this.entityTarget = target;
			this.entityPath = this.attacker.getNavigator().getPathToEntityLiving(this.entityTarget);
			return this.entityPath != null;
		}
	}

	/**
	 * Returns whether an in-progress EntityAIBase should continue executing
	 */
	@Override
	public boolean shouldContinueExecuting()
	{
		LivingEntity entityliving = this.attacker.getAttackTarget();
		return entityliving != null && this.entityTarget.isAlive() && (!this.willSearch ? !this.attacker.getNavigator().noPath() : this.attacker.isWithinHomeDistanceFromPosition(new BlockPos(MathHelper.floor(this.entityTarget.posX), MathHelper.floor(this.entityTarget.posY), MathHelper.floor(this.entityTarget.posZ))));
	}

	/**
	 * Execute a one shot task or start executing a continuous task
	 */
	@Override
	public void startExecuting()
	{
		this.attacker.getNavigator().setPath(this.entityPath, this.movementSpeed);
		this.movementTime = 0;
	}

	/**
	 * Resets the task
	 */
	@Override
	public void resetTask()
	{
		this.entityTarget = null;
		this.attacker.getNavigator().clearPath();
	}
	
	@Override
	public void tick()
	{
		this.attacker.getLookController().setLookPositionWithEntity(this.entityTarget, 30.0F, 30.0F);

		if ((this.willSearch || this.attacker.getEntitySenses().canSee(this.entityTarget)) && --this.movementTime <= 0)
		{
			this.movementTime = 4 + this.attacker.getRNG().nextInt(7);
			this.attacker.getNavigator().tryMoveToEntityLiving(this.entityTarget, this.movementSpeed);
		}

		this.attackTick = Math.max(this.attackTick - 1, 0);
		double d0 = (double)(this.attacker.getWidth() * distanceMultiplier * this.attacker.getWidth() * distanceMultiplier);

		if (this.attacker.getDistanceSq(this.entityTarget.posX, this.entityTarget.getBoundingBox().minY, this.entityTarget.posZ) - (entityTarget.getWidth() / 2 ) <= d0)
		{
			if (this.attackTick <= 0)
			{
				this.attackTick = this.attackRate;

				if (!this.attacker.getItemStackFromSlot(EquipmentSlotType.MAINHAND).isEmpty())
				{
					this.attacker.swingArm(Hand.MAIN_HAND);
				}

				this.attacker.attackEntityAsMob(this.entityTarget);
			}
		}
	}
	public void setDistanceMultiplier(float distanceMultiplier) 
	{
		this.distanceMultiplier = distanceMultiplier;
	}
}
