package com.mraof.minestuck.entity.ai;

import com.mraof.minestuck.entity.AnimatedPathfinderMob;
import com.mraof.minestuck.entity.animation.MobAnimation;
import net.minecraft.world.entity.ai.goal.PanicGoal;

public class AnimatedPanicGoal extends PanicGoal
{
	private final AnimatedPathfinderMob entity;
	private final MobAnimation panicAnimation;
	
	public AnimatedPanicGoal(AnimatedPathfinderMob entity, double speedModifier, MobAnimation panicAnimation)
	{
		super(entity, speedModifier);
		this.entity = entity;
		this.panicAnimation = panicAnimation;
	}
	
	@Override
	public void start()
	{
		this.entity.setCurrentAnimation(panicAnimation);
		super.start();
	}
	
	@Override
	public void stop()
	{
		this.entity.endCurrentAction();
		super.stop();
	}
}
