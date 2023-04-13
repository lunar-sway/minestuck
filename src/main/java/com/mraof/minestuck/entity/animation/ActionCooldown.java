package com.mraof.minestuck.entity.animation;

/**
 * Used to help standardize the method in which action cooldown(how long between uses of an animated action) values are maintained
 */
public interface ActionCooldown
{
	//TODO Figure out if it can be made so that group cooldowns can be applied to any animated entities with the same Goal, as opposed to giving a cooldown to just entities of the same type
	
	int getCooldown();
	
	void setCooldown(int amountTicks);
	
	default boolean existingCooldownIsLonger(int newCooldown)
	{
		return this.getCooldown() > newCooldown;
	}
	
	default boolean hasFinishedCooldown()
	{
		return this.getCooldown() == 0;
	}
}