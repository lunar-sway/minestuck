package com.mraof.minestuck.util;

import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.controller.AnimationController;

/**
 * Collection of helper functions for common-side animations.
 */
public final class AnimationUtil
{
	/**
	 * Helper to create a new animation controller with custom animation speed
	 *
	 * @param name      name of this controller
	 * @param speed     animation speed - default speed is 1
	 * @param predicate the animation predicate
	 * @return a configured animation controller with speed
	 */
	public static <T extends IAnimatable> AnimationController<T> createAnimation(T entity, String name, double speed, AnimationController.IAnimationPredicate<T> predicate)
	{
		AnimationController<T> controller = new AnimationController<>(entity, name, 0, predicate);
		controller.setAnimationSpeed(speed);
		return controller;
	}
}
