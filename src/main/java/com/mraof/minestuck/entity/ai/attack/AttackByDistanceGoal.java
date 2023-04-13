package com.mraof.minestuck.entity.ai.attack;

import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.monster.RangedAttackMob;

import java.util.EnumSet;

public class AttackByDistanceGoal extends Goal
{
	/** The entity the AI instance has been applied to */
	private final Mob entityHost;

	/**
	 * The entity (as a RangedAttackMob) the AI instance has been applied to.
	 */
	private final Mob attacker;
	private LivingEntity attackTarget;

	/**
	 * A decrementing tick that spawns a ranged attack once this value reaches 0. It is then set back to the
	 * maxRangedAttackTime.
	 */
	private int rangedAttackTime;
	private float entityMoveSpeed;
	private int ticksSeeingTarget;
	private int attackIntervalMin;

	/**
	 * The maximum time the AI has to wait before peforming another ranged attack.
	 */
	private int maxRangedAttackTime;
	private float attackRadius;
	private float attackRadiusSqr;

	public AttackByDistanceGoal(RangedAttackMob par1IRangedAttackMob, float par2, int par3, float par4)
	{
		this(par1IRangedAttackMob, par2, par3, par3, par4);
	}

	public AttackByDistanceGoal(RangedAttackMob par1IRangedAttackMob, float par2, int par3, int par4, float par5)
	{
		this.rangedAttackTime = -1;
		this.ticksSeeingTarget = 0;

		if(!(par1IRangedAttackMob instanceof Mob mob))
		{
			throw new IllegalArgumentException("ArrowAttackGoal requires Mob implements RangedAttackMob");
		}
		else
		{
			this.attacker = mob;
			this.entityHost = mob;
			this.entityMoveSpeed = par2;
			this.attackIntervalMin = par3;
			this.maxRangedAttackTime = par4;
			this.attackRadius = par5;
			this.attackRadiusSqr = par5 * par5;
			this.setFlags(EnumSet.of(Flag.TARGET, Flag.MOVE));
		}
	}

	/**
	 * Returns whether the EntityAIBase should begin execution.
	 */
	@Override
	public boolean canUse()
	{
		LivingEntity entityliving = this.entityHost.getTarget();

		if (entityliving == null)
			return false;
		else
		{
			if(entityliving.isAlive())
			{
				this.attackTarget = entityliving;
				return true;
			}
			else
				return false;
		}
	}

	/**
	 * Returns whether an in-progress EntityAIBase should continue executing
	 */
	@Override
	public boolean canContinueToUse()
	{
		return this.canUse() || !this.entityHost.getNavigation().isDone();
	}

	/**
	 * Resets the task
	 */
	@Override
	public void stop()
	{
		this.attackTarget = null;
		this.ticksSeeingTarget = 0;
		this.rangedAttackTime = -1;
	}
	
	
	@Override
	public void tick()
	{
		double d0 = this.entityHost.distanceToSqr(this.attackTarget.getX(), this.attackTarget.getBoundingBox().minY, this.attackTarget.getZ());
		boolean flag = this.entityHost.getSensing().hasLineOfSight(this.attackTarget);

		if (flag)
		{
			++this.ticksSeeingTarget;
		}
		else
		{
			this.ticksSeeingTarget = 0;
		}

		if (d0 <= (double)this.attackRadiusSqr && this.ticksSeeingTarget >= 20)
		{
			this.entityHost.getNavigation().stop();
		}
		else
		{
			this.entityHost.getNavigation().moveTo(this.attackTarget, this.entityMoveSpeed);
		}

		this.entityHost.getLookControl().setLookAt(this.attackTarget, 30.0F, 30.0F);
		float f;
		double meleeRange = (double)(this.attacker.getBbWidth() * 2.0F * this.attacker.getBbWidth() * 2.0F);
		if(d0 > meleeRange)
		{
			if (--this.rangedAttackTime == 0)
			{
				if (d0 > (double)this.attackRadiusSqr || !flag)
				{
					return;
				}
	
				f = (float) Math.sqrt(d0) / this.attackRadius;
				float f1 = Mth.clamp(f, 0.1F, 1.0F);
				
				((RangedAttackMob) this.attacker).performRangedAttack(this.attackTarget, f1);
				this.rangedAttackTime = Mth.floor(f * (float)(this.maxRangedAttackTime - this.attackIntervalMin) + (float)this.attackIntervalMin);
			}
			else if (this.rangedAttackTime < 0)
			{
				f = (float) Math.sqrt(d0) / this.attackRadius;
				this.rangedAttackTime = Mth.floor(f * (float)(this.maxRangedAttackTime - this.attackIntervalMin) + (float)this.attackIntervalMin);
			}
		}
		else
		{
			this.rangedAttackTime = Math.max(this.rangedAttackTime - 1, 0);
			if(this.attacker.distanceToSqr(this.attackTarget.getX(), this.attackTarget.getBoundingBox().minY, this.attackTarget.getZ()) <= d0)
			{
				if(this.rangedAttackTime <= 0)
				{
					this.rangedAttackTime = 20;
	
					if(!this.attacker.getItemBySlot(EquipmentSlot.MAINHAND).isEmpty())
					{
						this.attacker.swing(InteractionHand.MAIN_HAND);
					}
	
					this.attacker.doHurtTarget(this.attackTarget);
				}
			}
		}
	}
}

