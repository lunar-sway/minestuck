package com.mraof.minestuck.entity.ai.attack;

import com.mraof.minestuck.entity.AnimatedPathfinderMob;
import com.mraof.minestuck.entity.ai.MobAnimationPhaseGoal;
import com.mraof.minestuck.entity.animation.PhasedMobAnimation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;

import javax.annotation.Nonnull;

/**
 * A goal for performing a slow melee attack when within hitting range.
 * The attack will occur at the start of the Contact PhasedMobAnimation.Phases
 */
public class AnimatedAttackWhenInRangeGoal<T extends AnimatedPathfinderMob & PhasedMobAnimation.Phases.Holder> extends MobAnimationPhaseGoal<T>
{
	public static final float STANDARD_MELEE_RANGE = -1;
	
	private final float minRange;
	private final float maxRange;
	
	public AnimatedAttackWhenInRangeGoal(T entity, PhasedMobAnimation animation, float minRange, float maxRange)
	{
		super(entity, animation);
		this.minRange = minRange;
		this.maxRange = maxRange;
	}
	
	public AnimatedAttackWhenInRangeGoal(T entity, PhasedMobAnimation animation)
	{
		super(entity, animation);
		this.minRange = 0;
		this.maxRange = STANDARD_MELEE_RANGE;
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
		
		if(time == phasedAnimation.getContactStartTime())
		{
			LivingEntity target = this.entity.getTarget();
			attemptToLandAttack(this.entity, target);
		}
	}
	
	protected boolean isValidTarget(@Nonnull LivingEntity target)
	{
		return target.isAlive() && withinRange(target);
	}
	
	protected boolean targetCanBeHit(@Nonnull LivingEntity target)
	{
		return target.isAlive() && belowMaximumRange(target);
	}
	
	protected boolean belowMaximumRange(@Nonnull LivingEntity target)
	{
		float moddedMaxRange = maxRange == STANDARD_MELEE_RANGE ? this.getStandardAttackReachSqr(target) : maxRange * maxRange;
		return moddedMaxRange >= this.entity.distanceToSqr(target);
	}
	
	protected boolean aboveMinimumRange(@Nonnull LivingEntity target)
	{
		float moddedMinRange = minRange == STANDARD_MELEE_RANGE ? this.getStandardAttackReachSqr(target) : minRange * minRange;
		return moddedMinRange <= this.entity.distanceToSqr(target);
	}
	
	protected float getStandardAttackReachSqr(LivingEntity target)
	{
		return this.entity.getBbWidth() * 2.0F * this.entity.getBbWidth() * 2.0F + target.getBbWidth();
	}
	
	protected boolean withinRange(@Nonnull LivingEntity target)
	{
		//when min or max range values are -1, the standard melee range is used instead of the actual value
		return belowMaximumRange(target) && aboveMinimumRange(target);
	}
	
	public void attemptToLandAttack(PathfinderMob attacker, LivingEntity target)
	{
		if(target != null && targetCanBeHit(target))
		{
			attacker.doHurtTarget(target);
			// TODO: AOE bounding box collision checks + aoe flag
		}
	}
}