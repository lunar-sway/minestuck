package com.mraof.minestuck.entity.ai.attack;

import com.mraof.minestuck.entity.ai.MobAnimationPhaseGoal;
import com.mraof.minestuck.entity.animation.ActionCooldown;
import com.mraof.minestuck.entity.animation.PhasedMobAnimation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;

import javax.annotation.Nonnull;

/**
 * A goal for performing a slow melee attack when within hitting range.
 * The attack will occur at the start of the Contact PhasedMobAnimation.Phases
 */
public class AnimatedAttackWhenInRangeGoal<T extends PathfinderMob & PhasedMobAnimation.Phases.Holder> extends MobAnimationPhaseGoal<T>
{
	public static final float STANDARD_MELEE_RANGE = -1;
	public static final int NO_COOLDOWN = -1;
	
	protected final float minRange;
	protected final float maxRange;
	protected final int actionCooldown;
	
	public AnimatedAttackWhenInRangeGoal(T entity, PhasedMobAnimation animation, float minRange, float maxRange, int actionCooldown)
	{
		super(entity, animation);
		this.minRange = minRange;
		this.maxRange = maxRange;
		this.actionCooldown = actionCooldown;
	}
	
	/**
	 * For typical melee attacks in entities with alternate attacks that shouldn't use said attack immediately after
	 */
	public AnimatedAttackWhenInRangeGoal(T entity, PhasedMobAnimation animation, int actionCooldown)
	{
		this(entity, animation, 0, STANDARD_MELEE_RANGE, actionCooldown);
	}
	
	/**
	 * For typical melee attacks
	 */
	public AnimatedAttackWhenInRangeGoal(T entity, PhasedMobAnimation animation)
	{
		this(entity, animation, 0, STANDARD_MELEE_RANGE, NO_COOLDOWN);
	}
	
	@Override
	public boolean canUse()
	{
		LivingEntity target = this.entity.getTarget();
		return target != null && this.isValidTarget(target) && this.entity.getSensing().hasLineOfSight(target); //this goal is not sensitive to cooldowns. Cooldown sensitivity is determined in goals extending this
	}
	
	@Override
	public void stop()
	{
		super.stop();
		
		//at the end of the attack a cooldown may be applied to cooldown compatible entities to prevent an animated goal from being spammed
		if(actionCooldown != NO_COOLDOWN && entity instanceof ActionCooldown cooldownEntity)
			cooldownEntity.setCooldown(actionCooldown);
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
	
	/**
	 * For use once the attack has started, even if the target is now closer than the minimum range would allow
	 */
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