package com.mraof.minestuck.entity.ai;

import com.mraof.minestuck.entity.AnimatedPathfinderMob;
import com.mraof.minestuck.entity.animation.MobAnimation;
import com.mraof.minestuck.entity.animation.PhasedMobAnimation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.Goal;
import org.joml.Vector3d;

import java.util.EnumSet;
import java.util.Objects;

/**
 * An abstract goal for handling PhasedMobAnimation compliant animated actions
 */
public abstract class MobAnimationPhaseGoal<T extends PathfinderMob & PhasedMobAnimation.Phases.Holder> extends Goal
{
	protected final T entity;
	protected final PhasedMobAnimation phasedAnimation;
	
	/**
	 * Starts at 0 and counts up, when this int matches any of the ints in phases, it transitions to the start of that given phase
	 */
	protected int time = 0;
	
	protected Vector3d lookTarget;
	protected double speed;
	
	public MobAnimationPhaseGoal(T entity, PhasedMobAnimation phasedAnimation)
	{
		this.entity = entity;
		this.phasedAnimation = phasedAnimation;
		
		MobAnimation animation = phasedAnimation.getAnimation();
		
		//code block has redundancy with the methods embedded in AnimatedPathfinderMob's setCurrentAnimation(), however entities not of that class are expected to be able to use this goal
		if(animation.freezeMovement() && !animation.freezeSight())
			this.setFlags(EnumSet.of(Flag.MOVE));
		else if(!animation.freezeMovement() && animation.freezeSight())
			this.setFlags(EnumSet.of(Flag.LOOK));
		else if(animation.freezeMovement() && animation.freezeSight())
			this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
	}
	
	@Override
	public boolean canUse()
	{
		return true;
	}
	
	@Override
	public boolean canContinueToUse()
	{
		return phasedAnimation.getCurrentPhase(entity, time, speed) != PhasedMobAnimation.Phases.NEUTRAL;
	}
	
	@Override
	public boolean isInterruptable()
	{
		return false; //by default, it should not be possible to interrupt an animated goal
	}
	
	@Override
	public void start()
	{
		MobAnimation animation = phasedAnimation.getAnimation();
		this.speed = MobAnimation.getAttributeAffectedSpeed(entity, phasedAnimation.getSpeedModifyingAttribute());
		
		if(entity instanceof AnimatedPathfinderMob animatedMob)
			animatedMob.setCurrentAnimation(animation, speed);
		
		LivingEntity target = this.entity.getTarget();
		
		if(animation.freezeSight() && target != null)
		{
			this.lookTarget = new Vector3d(target.getX(), target.getEyeY(), target.getZ());
		}
		
		this.entity.setAnimationPhase(PhasedMobAnimation.Phases.ANTICIPATION, animation.action());
	}
	
	@Override
	public void stop()
	{
		this.time = 0;
		if(entity instanceof AnimatedPathfinderMob animatedMob)
			animatedMob.endCurrentAction();
	}
	
	@Override
	public void tick()
	{
		//make sure the super function is ran in goals that extend this
		
		if(phasedAnimation.getAnimation().freezeSight() && lookTarget != null)
			this.entity.getLookControl().setLookAt(lookTarget.x, lookTarget.y, lookTarget.z);
		
		this.time++;
		phasedAnimation.attemptPhaseChange(time, entity, speed);
	}
}
