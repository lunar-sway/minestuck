package com.mraof.minestuck.entity.ai;

import com.mraof.minestuck.entity.AnimatedPathfinderMob;
import com.mraof.minestuck.entity.animation.MSMobAnimations;
import net.minecraft.world.entity.ai.goal.PanicGoal;

public class AnimatedPanicGoal extends PanicGoal
{
	private final AnimatedPathfinderMob entity;
	
	public AnimatedPanicGoal(AnimatedPathfinderMob entity, double speedModifier)
	{
		super(entity, speedModifier);
		this.entity = entity;
	}
	
	@Override
	public void start()
	{
		MSMobAnimations animation = this.entity.getPanicAnimation();
		if(animation != null)
			this.entity.setCurrentAnimation(animation);
		super.start();
	}
	
	@Override
	public void stop()
	{
		this.entity.setCurrentAnimation(MSMobAnimations.DEFAULT_IDLE_ANIMATION);
		super.stop();
	}
}
