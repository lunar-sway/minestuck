package com.mraof.minestuck.entity.ai;

import com.mojang.math.Vector3d;
import com.mraof.minestuck.entity.AnimatedPathfinderMob;
import com.mraof.minestuck.entity.animation.PhasedMobAnimation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.Goal;

import java.util.EnumSet;
import java.util.Objects;

/**
 * An abstract goal for handling PhasedMobAnimation compliant animated actions
 */
public abstract class MobAnimationPhaseGoal<T extends PathfinderMob & PhasedMobAnimation.Phases.Holder> extends Goal
{
	protected final T entity;
	protected final PhasedMobAnimation animation;
	
	/**
	 * Starts at 0 and counts up, when this int matches any of the ints in phases, it transitions to the start of that given phase
	 */
	protected int time = 0;
	
	protected Vector3d lookTarget;
	
	public MobAnimationPhaseGoal(T entity, PhasedMobAnimation animation)
	{
		this.entity = entity;
		this.animation = animation;
		
		//code block has some redundancy with the tick function in AnimatedPathfinderMob, however entities who are not members of that class are expected to be able to use this goal
		if(animation.freezesMovement() && !animation.freezesSight())
			this.setFlags(EnumSet.of(Flag.MOVE));
		else if(!animation.freezesMovement() && animation.freezesSight())
			this.setFlags(EnumSet.of(Flag.LOOK));
		else if(animation.freezesMovement() && animation.freezesSight())
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
		return animation.getCurrentPhase(time) != PhasedMobAnimation.Phases.NEUTRAL;
	}
	
	@Override
	public void start()
	{
		if(entity instanceof AnimatedPathfinderMob animatedMob)
			animatedMob.setCurrentAnimation(animation);
		
		if(animation.freezesSight())
		{
			//the target should be guaranteed to be non-null because canUse() requires it to be non-null.
			LivingEntity target = Objects.requireNonNull(this.entity.getTarget());
			this.lookTarget = new Vector3d(target.getX(), target.getEyeY(), target.getZ());
		}
		
		this.entity.setAnimationPhase(PhasedMobAnimation.Phases.ANTICIPATION, animation.getAction());
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
		
		if(animation.freezesSight())
			this.entity.getLookControl().setLookAt(lookTarget.x, lookTarget.y, lookTarget.z);
		
		this.time++;
		animation.attemptPhaseChange(time, entity);
	}
}
