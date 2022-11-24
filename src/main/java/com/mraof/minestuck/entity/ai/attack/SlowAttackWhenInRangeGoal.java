package com.mraof.minestuck.entity.ai.attack;

import com.mraof.minestuck.entity.ai.MobAnimationPhaseGoal;
import com.mraof.minestuck.entity.animation.MobAnimation;
import com.mraof.minestuck.entity.animation.MobAnimationPhases;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;

import javax.annotation.Nonnull;

/**
 * A goal for performing a slow melee attack when within hitting range.
 * The attack has a preparation phase that delays the actual attack from the moment when the target is first in range.
 * The goal updates the attack state of the attacker accordingly, so that the state can be used for animations and other things.
 */
public class SlowAttackWhenInRangeGoal<T extends PathfinderMob & MobAnimationPhases.Phases.Holder> extends MobAnimationPhaseGoal<T>
{
	public SlowAttackWhenInRangeGoal(T entity, MobAnimation animation, MobAnimationPhases phases)
	{
		super(entity, animation, phases);
	}
	
	@Override
	public boolean canUse()
	{
		LivingEntity target = this.entity.getTarget();
		return target != null && this.isValidTarget(target) && this.entity.getSensing().hasLineOfSight(target);
	}
	
	@Override
	public void tick()
	{
		super.tick();
		//if(animation.freezesSight())
		//	this.entity.getLookControl().setLookAt(lookTarget.x, lookTarget.y, lookTarget.z, 30.0F, 30.0F);
		
		//this.attackDuration = Math.max(this.attackDuration - 1, -1);
		//this.recoverDuration = Math.max(this.recoverDuration - 1, -1);
		
		if(time == phases.getContactStartTime())
		{
			LivingEntity target = this.entity.getTarget();
			if(target != null && this.isValidTarget(target))
			{
				this.entity.doHurtTarget(target);
				// TODO: AOE bounding box collision checks + aoe flag
			}
			//this.recoverDuration = this.attackRecovery;
			//this.entity.setAnimationPhase(MobAnimationPhases.Phases.RECOVERY, animation.getAction()); //TODO add contact phase
		}
		
		/*if(this.recoverDuration == 0)
		{
			this.entity.setAnimationPhase(MobAnimationPhases.Phases.NEUTRAL, animation.getAction());
		}*/
	}
	
	protected boolean isValidTarget(@Nonnull LivingEntity target)
	{
		return target.isAlive() && this.getAttackReachSqr(target) >= this.entity.distanceToSqr(target);
	}
	
	protected double getAttackReachSqr(LivingEntity target)
	{
		return this.entity.getBbWidth() * 2.0F * this.entity.getBbWidth() * 2.0F + target.getBbWidth();
	}
	
}
